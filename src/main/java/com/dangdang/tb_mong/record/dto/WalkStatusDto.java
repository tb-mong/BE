package com.dangdang.tb_mong.record.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class WalkStatusDto {
    private Date date;
    private boolean YN;
}
