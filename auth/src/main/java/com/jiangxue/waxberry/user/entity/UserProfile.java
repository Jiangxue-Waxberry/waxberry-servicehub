package com.jiangxue.waxberry.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth_user_profiles")
public class UserProfile implements Serializable {

    @Id
    @Column(name = "id", length = 40, nullable = false, unique = true)
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
    private String id;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "uscc", length = 20)
    private String uscc; // 统一社会信用代码

    @Column(name = "company_name", length = 100)
    private String companyName;

    @Column(name = "company_admin", length = 100)
    private String companyAdmin;

    @Column(name = "school", length = 100)
    private String school;

    @Column(name = "college", length = 100)
    private String college;

    @Column(name = "major", length = 50)
    private String major;

    @Column(name = "work_num", length = 50)
    private String workNum;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "industry", length = 50)
    private String industry;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender = Gender.UNKNOWN;

    @Column(name = "title", length = 100)
    private String title;

    // 性别枚举
    public enum Gender {
        MALE, FEMALE, UNKNOWN
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