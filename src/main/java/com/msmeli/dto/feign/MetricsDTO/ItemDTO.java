package com.msmeli.dto.feign.MetricsDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.msmeli.dto.AttributesDTO;
import lombok.Data;

import java.util.List;

@Data
public class ItemDTO {
    private String item_id;
    private String domain_id;
    private AdoptionStatusDTO adoption_status;
}

@Data
class AdoptionStatusDTO {
    @JsonProperty("pi")
    private AdoptionMetricsDTO product_identifier;
    @JsonProperty("ft")
    private AdoptionMetricsDTO ficha_tecnica;
    @JsonProperty("all")
    private AdoptionMetricsDTO all_attributes;
    private AdoptionMetricsDTO required;
}

@Data
class AdoptionMetricsDTO {
    private boolean complete;
    private List<String> attributes;
    private List<String> missing_attributes;
}
