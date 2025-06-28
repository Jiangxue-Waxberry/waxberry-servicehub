package com.jiangxue.framework.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Optional;

/**
 * Security工具类，通过SecurityContextHolder获取当前用户信息
 * 所有用户信息都从JWT claims中获取
 */
public class SecurityUtils {

    /**
     * 获取当前认证信息
     */
    public static Optional<Authentication> getCurrentAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * 获取当前用户ID
     * 从JWT claims中按优先级获取：userid -> sub
     */
    public static Optional<String> getCurrentUserId() {
        return getCurrentAuthentication()
            .filter(auth -> auth instanceof JwtAuthenticationToken)
            .map(auth -> {
                JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
                Jwt jwt = jwtAuth.getToken();
                
                // 优先从userid claim获取
                String userId = jwt.getClaimAsString("userId");
                if (userId != null && !userId.isEmpty()) {
                    return userId;
                }
                
                return null;
            });
    }

    public static Optional<String> getCurrentUserLoginName() {
        return getCurrentAuthentication()
                .filter(auth -> auth instanceof JwtAuthenticationToken)
                .map(auth -> {
                    JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
                    Jwt jwt = jwtAuth.getToken();

                    // mobile claim获取
                    String loginName = jwt.getClaimAsString("loginName");
                    if (loginName != null && !loginName.isEmpty()) {
                        return loginName;
                    }

                    return null;
                });
    }

    /**
     * 获取当前用户名
     * 从JWT claims中按优先级获取：username -> preferred_username -> sub
     */
    public static Optional<String> getCurrentUserName() {
        return getCurrentAuthentication()
            .filter(auth -> auth instanceof JwtAuthenticationToken)
            .map(auth -> {
                JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
                Jwt jwt = jwtAuth.getToken();
                
                String sub = jwt.getClaimAsString("sub");
                if (sub != null && !sub.isEmpty()) {
                    return sub;
                }
                
                return null;
            });
    }

    /**
     * 获取当前用户邮箱
     * 从JWT claims中获取：email
     */
    public static Optional<String> getCurrentUserEmail() {
        return getJwtClaim("email");
    }

    /**
     * 获取当前用户手机号
     * 从JWT claims中获取：mobile
     */
    public static Optional<String> getCurrentUserMobile() {
        return getCurrentAuthentication()
            .filter(auth -> auth instanceof JwtAuthenticationToken)
            .map(auth -> {
                JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
                Jwt jwt = jwtAuth.getToken();
                
                // mobile claim获取
                String mobile = jwt.getClaimAsString("mobile");
                if (mobile != null && !mobile.isEmpty()) {
                    return mobile;
                }
                
                return null;
            });
    }

    /**
     * 获取当前用户的权限范围(scope)
     * 从JWT claims中获取：scope
     */
    public static Optional<String> getCurrentScope() {
        return getJwtClaim("scope");
    }

    /**
     * 获取当前用户的权限范围列表
     * 从JWT claims中获取：scope（支持字符串和数组格式）
     */
    @SuppressWarnings("unchecked")
    public static Optional<Collection<String>> getCurrentScopes() {
        return getCurrentAuthentication()
            .filter(auth -> auth instanceof JwtAuthenticationToken)
            .map(auth -> {
                JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
                Jwt jwt = jwtAuth.getToken();
                
                // 尝试获取scope claim（可能是字符串或数组）
                Object scopeClaim = jwt.getClaim("scope");
                if (scopeClaim instanceof Collection) {
                    return (Collection<String>) scopeClaim;
                } else if (scopeClaim instanceof String) {
                    // 如果是字符串，按空格分割
                    return java.util.Arrays.asList(((String) scopeClaim).split("\\s+"));
                }
                
                return null;
            });
    }

    /**
     * 检查当前用户是否具有指定权限
     */
    public static boolean hasScope(String scope) {
        return getCurrentScopes()
            .map(scopes -> scopes.contains(scope))
            .orElse(false);
    }

    /**
     * 获取JWT中的指定claim（字符串类型）
     */
    public static Optional<String> getJwtClaim(String claimName) {
        return getCurrentAuthentication()
            .filter(auth -> auth instanceof JwtAuthenticationToken)
            .map(auth -> {
                JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
                Jwt jwt = jwtAuth.getToken();
                return jwt.getClaimAsString(claimName);
            })
            .filter(claim -> claim != null && !claim.isEmpty());
    }

    /**
     * 获取JWT中的指定claim（支持任意类型）
     */
    public static <T> Optional<T> getJwtClaim(String claimName, Class<T> clazz) {
        return getCurrentAuthentication()
            .filter(auth -> auth instanceof JwtAuthenticationToken)
            .map(auth -> {
                JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
                Jwt jwt = jwtAuth.getToken();
                return jwt.getClaim(claimName);
            })
            .filter(clazz::isInstance)
            .map(clazz::cast);
    }

    /**
     * 获取JWT中的claim（原始Object类型）
     */
    public static Optional<Object> getJwtClaimRaw(String claimName) {
        return getCurrentAuthentication()
            .filter(auth -> auth instanceof JwtAuthenticationToken)
            .map(auth -> {
                JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
                Jwt jwt = jwtAuth.getToken();
                return jwt.getClaim(claimName);
            });
    }

    /**
     * 获取当前JWT token
     */
    public static Optional<Jwt> getCurrentJwtToken() {
        return getCurrentAuthentication()
            .filter(auth -> auth instanceof JwtAuthenticationToken)
            .map(auth -> ((JwtAuthenticationToken) auth).getToken());
    }

    /**
     * 获取当前JWT token字符串
     */
    public static Optional<String> getCurrentJwtTokenString() {
        return getCurrentJwtToken()
            .map(Jwt::getTokenValue);
    }

    /**
     * 获取当前认证主体
     */
    public static Optional<Object> getCurrentPrincipal() {
        return getCurrentAuthentication()
            .map(Authentication::getPrincipal);
    }

    /**
     * 检查当前用户是否已认证
     */
    public static boolean isAuthenticated() {
        return getCurrentAuthentication()
            .map(Authentication::isAuthenticated)
            .orElse(false);
    }

    /**
     * 检查当前是否为JWT认证
     */
    public static boolean isJwtAuthentication() {
        return getCurrentAuthentication()
            .map(auth -> auth instanceof JwtAuthenticationToken)
            .orElse(false);
    }

    /**
     * 获取当前用户ID，如果未认证则抛出异常
     */
    public static String requireCurrentUserId() {
        return getCurrentUserId()
            .orElseThrow(() -> new IllegalStateException("用户未认证或用户ID为空"));
    }

    /**
     * 获取当前用户名，如果未认证则抛出异常
     */
    public static String requireCurrentUserName() {
        return getCurrentUserName()
            .orElseThrow(() -> new IllegalStateException("用户未认证或用户名为空"));
    }

    /**
     * 清除当前安全上下文
     */
    public static void clearContext() {
        SecurityContextHolder.clearContext();
    }

    /**
     * 获取认证类型
     */
    public static String getAuthenticationType() {
        return getCurrentAuthentication()
            .map(auth -> auth.getClass().getSimpleName())
            .orElse("NONE");
    }

    /**
     * 获取当前用户的完整信息
     * 所有信息都从JWT claims中获取
     */
    public static Optional<UserInfo> getCurrentUserInfo() {
        return getCurrentAuthentication()
            .filter(auth -> auth instanceof JwtAuthenticationToken)
            .map(auth -> {
                JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
                Jwt jwt = jwtAuth.getToken();
                
                return UserInfo.builder()
                    .userId(jwt.getClaimAsString("userid"))
                    .userName(jwt.getClaimAsString("sub"))
                  
                    .build();
            });
    }

    /**
     * 通用用户信息类
     */
    public static class UserInfo {
        private String userId;
        private String userName;
        private String email;
        private String mobile;
        private String userRole;

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String userId;
            private String userName;
            private String email;
            private String mobile;
            private String userRole;

            public Builder userId(String userId) {
                this.userId = userId;
                return this;
            }

            public Builder userName(String userName) {
                this.userName = userName;
                return this;
            }

            public Builder email(String email) {
                this.email = email;
                return this;
            }

            public Builder mobile(String mobile) {
                this.mobile = mobile;
                return this;
            }

            public Builder userRole(String userRole) {
                this.userRole = userRole;
                return this;
            }

            public UserInfo build() {
                UserInfo userInfo = new UserInfo();
                userInfo.userId = this.userId;
                userInfo.userName = this.userName;
                userInfo.email = this.email;
                userInfo.mobile = this.mobile;
                userInfo.userRole = this.userRole;
                return userInfo;
            }
        }

        // Getters
        public String getUserId() { return userId; }
        public String getUserName() { return userName; }
        public String getEmail() { return email; }
        public String getMobile() { return mobile; }
        public String getUserRole() { return userRole; }
    }
}
