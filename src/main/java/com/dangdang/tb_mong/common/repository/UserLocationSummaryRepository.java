package com.dangdang.tb_mong.common.repository;

import com.dangdang.tb_mong.common.entity.UserLocationSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocationSummaryRepository extends JpaRepository<UserLocationSummary, Long> {
}
