package com.dangdang.tb_mong.common.repository;

import com.dangdang.tb_mong.common.entity.Trail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrailRepository extends JpaRepository<Trail, Long> {
    List<Trail> findByUserIdAndDate(Long userId, LocalDate localDate);

    List<Trail> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<Trail> findByLocationId(Long locationId);

    List<Trail> findByLocationIdAndNameContaining(Long locationId, String name);
}
