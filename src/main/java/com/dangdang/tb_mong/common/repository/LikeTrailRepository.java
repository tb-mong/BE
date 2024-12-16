package com.dangdang.tb_mong.common.repository;

import com.dangdang.tb_mong.common.entity.LikeTrail;
import com.dangdang.tb_mong.common.entity.Trail;
import com.dangdang.tb_mong.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeTrailRepository extends JpaRepository<LikeTrail, Long> {
    boolean existsByUserAndTrail(User user, Trail trail);

    List<LikeTrail> findAllByUser(User user);
}
