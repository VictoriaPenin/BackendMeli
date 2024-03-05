package com.msmeli.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msmeli.dto.ItemCatalogDTO;
import com.msmeli.dto.response.BuyBoxWinnerResponseDTO;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
//@CrossOrigin(origins = "http://201.216.243.146:10080")
//@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin(origins = "https://ml.gylgroup.com")
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    private final MeliService meliService;


    public ItemController(ItemService itemService, MeliService meliService) {
        this.itemService = itemService;
        this.meliService = meliService;
    }



    @GetMapping("/search")
    public Page<ItemResponseDTO> searchItems(
            @RequestParam(value = "searchType", defaultValue = "id") String searchType,
            @RequestParam(value = "searchInput", defaultValue = "mla") String searchInput,
            @RequestParam(value = "isCatalogue", defaultValue = "false") boolean isCatalogue,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(value = "isActive", defaultValue = "null") String isActive
    ) throws ResourceNotFoundException, AppException {
        return itemService.searchProducts(searchType, searchInput, offset, pageSize, isCatalogue, isActive);
    }

    @GetMapping("/seller/list")
    public ResponseEntity<Page<ItemResponseDTO>> sellerItemsList(@RequestParam(value = "sellerId", defaultValue = "1152777827") Integer sellerId, @RequestParam(value = "offset", defaultValue = "0") int offset, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(itemService.getItemsAndCostPaged(sellerId, offset, pageSize));
    }


    @GetMapping("/catalog/{product_catalog_id}")
    public ItemCatalogDTO getSellerItemCatalog(@PathVariable String product_catalog_id,@RequestParam(defaultValue = "5") int limit,@RequestParam(defaultValue = "0") int page) throws ParseException {
        return meliService.getSellerItemCatalog(product_catalog_id,limit,  page);
    }

    @GetMapping("/seller/catalog/{product_catalog_id}")
    public OneProductResponseDTO getOneCatalogProduct(@PathVariable String product_catalog_id) throws JsonProcessingException, ResourceNotFoundException {
        return itemService.getOneProduct(product_catalog_id);
    }

    @GetMapping("/winner/{product_catalog_id}")
    public BuyBoxWinnerResponseDTO getBuyBoxWinner(@PathVariable String product_catalog_id) throws JsonProcessingException, ResourceNotFoundException, AppException {
        return meliService.getBuyBoxWinner(product_catalog_id);
    }

    @GetMapping("/oneItem/{idItem}")
    public ResponseEntity<ItemResponseDTO> getOneItem(@PathVariable String idItem) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getOneItem(idItem));
    }

}
