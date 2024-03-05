package com.msmeli.dto.feign.MetricsDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionsDTO {

    private int total;
    private int limit;
    private List<Question> questions;
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Question {
        private long id;
        @JsonProperty("seller_id")
        private long sellerId;
        @JsonProperty("buyer_id")
        private String buyerID;
        @JsonProperty("item_id")
        private String itemId;
        @JsonProperty("deleted_from_listing")
        private boolean deletedFromListing;
        @JsonProperty("suspected_spam")
        private boolean suspectedSpam;
        private String status;
        private boolean hold;
        private String text;
        @JsonProperty("date_created")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private LocalDateTime dateCreated;
        @JsonProperty("last_updated")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private LocalDateTime lastUpdated;
        private Answer answer;
        private From from;
    }
    @Data
    public static class Answer {
        private String text;
        private String status;
        @JsonProperty("date_created")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private LocalDateTime dateCreated;
    }
    @Data
    public static class From {
        private long id;
    }
}