package com.dangdang.tb_mong.common.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Swagger 연동 테스트용", description = "Swagger를 이용한 API 연동 테스트용 컨트롤러")
public class SwaggerTestController {
    @GetMapping("/test")
    public String test() {
        return "Hello, Swagger!";
    }
}