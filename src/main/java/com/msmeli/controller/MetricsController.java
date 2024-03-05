package com.msmeli.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msmeli.dto.TopSoldDetailedProductDTO;
import com.msmeli.dto.feign.ItemFeignDTO;
import com.msmeli.dto.feign.MetricsDTO.*;
import com.msmeli.dto.response.TopItemResponseDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.MeliException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.AllGeneralCategory;
import com.msmeli.model.GeneralCategory;
import com.msmeli.service.services.AllGeneralCategoryService;
import com.msmeli.service.services.GeneralCategoryService;
import com.msmeli.service.services.MeLiMetricsService;
import com.msmeli.service.services.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://201.216.243.146:10080")
//@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin(origins = "https://ml.gylgroup.com")
@RequestMapping("/metrics")
public class MetricsController {
    private final GeneralCategoryService categoryService;
    private final AllGeneralCategoryService generalCategoryService;
    private final MeliFeignClient meliFeignClient;
    private final MeLiMetricsService meLiMetricsService;
    private final ItemService itemService;

    public MetricsController(GeneralCategoryService categoryService, AllGeneralCategoryService generalCategoryService, MeliFeignClient meliFeignClient, MeLiMetricsService meLiMetricsService, ItemService itemService) {
        this.categoryService = categoryService;
        this.generalCategoryService = generalCategoryService;
        this.meliFeignClient = meliFeignClient;
        this.meLiMetricsService = meLiMetricsService;
        this.itemService = itemService;
    }

        @GetMapping("/listCategories")
        public ResponseEntity<List<GeneralCategory>> createAll () throws ResourceNotFoundException {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryService.findAll());
        }

        @GetMapping("/topSold/{id}")
        public ResponseEntity<List<TopSoldDetailedProductDTO>> getTopProductsByCategory (@PathVariable String id){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryService.getTopProductsByCategory(id));
        }

        @PostMapping("save")
        public void saveAllGeneralCategory ()throws ResourceNotFoundException, AppException {
            generalCategoryService.saveAllGeneralCategory();
        }

        @GetMapping("listAllCategory")
        public List<AllGeneralCategory> getAllGeneralCategories ()throws ResourceNotFoundException, AppException {
            return generalCategoryService.findAll();
        }

        @GetMapping("/itemOpinion/{itemID}")
        public ResponseEntity<ReviewsDTO> getProductOpinions
                (@PathVariable("itemID") String itemID,
                 @RequestParam(name = "limit", required = false) Integer limit,
                 @RequestParam(name = "offset", required = false) Integer offset) throws
                JsonProcessingException, MeliException {
            return ResponseEntity.ok(meLiMetricsService.getProductOpinions(itemID,limit,offset));
        }
        @GetMapping("/itemHealth/{itemID}")
        public ResponseEntity<ItemHealthDTO> getItemHealth (@PathVariable("itemID") String itemID) throws
                JsonProcessingException, MeliException {
            return ResponseEntity.ok(meLiMetricsService.getItemHealth(itemID));
        }

        @GetMapping("/itemHealthActions/{itemID}")
        public ResponseEntity<ItemHealthActionablesDTO> getItemHealthActionables (@PathVariable("itemID") String itemID) throws
                JsonProcessingException, MeliException {
            return ResponseEntity.ok(meLiMetricsService.getItemHealthActionables(itemID));
        }

        @GetMapping("/PurchaseExperience/{itemID}")
        public ResponseEntity<PurchaseExperienceDTO> getPurchaseExperience (@PathVariable("itemID") String itemID) throws
                JsonProcessingException, MeliException {
            return ResponseEntity.ok(meLiMetricsService.getIntegrators(itemID));
        }

        @GetMapping("/ItemAttributesBySeller/{includeItemsBoolean}")
        public ResponseEntity<?> getItemAttributesDataBySeller (
                @PathVariable("includeItemsBoolean") boolean includeItems)
                throws JsonProcessingException, MeliException {
                return ResponseEntity.ok(meLiMetricsService.getItemAttributesDataBySeller(includeItems));
        }

        @GetMapping("/ItemAttributesBySellerAndDomain/{sellerID}/{domainID}/{includeItemsBoolean}")
        public ResponseEntity<QualityProbeDTO> getItemAttributesDataBySeller (
                @PathVariable("sellerID") String sellerID,
                @PathVariable("domainID") String domainID,
                @PathVariable("includeItemsBoolean") boolean includeItems)
                throws JsonProcessingException, MeliException {
                return ResponseEntity.ok(meLiMetricsService.getItemAttributesDataBySellerWithDomain(sellerID, includeItems));
        }

        @GetMapping("/ItemAttributesByItem/{itemID}/{includeItemsBoolean}")
        public ResponseEntity<?> getItemAttributesDataByItem (
                @PathVariable("itemID") String itemID,
        @PathVariable("includeItemsBoolean") boolean includeItems)
                throws JsonProcessingException, MeliException {
            return ResponseEntity.ok(meLiMetricsService.getItemAttributesDataByItem(itemID, includeItems));
        }

        @GetMapping("/ItemAttributesBySellerAndDomain/{itemID}/{domainID}/{includeItemsBoolean}")
        public ResponseEntity<?> getItemAttributesDataByItemWithDomain (
                @PathVariable("itemID") String itemID,
                @PathVariable("domainID") String domainID,
                @PathVariable("includeItemsBoolean") boolean includeItems)
                throws JsonProcessingException, MeliException {
                return ResponseEntity.ok(meLiMetricsService.getItemAttributesDataByItemWithDomain(itemID, domainID, includeItems));
        }

        @GetMapping("getTopForCategorie/{id}")
        public List<TopItemResponseDTO> getTopForCategorie (@PathVariable String id) throws ResourceNotFoundException {
            return itemService.getTopforCategorie(id);

        }
}
