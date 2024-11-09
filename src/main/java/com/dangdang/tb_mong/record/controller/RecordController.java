package com.dangdang.tb_mong.record.controller;

import com.dangdang.tb_mong.common.security.PrincipalDetails;
import com.dangdang.tb_mong.record.dto.WalkStatusDto;
import com.dangdang.tb_mong.common.dto.TrailDto;
import com.dangdang.tb_mong.record.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
@Tag(name = "기록 관련 기능", description = "사용자의 산책 기록을 관리하는 API")
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/date")
    @Operation(summary = "특정 날짜의 산책 기록 조회")
    public List<TrailDto> getWalksByDate(@AuthenticationPrincipal PrincipalDetails userDetails,
                                         @RequestParam Date date){
        return recordService.getWalksByDate(userDetails, date);
    }

    @GetMapping("/month")
    @Operation(summary = "특정 월의 산책 기록 상태 조회")
    public List<WalkStatusDto> getWalkStatusByMonth(@AuthenticationPrincipal PrincipalDetails userDetails,
                                                    @RequestParam int year,
                                                    @RequestParam int month){
        return recordService.getWalkStatusByMonth(userDetails, year, month);
    }
}
