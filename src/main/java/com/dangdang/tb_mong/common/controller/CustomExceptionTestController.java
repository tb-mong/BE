package com.dangdang.tb_mong.common.controller;

import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exception-test")
@Tag(name = "Custom Exception 테스트용", description = "")
public class CustomExceptionTestController {

    @GetMapping("/bad-request")
    public ResponseEntity<Object> test400Error() {
        try{
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATED_USERID);
        }
        catch(Exception ex) {
            throw new CustomException(ex);
        }
    }

    @GetMapping("/unauthorized")
    public ResponseEntity<Object> test401Error() {
        try{
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.NOT_VALID_TOKEN);
        }
        catch(Exception ex) {
            throw new CustomException(ex);
        }
    }
}

