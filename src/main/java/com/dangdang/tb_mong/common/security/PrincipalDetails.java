package com.dangdang.tb_mong.common.security;

import com.dangdang.tb_mong.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private final User user;
    private final String kakaoEmail;

    // 인증이 완료된 사용자 추가하기
    public PrincipalDetails(User user, String kakaoEmail) {
        this.user = user;
        this.kakaoEmail = kakaoEmail;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
//        Collection<GrantedAuthority> authorities = new ArrayList<>();   // 사용자 권한을 GrantedAuthority 로 추상화
//        authorities.add(simpleGrantedAuthority);

        Collection<GrantedAuthority> collectors = new ArrayList<>();

        collectors.add(()->{return "ROLE_" + user.getRole().name();});

        return collectors;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.kakaoEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
