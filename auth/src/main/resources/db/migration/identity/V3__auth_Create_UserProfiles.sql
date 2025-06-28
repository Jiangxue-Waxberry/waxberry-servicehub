-- 用户资料表
CREATE TABLE IF NOT EXISTS auth_user_profiles
(
    id         VARCHAR(40)                        PRIMARY KEY COMMENT '资料ID',
    user_id    VARCHAR(40)                        NOT NULL UNIQUE COMMENT '关联用户ID',
    uscc    VARCHAR(20)                        NULL COMMENT '统一社会信用代码',
    company_name    VARCHAR(100)                       NULL COMMENT '公司名称',
    company_admin    VARCHAR(100)                       NULL COMMENT '公司管理员',
    school    VARCHAR(100)                        NULL COMMENT '高校',
    college    VARCHAR(100)                       NULL COMMENT '学院',
    major      VARCHAR(50)                        NULL COMMENT '专业领域',
    work_num      VARCHAR(50)                     NULL COMMENT '工号',
    bio        TEXT                               NULL COMMENT '个人简介',
    industry   VARCHAR(50)                        NULL COMMENT '行业领域',
    avatar_url VARCHAR(255)                       NULL COMMENT '头像URL',
    created_at TIMESTAMP                          NOT NULL COMMENT '创建时间',
    updated_at TIMESTAMP                          NOT NULL COMMENT '更新时间',
    gender     ENUM ('MALE', 'FEMALE', 'UNKNOWN') NULL DEFAULT 'UNKNOWN' COMMENT '性别',
    title      VARCHAR(100)                       NULL COMMENT '头衔/称号',
    -- 索引优化
    INDEX idx_user_id (user_id)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT ='用户资料表';