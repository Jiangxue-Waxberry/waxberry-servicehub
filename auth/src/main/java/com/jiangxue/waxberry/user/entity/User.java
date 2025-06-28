package com.jiangxue.waxberry.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth_users")
public class User implements Serializable {

    @Id
    @Column(name = "id", length = 40, nullable = false, unique = true)
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
    private String id;

    @Column(name = "loginname", length = 50, nullable = false, unique = true)
    private String loginname;

    @Column(name = "username", length = 50)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", length = 20, nullable = false)
    private UserRole userRole = UserRole.ENTERPRISE;

    @Column(name = "email", length = 255, unique = true)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "mobile", length = 20, unique = true)
    private String mobile;

    @Column(name = "enabled", columnDefinition = "TINYINT(1) DEFAULT 1", nullable = false)
    private Boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false, columnDefinition = "ENUM('DISABLED', 'ENABLED') DEFAULT 'ENABLED'")
    private UserStatus status = UserStatus.ENABLED;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "create_num")
    private int createNum = 5;

    // 状态枚举
    public enum UserStatus {
        DISABLED, ENABLED
    }

    // 角色枚举（与表结构一致）
    public enum UserRole {
        ADMIN, PERSONAL, ENTERPRISE, COLLEGE
    }

    public boolean isEnabled() {
        return enabled;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
        this.updatedAt = Timestamp.valueOf(LocalDateTime.now());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Timestamp.valueOf(LocalDateTime.now());
    }
}