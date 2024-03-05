package com.msmeli.service.feignService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.dto.*;
import com.msmeli.dto.response.BuyBoxWinnerResponseDTO;
import com.msmeli.dto.response.CatalogItemResponseDTO;
import com.msmeli.dto.response.FeeResponseDTO;
import com.msmeli.dto.response.OptionsDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.*;
import com.msmeli.repository.*;
import com.msmeli.service.services.SellerService;
import com.msmeli.service.services.UserEntityService;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class MeliService {

    private final MeliFeignClient meliFeignClient;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ListingTypeRepository listingTypeRepository;
    private  final UserEntityService userEntityService;
    private final SellerService sellerService;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    public MeliService(MeliFeignClient meliFeignClient, ItemRepository itemRepository, CategoryRepository categoryRepository, ListingTypeRepository listingTypeRepository, UserEntityService userEntityService, SellerService sellerService, ObjectMapper objectMapper1) {
        this.meliFeignClient = meliFeignClient;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.listingTypeRepository = listingTypeRepository;
        this.userEntityService = userEntityService;
        this.sellerService = sellerService;
        this.objectMapper = objectMapper1;
        this.modelMapper = new ModelMapper();
    }

    public Item getItemById(String itemId) throws Exception {
        return itemRepository.findById(itemId).orElseThrow(() -> new Exception("Item not found"));
    }

    public Category getCategory(String categoryId) throws Exception {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new Exception("Category not found"));
    }



    public Category saveCategory(String categoryId) {
        DocumentContext json = JsonPath.parse(meliFeignClient.getCategory(categoryId));
        return categoryRepository.save(Category.builder().categoryId(json.read("$.id")).categoryName(json.read("$.name")).build());
    }


    public String getListingTypeName(String listingTypeId) {
        DocumentContext jsonType = JsonPath.parse(meliFeignClient.getTypeName());
        String content = jsonType.read("$.[*]").toString();

        List<ListingTypeDTO> typesList = null;
        try {
            typesList = objectMapper.readValue(content, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String typeName;

        for (ListingTypeDTO e : typesList) {
            if (e.getId().equals(listingTypeId)) {
                typeName = e.getName();
                return typeName;
            }
        }

        return null;

    }

    public ListingType getListingTypeNameFromBd(String listingTypeId) throws ResourceNotFoundException {
        return listingTypeRepository.findById(listingTypeId).orElseThrow(() -> new ResourceNotFoundException("No hay un Listing Type con ese id"));
    }

    public Integer getPositionMethod(DocumentContext positionByItemId) {
        return positionByItemId.read("$.position");
    }

    public Integer getBestSellerPosition(String itemId, String productId) {

        try {
            DocumentContext positionByItemId = JsonPath.parse(meliFeignClient.getItemPositionByItemId(itemId));
            return getPositionMethod(positionByItemId);

        } catch (FeignException.NotFound ignored) {

        }

        try {
            DocumentContext positionByProductId = JsonPath.parse(meliFeignClient.getItemPositionByProductId(productId));
            return getPositionMethod(positionByProductId);
        } catch (FeignException.NotFound ignored) {

        }

        return 0;

    }

    private String getItemSku(ItemAttributesDTO attributes) {
        List<AttributesDTO> sku = attributes.getAttributes().parallelStream().filter(att -> att.getName().equals("SKU")).toList();
        if (!sku.isEmpty()) return sku.get(0).getValue_name();
        return null;
    }



    public ItemCatalogDTO getSellerItemCatalog(String product_catalog_id, int limit, int page) {
        /*el paginado de la api de meli funciona por offset(a partir de que elemento) y no por pagina.*/
        int pageN = page * limit;
        ItemCatalogDTO resultDTO = meliFeignClient.getProductSearch(product_catalog_id, limit, pageN);
        int position = pageN + 1;

        List<CatalogItemResponseDTO> processedResults = resultDTO.getResults()
                .parallelStream()
                .map(e -> {

                    ItemAttributesDTO attributesDTO = meliFeignClient.getItemAtributtes(e.getItem_id());
                    SellerDTO sellerDTO = meliFeignClient.getSellerBySellerId(e.getSeller_id());

                    e.setCreated_date_item(attributesDTO.getDate_created());
                    e.setUpdated_date_item(attributesDTO.getLast_updated());
                    e.setSeller_nickname(sellerDTO.getSeller().getNickname());

                    e.setCatalogPosition(resultDTO.getResults().indexOf(e) + position);
                    return e;
                }).toList();

        ItemCatalogDTO responseDTO = new ItemCatalogDTO();
        responseDTO.setPaging(resultDTO.getPaging());
        responseDTO.setResults(processedResults);
        return responseDTO;
    }

    /**
     *
     * @param productId id que para indentificar el catalogo en la api de MERCADO LIBRE
     * @return DTO Con los datos del mejor pocisionado en el catalogo
     * @throws ResourceNotFoundException
     */
    public BuyBoxWinnerResponseDTO getBuyBoxWinner(String productId) throws ResourceNotFoundException, AppException {
        try {
            Long id = userEntityService.getAuthenticatedUserId();
            SellerRefactor user = sellerService.findById(id);

            BoxWinnerDTO result = meliFeignClient.getProductWinnerSearch(productId,"Bearer " + user.getTokenMl());
            SellerDTO seller = meliFeignClient.getSellerBySellerId(result.getBuy_box_winner().getSeller_id());

            BuyBoxWinnerResponseDTO responseDTO = modelMapper.map(result.getBuy_box_winner(), BuyBoxWinnerResponseDTO.class);

            responseDTO.setSeller_nickname(seller.getSeller().getNickname());
            //responseDTO.setListing_type_id(getListingTypeNameFromBd(responseDTO.getListing_type_id()).getName());
            return responseDTO;
        }catch (Exception ex){
            throw new AppException(ex.getMessage(),"meliService->getBuyWinner",000,404);
        }

    }

    public BuyBoxWinnerResponseDTO getBuyBoxWinnerCatalog(String productId) throws ResourceNotFoundException {
        Long id = userEntityService.getAuthenticatedUserId();
        SellerRefactor user = sellerService.findById(id);

        BoxWinnerDTO result = meliFeignClient.getProductWinnerSearch(productId ,"Bearer " + user.getTokenMl());

        return modelMapper.map(result.getBuy_box_winner(), BuyBoxWinnerResponseDTO.class);
    }

    public int getCatalogPosition(String itemId, String product_catalog_id) {
        if (product_catalog_id == null) {
            return -1;
        }

        try {

            ItemCatalogDTO responseDTO = meliFeignClient.getProductSearch(product_catalog_id);

            return IntStream.range(0, responseDTO.getResults().size()).parallel().filter(index -> responseDTO.getResults().get(index).getItem_id().equals(itemId)).findFirst().orElse(-1) + 1;

        } catch (FeignException.NotFound ignored) {

        }

        return -1;
    }

    public OptionsDTO getShippingCostDTO(String itemId) {

        return meliFeignClient.getShippingCostDTO(itemId);
    }

    public FeeResponseDTO getItemFee(double price, String category_id, String listing_type_id) throws AppException {
        try {
            return meliFeignClient.getItemFee(price, category_id, listing_type_id);
        }catch (Exception ex){
            throw new AppException(ex.getMessage(),"MeliService-> getItemfee",000,204);
        }

    }


    public List<GeneralCategory> findGeneralCategories() {
        ArrayList<String> categoriesNotIncluded = new ArrayList<>(Arrays.asList("MLA1540", "MLA1459", "MLA2547", "MLA1743", "MLA1430"));
        return meliFeignClient.getGeneralCategory().stream().filter(category -> !categoriesNotIncluded.contains(category.getId())).toList();
    }

    public List<AllGeneralCategory> findAllGeneralCategories(){
        return meliFeignClient.getAllGeneralCategory();
    }

    public TopSoldProductCategoryDTO getTopProductsByCategory(String id) {
        return meliFeignClient.getTopProductsByCategory(id);
    }

    public TopSoldDetailedProductDTO getTopProductDetails(String id) {
        return meliFeignClient.getTopProductDetails(id);
    }


}
