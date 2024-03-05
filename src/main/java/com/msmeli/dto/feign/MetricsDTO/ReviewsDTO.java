package com.msmeli.dto.feign.MetricsDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ReviewsDTO {
    private Paging paging;
    private List<Review> reviews;
    private HelpfulReviews helpful_reviews;
    private List<String> attributes;
    private List<QTAttributes> quanti_attributes;
    private List<String> quali_attributes;
    private Double rating_average;
    private RatingLevels rating_levels;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Paging {
        private Integer total;
        private Integer limit;
        private Integer offset;
        private Integer kvs_total;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Review {
        private Integer id;
        private ReviewableObject reviewable_object;
        private String date_created;
        private String status;
        private String title;
        private String content;
        private Integer rate;
        private Integer valorization;
        private Integer likes;
        private Integer dislikes;
        private Integer reviewer_id;
        private String buying_date;
        private Integer relevance;
        private Integer forbidden_words;
        private List<Media> media;
        private String attributes;
        private Object reactions;
        private Object attributes_variation;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewableObject {
        private String id;
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HelpfulReviews {
        private Integer best_max_stars;
        private Integer best_min_stars;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingLevels {
        private Integer one_star;
        private Integer two_star;
        private Integer three_star;
        private Integer four_star;
        private Integer five_star;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Media {
        private String id;
        private String status;
        private String type;
        private String alt;
        private List<MediaVariation> variations;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class MediaVariation {
            private String size;
            private String url;
        }
    }
    public static class QTAttributes{
        private String name;
        private String display_text;
        private Double average_rating;
    }
}