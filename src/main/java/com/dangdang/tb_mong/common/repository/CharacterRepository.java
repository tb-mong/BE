package com.dangdang.tb_mong.common.repository;

import com.dangdang.tb_mong.common.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {
}
