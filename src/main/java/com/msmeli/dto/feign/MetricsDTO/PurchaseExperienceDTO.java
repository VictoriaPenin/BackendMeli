package com.msmeli.dto.feign.MetricsDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class PurchaseExperienceDTO {

    private String item_id;
    private Freeze freeze;
    private Title title;
    private List<Subtitle> subtitles;
    private List<Action> actions;
    private Reputation reputation;
    private Status status;
    private MetricsDetails metrics_details;

    @Data
    public static class Freeze {
        private String text;
    }

    @Data
    public static class Title {
        private String text;
    }

    @Data
    public static class Subtitle {
        private int order;
        private String text;
    }

    @Data
    public static class Action {
        private int order;
        private String text;
    }

    @Data
    public static class Reputation {
        private String color;
        private String text;
        private int value;
    }

    @Data
    public static class Status {
        private String id;
    }

    @Data
    public static class MetricsDetails {
        private String empty_state_title;
        private List<Problem> problems;
        private Distribution distribution;
    }

    @Data
    public static class Problem {
        private int order;
        private String key;
        private String color;
        private String quantity;
        private int cancellations;
        private int claims;
        private String tag;
        private LevelTwo levelTwo;
        private LevelThree levelThree;
    }

    @Data
    public static class LevelTwo {
        private String key;
        private Title title;
    }

    @Data
    public static class LevelThree {
        private String key;
        private Title title;
        private Remedy remedy;
    }

    @Data
    public static class Remedy {
        private String text;
    }

    @Data
    public static class Distribution {
        private String from;
        private String to;
        private List<LevelOne> level_one;
    }

    @Data
    public static class LevelOne {
        private String key;
        private Title title;
        private String color;
        private double percentage;
        private List<QuantitiesLevelTwo> level_two;
    }

    @Data
    public static class QuantitiesLevelTwo {
        private String key;
        private Title title;
        private int quantity;
    }
}