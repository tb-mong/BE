package com.dangdang.tb_mong.common.repository;

import com.dangdang.tb_mong.common.entity.LikeTrail;
import com.dangdang.tb_mong.common.entity.Trail;
import com.dangdang.tb_mong.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeTrailRepository extends JpaRepository<LikeTrail, Long> {
    boolean existsByUserAndTrail(User user, Trail trail);
}
