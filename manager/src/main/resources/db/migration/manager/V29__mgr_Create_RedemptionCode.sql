CREATE TABLE IF NOT EXISTS mgr_redemption_code
(
    id           VARCHAR(40)                        PRIMARY KEY COMMENT '兑换码Id',
    user_id      VARCHAR(40)                        NULL COMMENT '使用用户ID',
    code         VARCHAR(20)                        NULL COMMENT '兑换码',
    status       INT                                DEFAULT 0 COMMENT '0 是未使用 1为使用过',
    create_time  DATETIME                           NOT NULL COMMENT '创建时间',
    modify_time  DATETIME                           COMMENT '使用时间',
    deadline_time DATETIME                          COMMENT '截止时间'
    )  ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT ='兑换码表';