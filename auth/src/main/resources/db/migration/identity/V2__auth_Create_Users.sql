-- 用户表
CREATE TABLE IF NOT EXISTS auth_users
(
    id           VARCHAR(40) PRIMARY KEY COMMENT '用户ID',
    loginname    VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    username     VARCHAR(50) NULL COMMENT '真实姓名',
    user_role    ENUM('ADMIN', 'PERSONAL', 'ENTERPRISE', 'COLLEGE') NOT NULL DEFAULT 'ENTERPRISE' COMMENT '用户角色',
    email        VARCHAR(255) UNIQUE NULL COMMENT '电子邮箱',
    password     VARCHAR(255) NOT NULL COMMENT '加密密码',
    mobile       VARCHAR(20) UNIQUE NULL COMMENT '手机号码',
    enabled      bit NOT NULL,
    status       ENUM('DISABLED', 'ENABLED') NOT NULL DEFAULT 'ENABLED' COMMENT '用户状态',
    created_at   TIMESTAMP NULL COMMENT '创建时间',
    updated_at   TIMESTAMP NULL COMMENT '更新时间',
    create_num    INT                                        DEFAULT 5 COMMENT '创建数量',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (mobile)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='用户主表';