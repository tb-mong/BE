package com.dangdang.tb_mong.common.repository;

import com.dangdang.tb_mong.common.entity.UserLocationSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLocationSummaryRepository extends JpaRepository<UserLocationSummary, Long> {

    List<UserLocationSummary> findAllByLocationId(Long locationId);
}
