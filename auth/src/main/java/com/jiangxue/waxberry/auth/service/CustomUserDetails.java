package com.jiangxue.waxberry.auth.service;

import com.jiangxue.waxberry.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 直接获取用户角色（假设只有一个角色）
        String role = user.getUserRole().name();

        // 确保角色不为空，添加 ROLE_ 前缀
        if (role == null || role.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // 构建单个权限对象
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role.trim())
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public User getUser() {
        return user;
    }
}