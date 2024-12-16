package com.dangdang.tb_mong.common.repository;

import com.dangdang.tb_mong.common.entity.UserLocationSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLocationSummaryRepository extends JpaRepository<UserLocationSummary, Long> {

    List<UserLocationSummary> findAllByLocationId(Long locationId);

    UserLocationSummary findByLocationId(Long id);

    Optional<UserLocationSummary> findByUserIdAndLocationId(Long userId, Long locationId);
}
