package com.msmeli.dto.feign.MetricsDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemHealthActionablesDTO {
    @JsonProperty("item_id")
    private String itemId;
    private double health;
    private List<Action> actions;
    private ArrayList<String> curatedMessage;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Action {
        private String id;
        private String name;
    }

    private String compileMessage(String jsonMessage){
        String result = switch (jsonMessage) {
            case "technical_specification" -> "verifica la calidad de los atributos y completa la ficha técnica.";
            case "buybox" -> "publica en catálogo.";
            case "variations" -> "utiliza variaciones para la publicación.";
            case "product_identifiers" -> "informar código universal del producto.";
            case "picture" -> "verifica la calidad de las imágenes.";
            case "price" -> "publica con precio más competitivo, y en caso de que aplique, te vamos a indicar el rango de precio que puedes utilizar.";
            case "me2" -> "utiliza Mercado Envíos en las publicaciones.";
            case "free_shipping" -> "ofrece envíos gratis.";
            case "flex" -> "utiliza Mercado Envíos Flex.";
            case "immediate_payment" -> "utiliza Mercado Pago (tag immediate_payment).";
            case "classic" -> "realiza una publicación con exposición al menos clásica.";
            case "premium" -> "Realiza una publicación como premium.";
            case "size_chart" -> "informa una guía de talles.";
            case "publish" -> "es el objetivo relacionado a la publicación del ítem, realizado automáticamente al publicar.";
            case "picture_fashion" -> "verifica la calidad de las imágenes en tus publicaciones de moda.";
            default -> "ID de acción no reconocido.";
        };
        return result;
    }
    public void buildMessagePost(){
        ArrayList<String> result = new ArrayList<>();
        Integer index=0;
        List<Action> actions = this.getActions();
        for (Action action:actions) {
            result.add(compileMessage(action.getId()));
            index++;
        }
        this.setCuratedMessage(result);
    }
}