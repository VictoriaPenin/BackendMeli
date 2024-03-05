package com.msmeli.dto.feign.MetricsDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerReputationDTO {
    private String nickname;
    private String level_id;
    private String power_seller_status;
    private String realLevel;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private String protectionEndDate;
    private Transactions transactions;
    private Metrics metrics;

    //En cuanto a períodos, se calcula siempre los últimos 60 días,
    //a menos que el vendedor tenga menos de 50 ventas, en cuyo caso se calculan los últimos 365.

@Getter
@Setter
    public static class Transactions {

        private int canceled;
        private int completed;
        private String period;
        private Ratings ratings;
        private int total;
    }

    @Getter
    @Setter
    public static class Ratings {
    //Expresado en decimales, debe ser interpretado como porcentaje cuyo 100% es 1
        private double negative;
        private double neutral;
        private double positive;
    }
    //Fórmula para calculo de Reclamos: claims_rate = ventas con reclamos / ventas totales
    //Fórmula para cálculo de cancelaciones: Cancellations rate = cancelaciones que hace el vendedor / ventas totales
    //Fórmula para el cálculo de Retraso:  delayed handling time rate = ventas con despacho tardío / ventas despachadas con me2
    @Getter
    @Setter
    public static class Metrics {

        private Sales sales;
        private Claims claims;
        private DelayedHandlingTime delayed_handling_time;
        private Cancellations cancellations;
    }

    @Getter
    @Setter
    public static class Sales {
        private String period;
        private int completed;
    }

    @Getter
    @Setter
    public static class Claims {

        private String period;
        private double rate;
        private int value;
        private Excluded excluded;
    }

    @Getter
    @Setter
    public static class DelayedHandlingTime {

        private String period;
        private double rate;
        private int value;
        private Excluded excluded;
    }

    @Getter
    @Setter
    public static class Cancellations {

        private String period;
        private double rate;
        private int value;
        private Excluded excluded;
    }

    @Getter
    @Setter
    public static class Excluded {

        @JsonProperty("real_value")
        private int realValue;

        @JsonProperty("real_rate")
        private double realRate;
    }
}