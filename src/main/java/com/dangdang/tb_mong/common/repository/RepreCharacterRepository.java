package com.dangdang.tb_mong.common.repository;

import com.dangdang.tb_mong.common.entity.RepreCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepreCharacterRepository extends JpaRepository<RepreCharacter, Long> {
    Optional<RepreCharacter> findByUserId(Long userId);
}
