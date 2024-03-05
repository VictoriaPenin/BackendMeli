package com.msmeli.feignClient;

import com.msmeli.configuration.feign.FeignClientConfiguration;
import com.msmeli.dto.*;
import com.msmeli.dto.feign.ItemFeignDTO;
import com.msmeli.dto.feign.ItemIdsResponseDTO;
import com.msmeli.dto.request.TokenRequestDTO;
import com.msmeli.dto.request.description.DescriptionCatalogDTO;
import com.msmeli.dto.request.description.DescriptionProductDTO;
import com.msmeli.dto.response.FeeResponseDTO;
import com.msmeli.dto.response.OptionsDTO;
import com.msmeli.dto.response.TokenResposeDTO;
import com.msmeli.model.AllGeneralCategory;
import com.msmeli.model.GeneralCategory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.msmeli.MsMeliApplication.MELI_URL;

@FeignClient(name = "Meli", url = MELI_URL, configuration = FeignClientConfiguration.class)
public interface MeliFeignClient {

    @GetMapping("/products/{productId}/items")
    public ItemCatalogDTO getProductSearch(@PathVariable String productId);
    @GetMapping("/products/{productId}/items")
    public ItemCatalogDTO getProductSearch(@PathVariable String productId,@RequestParam int limit,@RequestParam int offset);

    @GetMapping("/products/{productId}")
    public BoxWinnerDTO getProductWinnerSearch(@PathVariable String productId, @RequestHeader("Authorization") String authorization);

    @GetMapping("/sites/MLA/search?nickname={nickname}&catalog_listing=true&offset={offset}")
    public SellerDTO getSellerByNickname(@PathVariable String nickname, @PathVariable int offset);

    @GetMapping("/categories/{categoryId}")
    public String getCategory(@PathVariable String categoryId);

    @GetMapping("/sites/MLA/search?nickname={nickname}&catalog_listing=true")
    public SellerDTO getSellerCatalogItems(@PathVariable String nickname);

    @GetMapping("/sites/MLA/search?seller_id={seller_id}")
    public SellerDTO getSellerBySellerId(@PathVariable Integer seller_id);

    @GetMapping("/items/{item_id}")
    public ItemAttributesDTO getItemAtributtes(@PathVariable String item_id);
    @GetMapping("/items/{item_id}")
    public ItemFeignDTO getItemAtributtesRe(@PathVariable String item_id,@RequestHeader("Authorization") String authorization);

//    //Puede funcionar mas a futuro
//    @GetMapping("/highlights/MLA/item/{product_id}")
//    public String getBestSellerPosition(@PathVariable String product_id);

    @GetMapping("/highlights/MLA/item/{item_id}")
    public String getItemPositionByItemId(@PathVariable String item_id);

    @GetMapping("/highlights/MLA/product/{product_id}")
    public String getItemPositionByProductId(@PathVariable String product_id);

    @GetMapping("/sites/MLA/listing_types")
    public String getTypeName();

    @GetMapping("/sites/MLA/listing_types")
    public List<ListingTypeDTO> saveListingTypes();

    @GetMapping("/products/{productId}")
    public String getBuyBoxWinner(@PathVariable String productId);

    /*@PostMapping("/oauth/token")
    public RefreshTokenDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenDTO);*/

    @GetMapping("/sites/MLA/listing_prices")
    public FeeResponseDTO getItemFee(@RequestParam("price") double price, @RequestParam("category_id") String category_id, @RequestParam("listing_type_id") String listing_type_id);

    @GetMapping("/items/{itemId}/shipping_options?zip_code=1804")
    OptionsDTO getShippingCostDTO(@PathVariable String itemId);

    @GetMapping("/products/{catalog_Product_Id}")
    DescriptionCatalogDTO getCatalogDescription(@PathVariable String catalog_Product_Id);

    @GetMapping("/items/{itemId}/description")
    DescriptionProductDTO getProductDescription(@PathVariable String itemId);

    @GetMapping("/sites/MLA/categories")
    List<GeneralCategory> getGeneralCategory();

    @GetMapping("sites/MLA/categories")
    List<AllGeneralCategory> getAllGeneralCategory();

    @GetMapping("/highlights/MLA/category/{id}")
    TopSoldProductCategoryDTO getTopProductsByCategory(@PathVariable String id);
    @GetMapping("/highlights/MLA/category/{id}")
    TopSoldProductCategoryDTO getTopProductsByCategoryv2(@PathVariable String id,@RequestHeader("Authorization") String authorization);

    @GetMapping("/products/{productId}")
    TopSoldDetailedProductDTO getTopProductDetails(@PathVariable String productId);
    @GetMapping("/products/{productId}")
    TopSoldDetailedProductDTO getTopProductDetailsv2(@PathVariable String productId,@RequestHeader("Authorization") String authorization);


    @PostMapping("/oauth/token")
    TokenResposeDTO tokenForTG(@RequestBody TokenRequestDTO tokenRequestDTO);

    @GetMapping("/users/{userId}/items/search")
    ItemIdsResponseDTO getAllIDsForSeller(
            @PathVariable("userId") int userId,
            @RequestHeader("Authorization") String authorization
    );
    @PostMapping("/oauth/token")
    TokenResposeDTO refreshToken(TokenRequestDTO tokenRequestDTO);

    @GetMapping("/users/me") String getMeliMyReputation(
            @RequestHeader("Authorization") String authorization
    );
    @GetMapping("/trends/{siteID}/{categoryID}") String getMeliTrendsBySiteAndCategoryOptional(
            @PathVariable ("siteID") String siteID,
            @PathVariable(name = "categoryID", required = false) String categoryID,
            @RequestHeader("Authorization") String authorization
    );
    @GetMapping("/trends/{itemID}") String getProductReviewInfo(
            @PathVariable ("itemID") String siteID,
            @RequestHeader("Authorization") String authorization
    );

    @GetMapping("/reviews/item/{itemID}") String getProductOpinions(
            @PathVariable ("itemID") String itemID,
            @RequestHeader("Authorization") String authorization,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "offset", required = false) Integer offset
    );

    @GetMapping("/trends/{itemID}?catalog_product_id={catalogID}") String getProductReviewInfoOptionalCatalog(
            /*
            Este método deberia funcionar para este y el de arriba. requiere testeo

            Utilizando el catalog_product_id correspondiente
            al ítem con optin a catálogo, puedes consultar sus opiniones
            */
            @PathVariable ("itemID") String siteID,
            @PathVariable(name = "catalogID", required = false) String catalogID,
            @RequestHeader("Authorization") String authorization
    );
    @GetMapping("/sites/{siteID}ID/health_levels") String getSiteHealthStandard(

            /*
            Estándares de salud para las publicaciónes, por sitio:
            https://developers.mercadolibre.com.ar/es_ar/calidad-de-publicaciones#Niveles-calidad-por-sitio
             */

            @PathVariable ("siteID") String siteID,
            @RequestHeader("Authorization") String authorization
    );
    @GetMapping("/items/{item_id}/health") String getItemHealth(

            /*
            Salud de una publicación:
            https://developers.mercadolibre.com.ar/es_ar/calidad-de-publicaciones#Detalle-de-calidad-por-item
             */

            @PathVariable ("item_id") String siteID,
            @RequestHeader("Authorization") String authorization
    );

    @GetMapping("/items/{item_id}/health/actions") String getItemActionables(

            /*
            Accionables para mejorar la calidad de un ítem:
            https://developers.mercadolibre.com.ar/es_ar/calidad-de-publicaciones#Acciones-necesarias-para-mejorar-la-calidad-de-un-item
             */

            @PathVariable ("item_id") String item_id,
            @RequestHeader("Authorization") String authorization
    );

    @GetMapping("/reputation/items/{item_id}/purchase_experience/integrators?locale=es_AR") String getPurchasingExperience(

            //https://developers.mercadolibre.com.ar/es_ar/experiencia-de-compra

            @PathVariable ("item_id") String site_id,
            @RequestHeader("Authorization") String authorization
    );


    @GetMapping(" /users/{userID}/items_visits?date_from={dateFROM}&date_to={dateTO}") String getVisitsByUserBetweenDates(
            //Formato de fecha: String YYYY-MM-DD
            @PathVariable ("userID") String userID,
            @PathVariable ("dateFROM") String dateFROM,
            @PathVariable ("dateTO") String dateTO,
            @RequestHeader("Authorization") String authorization
    );

    @GetMapping("/visits/items?ids={itemIDs}") String getVisitsByItemList(
            @RequestParam("ids") List<String> itemIDs,
            @RequestHeader("Authorization") String authorization
    );

    @GetMapping("/items/visits?ids={itemID}&date_from={dateFROM}&date_to={dateTO}") String getVisitsByItemBetweenDates(
            @PathVariable ("itemID") String siteID,
            @PathVariable ("dateFROM") String dateFROM,
            @PathVariable ("dateTO") String dateTO,
            @RequestHeader("Authorization") String authorization
    );

    // /users/$USER_ID/items_visits/time_window?last=$LAST&unit=$UNIT&ending=$ENDING
    // /items/$ITEM_ID/visits/time_window?last=$LAST&unit=$UNIT&ending=$ENDING

    @GetMapping("/users/{userID}/items_visits/time_window?last={lastAmmount}&unit={timeUnit}&ending={dateEnding}") String getVisitsByUserWindowed(
            /*
            Por ejemplo si quisiera ver los 3 dias anteriores al 24/1/10:
            lastAmmount=2,
            timeUnit="days"
            dateEnding="2024-01-10"
             */
            @PathVariable ("userID") String userID,
            @PathVariable ("lastAmmount") Integer lastAmmount,
            @PathVariable ("timeUnit") String timeUnit,
            @PathVariable ("dateEnding") String dateEnding,
            @RequestHeader("Authorization") String authorization
    );

    @GetMapping("/items/{itemID}/visits/time_window?last={lastAmmount}&unit={timeUnit}&ending={dateEnding}") String getVisitsByItemWindowed(
            /*
            Por ejemplo si quisiera ver los 3 dias anteriores al 24/1/10:
            lastAmmount=2,
            timeUnit="days"
            dateEnding="2024-01-10"
             */
            @PathVariable ("itemID") String itemID,
            @PathVariable ("lastAmmount") Integer lastAmmount,
            @PathVariable ("timeUnit") String timeUnit,
            @PathVariable ("dateEnding") String dateEnding,
            @RequestHeader("Authorization") String authorization
    );
    @GetMapping(("/catalog_quality/status"))
    String getAttributesData(
            @RequestParam(name = "seller_id", required = false) String seller_id,
            @RequestParam(name = "item_id", required = false) String item_id,
            @RequestParam(name = "domain_id", required = false) String domain_id,
            @RequestParam("include_items") boolean include_items,
            @RequestParam("v") String v,
            @RequestHeader("Authorization") String authorization
    );

    /*
    PREGUNTAS:

    Para ordenar los resultados puedes añadir los siguientes query params:
        sort_fields: permite ordenar los resultados de la búsqueda por uno o varios campos determinados.
        Acepta los campos item_id, seller_id, from_id y date_created separados por coma.

     */

    @GetMapping("questions/search?seller_id={seller_id}&api_version=4")
    String GetQuestionsBySeller(
            @RequestParam("seller_id") String seller_id,
            @RequestHeader("Authorization") String authorization
    );
    @GetMapping("questions/search?item={item_id}&api_version=4")
    String GetQuestionsByItem(
            @RequestParam("item_id") String item_id,
            @RequestHeader("Authorization") String authorization
    );
    @GetMapping("questions/{question_id}?api_version=4")
    String GetQuestionsByID(
            @RequestParam("question_id") String question_id,
            @RequestHeader("Authorization") String authorization
    );

}

