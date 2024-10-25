package com.dangdang.tb_mong.common.repository;

import com.dangdang.tb_mong.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
