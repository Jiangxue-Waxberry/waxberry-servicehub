CREATE TABLE IF NOT EXISTS mgr_agent_approval
(
    id         VARCHAR(40)                       PRIMARY KEY COMMENT 'ID',
    agent_id    VARCHAR(40)                       NULL COMMENT 'agentId',
    agent_name    VARCHAR(40)                       NULL COMMENT 'agentname',
    mobile    VARCHAR(40)                       NULL COMMENT '提交人电话',
    approval_code  VARCHAR(40)                   NULL COMMENT '审批编号',
    creator      VARCHAR(40)                       NULL COMMENT '提交人ID',
    approval_language varchar(500)               DEFAULT '' COMMENT '审批语',
    status     ENUM ('PROCESS', 'PASS','REFUSE') NOT NULL DEFAULT 'PROCESS' COMMENT '审批状态',
    create_time TIMESTAMP                         NULL COMMENT '创建时间',
    update_time TIMESTAMP                         NULL COMMENT '更新时间（通过/不通过时间）'
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;