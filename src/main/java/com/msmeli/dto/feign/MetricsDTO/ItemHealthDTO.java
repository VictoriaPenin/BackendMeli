package com.msmeli.dto.feign.MetricsDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemHealthDTO {
    @JsonProperty("item_id")
    private String itemId;
    private Double health;
    private String level;
    private List<Goal> goals;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Goal {
        private Integer progress;
        private Integer progress_max;
        private String id;
        private String name;
        private boolean apply;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private Date completed;
        private GoalData data;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoalData {
        @JsonProperty("listing_type")
        private String listingType;
    }
}