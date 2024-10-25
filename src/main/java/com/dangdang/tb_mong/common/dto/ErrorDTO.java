package com.dangdang.tb_mong.common.dto;

import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorDTO {

    private int code;
    private String msg;
    private String detail;

    public static ResponseEntity<ErrorDTO> toResponseEntity(CustomException ex) {
        ErrorCode errorType = ex.getErrorCode();
        String detail = ex.getDetail();

        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorDTO.builder()
                        .code(errorType.getCode())
                        .msg(errorType.getMsg())
                        .detail(detail)
                        .build());
    }
}
