package com.jiangxue.waxberry.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth_user_process")
public class UserProcess {

    @Id
    @Column(name = "id", length = 40, nullable = false, unique = true)
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
    private String id;

    @Column(name = "mobile", length = 20, unique = true, nullable = false)
    private String mobile;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false, columnDefinition = "ENUM('PROCESS', 'PASS', 'REFUSE') DEFAULT 'PROCESS'")
    private UserProcess.ProcessStatus status = UserProcess.ProcessStatus.PROCESS;

    @Column(name = "admin_user", length = 50)
    private String adminUser;

    @Column(name = "approval_number", length = 50)
    private String approvalNumber;

    @Column(name = "approval_language", length = 50)
    private String approvalLanguage;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // 状态枚举
    public enum ProcessStatus {
        PROCESS,PASS, REFUSE
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
