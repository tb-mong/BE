package com.dangdang.tb_mong.common.security;

import com.dangdang.tb_mong.common.entity.User;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByKakaoEmail(userEmail)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));   // 사용자가 DB 에 없으면 예외처리

        return new PrincipalDetails(user, user.getKakaoEmail());   // 사용자 정보를 UserDetails 로 반환
    }
}