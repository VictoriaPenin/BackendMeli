package com.msmeli.dto.feign.MetricsDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QualityProbeDTO {
    private StatusDTO status;
    private List<DomainDTO> domains;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class StatusDTO {
    private MetricsContentDTO metrics;
    private Integer total_items;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class MetricsContentDTO {
    @JsonProperty("pi")
    private MetricsDTO product_identifier;
    @JsonProperty("ft")
    private MetricsDTO ficha_tecnica;
    @JsonProperty("all")
    private MetricsDTO all_attributes;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class MetricsDTO {
    private Integer complete_items;
    private Integer complete_attributes;
    private Integer incomplete_attributes;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class DomainDTO {
    private String domain_id;
    private StatusDTO status;
    private List<ItemDTO> items;

}




