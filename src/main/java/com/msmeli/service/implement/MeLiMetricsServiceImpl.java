package com.msmeli.service.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msmeli.dto.feign.ItemFeignDTO;
import com.msmeli.dto.feign.MetricsDTO.*;
import com.msmeli.exception.MeliException;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.repository.SellerRefactorRepository;
import com.msmeli.service.services.MeLiMetricsService;
import com.msmeli.service.services.UserEntityService;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.msmeli.exception.MeliException.NewMeliException;

@Service
public class MeLiMetricsServiceImpl implements MeLiMetricsService {
    private final MeliFeignClient meliFeignClient;
    private final UserEntityService userEntityService;
    private final SellerRefactorRepository sellerRefactorRepository;

    public MeLiMetricsServiceImpl(MeliFeignClient meliFeignClient, UserEntityService userEntityService, SellerRefactorRepository sellerRefactorRepository) {
        this.meliFeignClient = meliFeignClient;
        this.userEntityService = userEntityService;
        this.sellerRefactorRepository = sellerRefactorRepository;
    }

    /**
     * Devuelve las opiniones y calificaciones de un producto
     * @param itemID
     * @return
     * @throws JsonProcessingException
     * @throws MeliException
     */
    @Override
    public ReviewsDTO getProductOpinions(String itemID, Integer limit, Integer offset) throws JsonProcessingException, MeliException {
        try{
        String meliToken = "Bearer "+ sellerRefactorRepository.getMeliToken(userEntityService.getAuthenticatedUserId());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(meliFeignClient.getProductOpinions(itemID,meliToken,limit,offset));
        ReviewsDTO itemReviews = objectMapper.treeToValue(rootNode, ReviewsDTO.class);
        return itemReviews;
        }catch(FeignException f){
            throw NewMeliException(f.getMessage(),f.contentUTF8(),"300 Error MELI", f.status(), "getProductOpinions", null);
        }
        }

    /**
     * Devuelve el estado de salud de un objeto y todos sus atributos
     * @param itemID
     * @return
     * @throws JsonProcessingException
     * @throws MeliException
     */
    @Override
    public ItemHealthDTO getItemHealth(String itemID) throws JsonProcessingException, MeliException {
        try{
        String meliToken = "Bearer "+ sellerRefactorRepository.getMeliToken(userEntityService.getAuthenticatedUserId());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(meliFeignClient.getItemHealth(itemID, meliToken));
        ItemHealthDTO itemHealth = objectMapper.treeToValue(rootNode, ItemHealthDTO.class);
        return itemHealth;
        }catch(FeignException f){
            throw NewMeliException(f.getMessage(),f.contentUTF8(),"300 Error MELI", f.status(), "getItemHealth", null);
        }
        }

    /**
     * Este método devuelve un objeto que ilustra que clase de acciones se pueden tomar para mejorar la salud de ese objeto
     * @param itemID
     * @return
     * @throws JsonProcessingException
     * @throws MeliException
     */
    @Override
    public ItemHealthActionablesDTO getItemHealthActionables(String itemID) throws JsonProcessingException, MeliException {
        try{
        String meliToken = "Bearer "+ sellerRefactorRepository.getMeliToken(userEntityService.getAuthenticatedUserId());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(meliFeignClient.getItemActionables(itemID, meliToken));
        ItemHealthActionablesDTO itemHealthActionable = objectMapper.treeToValue(rootNode, ItemHealthActionablesDTO.class);
        itemHealthActionable.buildMessagePost();
        return itemHealthActionable;
        }catch(FeignException f){
            throw NewMeliException(f.getMessage(),f.contentUTF8(),"300 Error MELI", f.status(), "getItemHealthActionables", null);
        }
        }

    /**
     * Este método sirve para devolver un Objeto con la información
     * sobre la experiencia de compra (si hubo problemas, devoluciones, cancelaciones, etc)
     * @param itemID
     * @return
     * @throws JsonProcessingException
     * @throws FeignException
     * @throws MeliException
     */
    @Override
    public PurchaseExperienceDTO getIntegrators(String itemID) throws JsonProcessingException, FeignException, MeliException {
        try {
            String meliToken = "Bearer " + sellerRefactorRepository.getMeliToken(userEntityService.getAuthenticatedUserId());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(meliFeignClient.getPurchasingExperience(itemID, meliToken));
            return objectMapper.treeToValue(rootNode, PurchaseExperienceDTO.class);
        }catch(FeignException f){
            throw NewMeliException(f.getMessage(),f.contentUTF8(),"300 Error MELI", f.status(), "getIntegrators", null);
        }
    }
    @Override
    public String getVisitsByItemBetweenDates(String itemID, String dateFrom, String dateTo){
        String meliToken = "Bearer "+ sellerRefactorRepository.getMeliToken(userEntityService.getAuthenticatedUserId());
        return meliFeignClient.getVisitsByItemBetweenDates(itemID,dateFrom, dateTo, meliToken);
    }

    @Override
    public String getVisitsByUserBetweenDates(String itemID, String dateFrom, String dateTo){
        String meliToken = "Bearer "+ sellerRefactorRepository.getMeliToken(userEntityService.getAuthenticatedUserId());
        return meliFeignClient.getVisitsByUserBetweenDates(itemID,dateFrom, dateTo, meliToken);
    }

    @Override
    public String getVisitsByUserWindowed(String userID, Integer lastAmmount, String unit, String date){
        String meliToken = "Bearer "+ sellerRefactorRepository.getMeliToken(userEntityService.getAuthenticatedUserId());
        return meliFeignClient.getVisitsByUserWindowed(userID, lastAmmount, unit, date, meliToken);
    }

    @Override
    public String getVisitsByItemWindowed(String itemID, Integer lastAmmount, String unit, String date){
        String meliToken = "Bearer "+ sellerRefactorRepository.getMeliToken(userEntityService.getAuthenticatedUserId());
        return meliFeignClient.getVisitsByItemWindowed(itemID, lastAmmount, unit, date, meliToken);
    }
    @Override
    public String getVisitsInLastTwoYearsByItemList(List<String> itemIDsList) throws JsonProcessingException, MeliException {
        try{
        String meliToken = "Bearer "+ sellerRefactorRepository.getMeliToken(userEntityService.getAuthenticatedUserId());
        return meliFeignClient.getVisitsByItemList(itemIDsList, meliToken);
        }catch(FeignException f){
            throw NewMeliException(f.getMessage(),f.contentUTF8(),"300 Error MELI", f.status(), "getVisitsInLastTwoYearsByItemList", null);
        }
        }

    /**
     * Consigue los atribues de un vendedor, en cierto dominio
     * @param domain_id
     * @param include_items
     * @return
     * @throws JsonProcessingException
     * @throws MeliException
     */
    @Override
    public QualityProbeDTO getItemAttributesDataBySellerWithDomain(String domain_id, boolean include_items) throws JsonProcessingException, MeliException {
        try{
            String MeliSellerID = sellerRefactorRepository.GetMeliUserID(userEntityService.getAuthenticatedUserId()).toString();
        String meliToken = "Bearer "+ sellerRefactorRepository.getMeliToken(userEntityService.getAuthenticatedUserId());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(meliFeignClient.getAttributesData( MeliSellerID, null, domain_id, include_items,"3",meliToken));
        return objectMapper.treeToValue(rootNode, QualityProbeDTO.class);
        }catch(FeignException f){
            throw NewMeliException(f.getMessage(),f.contentUTF8(),"300 Error MELI", f.status(), "getItemAttributesDataBySellerWithDomain", null);
        }
        }

    /**
     * Consigue los atributos de un vendedor
     * @param include_items
     * @return
     * @throws JsonProcessingException
     * @throws MeliException
     */
    @Override
    public QualityProbeDTO getItemAttributesDataBySeller(boolean include_items) throws JsonProcessingException, MeliException {
        try {
            String MeliSellerID = sellerRefactorRepository.GetMeliUserID(userEntityService.getAuthenticatedUserId()).toString();
            String meliToken = "Bearer " + sellerRefactorRepository.getMeliToken(userEntityService.getAuthenticatedUserId());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(meliFeignClient.getAttributesData(MeliSellerID, null, null, include_items, "3", meliToken));
            return objectMapper.treeToValue(rootNode, QualityProbeDTO.class);
        }catch(FeignException f){
            throw NewMeliException(f.getMessage(),f.contentUTF8(),"300 Error MELI", f.status(), "getItemAttributesDataBySeller", null);
        }
        }

    /**
     * Consigue los atributos de objetos por dominio
     * @param item_id
     * @param domainID
     * @param include_items
     * @return
     * @throws JsonProcessingException
     * @throws MeliException
     */
    @Override
    public ItemDTO getItemAttributesDataByItemWithDomain(String item_id, String domainID, boolean include_items) throws JsonProcessingException, MeliException {
        try{
        String meliToken = "Bearer "+ sellerRefactorRepository.getMeliToken(userEntityService.getAuthenticatedUserId());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(meliFeignClient.getAttributesData( null, item_id, domainID, include_items, "3",meliToken));
        return objectMapper.treeToValue(rootNode, ItemDTO.class);
        }catch(FeignException f){
            throw NewMeliException(f.getMessage(),f.contentUTF8(),"300 Error MELI", f.status(), "getItemAttributesDataByItemWithDomain", null);
        }
        }

    /**
     * Consigue los atributos de un objeto.
     * @param item_id
     * @param include_items
     * @return
     * @throws JsonProcessingException
     * @throws MeliException
     */
    @Override
    public ItemDTO getItemAttributesDataByItem(String item_id, boolean include_items) throws JsonProcessingException, MeliException {
        try{
        String meliToken = "Bearer "+ sellerRefactorRepository.getMeliToken(userEntityService.getAuthenticatedUserId());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(meliFeignClient.getAttributesData( null, item_id, null, include_items, "3",meliToken));
            System.out.println(rootNode);
        ItemDTO catalogQuality = objectMapper.treeToValue(rootNode, ItemDTO.class);
        return catalogQuality;
        }catch(FeignException f){
            throw NewMeliException(f.getMessage(),f.contentUTF8(),"300 Error MELI", f.status(), "getItemAttributesDataByItem", null);
        }
        }
}
