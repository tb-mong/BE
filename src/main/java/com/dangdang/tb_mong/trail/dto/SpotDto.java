package com.dangdang.tb_mong.trail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Getter
@Setter
public class SpotDto {
//    @JsonProperty("La")
    private double La;
//    @JsonProperty("Lo")
    private double Lo;
}
