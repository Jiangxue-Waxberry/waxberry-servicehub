CREATE TABLE mgr_agent_run (
  id                   VARCHAR(100) NOT NULL COMMENT '主键',
  user_id              VARCHAR(100) DEFAULT '' COMMENT '用户ID',
  agent_id             VARCHAR(100) DEFAULT '' COMMENT 'agentId',
  run_count            INT DEFAULT 0 COMMENT '运行次数',
  classification_id    VARCHAR(100) DEFAULT '' COMMENT 'agent的文件夹ID',
  create_time          DATETIME COMMENT '第一次运行时间',
  update_time          DATETIME COMMENT '最后一次运行时间',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;