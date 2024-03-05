package com.msmeli.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msmeli.dto.feign.ItemFeignDTO;
import com.msmeli.dto.feign.MetricsDTO.*;
import com.msmeli.exception.MeliException;

import java.util.List;

public interface MeLiMetricsService {
    ReviewsDTO getProductOpinions(String itemID, Integer limit, Integer offset) throws JsonProcessingException, MeliException;

    ItemHealthDTO getItemHealth(String itemID) throws JsonProcessingException, MeliException;

    ItemHealthActionablesDTO getItemHealthActionables(String itemID) throws JsonProcessingException, MeliException;

    PurchaseExperienceDTO getIntegrators(String itemID) throws JsonProcessingException, MeliException;

    String getVisitsByItemBetweenDates(String itemID, String dateFrom, String dateTo);

    String getVisitsByUserBetweenDates(String itemID, String dateFrom, String dateTo);

    String getVisitsByUserWindowed(String userID, Integer lastAmmount, String unit, String date);

    String getVisitsByItemWindowed(String itemID, Integer lastAmmount, String unit, String date);

    String getVisitsInLastTwoYearsByItemList(List<String> itemIDsList) throws JsonProcessingException, MeliException;

    QualityProbeDTO getItemAttributesDataBySellerWithDomain(String domainID, boolean includeItems) throws JsonProcessingException, MeliException;

    QualityProbeDTO getItemAttributesDataBySeller(boolean include_items) throws JsonProcessingException, MeliException;

    ItemDTO getItemAttributesDataByItemWithDomain(String itemID, String domainID, boolean includeItems) throws JsonProcessingException, MeliException;

    ItemDTO getItemAttributesDataByItem(String itemID, boolean includeItems) throws JsonProcessingException, MeliException;

    /*
    Acciones para productos

        technical_specification: verifica la calidad de los atributos y completa la ficha técnica.
        buybox: publica en catálogo.
        variations: utiliza variaciones para la publicación.
        product_identifiers: informar código universal del producto.
        picture: verifica la calidad de las imágenes.
        price: publica con precio más competitivo, y en caso de que aplique, te vamos a indicar el rango de precio que puedes utilizar.
        me2: utiliza Mercado Envíos en las publicaciones.
        free_shipping: ofrece envíos gratis.
        flex: utiliza Mercado Envíos Flex.
        immediate_payment: utiliza Mercado Pago (tag immediate_payment).
        classic: realiza una publicación con exposición al menos clásica.
        premium (installments_free): Realiza una publicación como premium.
        size_chart: informa una guía de talles.
        publish: es el objetivo relacionado a la publicación del ítem, realizado automáticamente al publicar.
        picture_fashion: verifica la calidad de las imágenes en tus publicaciones de moda.


        Acciones para Vehículos

        picture: cantidad mínima de imágenes.
        price: publicar con precio más competitivo, y en caso de que aplique, te vamos a indicar el rango de precio que puedes utilizar.
        technical_specification: completar los atributos técnicos específicos del ítem.
        video: cargar video presentando el vehículo.
        upgrade_listing: aplicar un upgrade en el tipo de la publicación.
        publish: es el objetivo relacionado a la publicación del ítem, realizado automáticamente al publicar.
     */
}
