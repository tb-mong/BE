package com.dangdang.tb_mong.record.service;

import com.dangdang.tb_mong.common.entity.Trail;
import com.dangdang.tb_mong.common.entity.User;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.TrailRepository;
import com.dangdang.tb_mong.common.repository.UserRepository;
import com.dangdang.tb_mong.common.security.PrincipalDetails;
import com.dangdang.tb_mong.record.dto.WalkStatusDto;
import com.dangdang.tb_mong.common.dto.TrailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final UserRepository userRepository;
    private final TrailRepository trailRepository;

    public List<TrailDto> getWalksByDate(PrincipalDetails userDetails, Date date) {
        // 사용자 확인
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER));

        // 사용자가 걸었던 경로들을 특정 날짜로 필터링하여 조회
        LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
        List<Trail> trails = trailRepository.findByUserIdAndDate(user.getId(), localDate);

        // trail 데이터를 trailDto로 매핑
        return trails.stream()
                .map(trail -> new TrailDto(
                        trail.getId(),
                        trail.getName(),
                        trail.getLocation().getName(),
                        trail.getKm(),
                        trail.getUser().getNickname(),
                        trail.getLikeCnt(),
                        trail.getImage()
                ))
                .collect(Collectors.toList());
    }

    public List<WalkStatusDto> getWalkStatusByMonth(PrincipalDetails userDetails, int year, int month) {
        // 사용자 확인
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER));

        // 해당 월의 첫 날과 마지막 날 계산
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // 해당 사용자와 월에 대한 산책 경로 조회
        List<Trail> trails = trailRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);

        // 결과 리스트 생성
        List<WalkStatusDto> walkStatusList = new ArrayList<>();

        // 시작 날짜부터 끝 날짜까지 반복
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            boolean walkedOnDate = false;

            // 해당 날짜에 산책 기록이 있는지 확인
            for (Trail trail : trails) {
                if (trail.getDate().toLocalDate().isEqual(currentDate)) {
                    walkedOnDate = true;
                    break;
                }
            }

            // WalkStatusDto 객체 생성하여 리스트에 추가
            walkStatusList.add(new WalkStatusDto(java.sql.Date.valueOf(currentDate), walkedOnDate));

            // 다음 날짜로 이동
            currentDate = currentDate.plusDays(1);
        }

        return walkStatusList;
    }
}