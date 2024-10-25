package com.dangdang.tb_mong.common.repository;

import com.dangdang.tb_mong.common.entity.UserCharacter;
import com.dangdang.tb_mong.dict.dto.CharacterResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {
    List<UserCharacter> findAllByUserId(Long userId);
}
