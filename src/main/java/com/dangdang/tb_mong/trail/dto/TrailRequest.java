package com.dangdang.tb_mong.trail.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class TrailRequest {
    private String name;
    private BigDecimal km;

    @Schema(type = "string", example = "HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime pace;

    @Schema(type = "string", example = "HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime time;

    private BigDecimal perHour;
    private String locationCode;

    private List<SpotDto> spotLists;
}
