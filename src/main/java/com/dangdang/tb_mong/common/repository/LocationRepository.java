package com.dangdang.tb_mong.common.repository;

import com.dangdang.tb_mong.common.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByCode(String code);
}
