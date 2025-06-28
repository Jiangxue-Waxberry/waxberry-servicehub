-- 用户审核
CREATE TABLE IF NOT EXISTS auth_user_process
(
    id                VARCHAR(40) PRIMARY KEY COMMENT 'ID',
    mobile            VARCHAR(20) NULL COMMENT '手机号码',
    admin_user        VARCHAR(40) COMMENT '审核员',
    approval_number   varchar(50) COMMENT '审批编号',
    approval_language varchar(500)                               default '' COMMENT '审批语',
    status            ENUM ('PROCESS', 'PASS','REFUSE') NOT NULL DEFAULT 'PROCESS' COMMENT '用户状态',
    created_at        TIMESTAMP                         NULL COMMENT '创建时间',
    updated_at        TIMESTAMP                         NULL COMMENT '更新时间',
    INDEX idx_mobile (mobile),
    INDEX idx_admin_user (admin_user)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci COMMENT ='用户审核表';