package com.dangdang.tb_mong.common.repository;

import com.dangdang.tb_mong.common.entity.User;
import com.dangdang.tb_mong.common.entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoUuid(String kakaoUuid);

    Optional<User> findByKakaoEmail(String userEmail);

    List<User> findByLocationId(Long locationId);
}
