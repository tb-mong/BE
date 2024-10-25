package com.dangdang.tb_mong.common.exception;

import com.dangdang.tb_mong.common.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorDTO> handleCustomException(CustomException ex) {
        return ErrorDTO.toResponseEntity(ex);
    }
}

