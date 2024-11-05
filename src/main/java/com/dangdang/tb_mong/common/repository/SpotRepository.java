package com.dangdang.tb_mong.common.repository;

import com.dangdang.tb_mong.common.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findAllByTrailId(Long trailId);
}
