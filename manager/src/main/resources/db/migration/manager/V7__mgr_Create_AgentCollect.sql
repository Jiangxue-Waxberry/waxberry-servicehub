CREATE TABLE mgr_agent_collect (
  id            VARCHAR(100) NOT NULL COMMENT '主键',
  user_id       VARCHAR(100) DEFAULT '' COMMENT 'userId',
  agent_id      VARCHAR(100) DEFAULT '' COMMENT 'agentId',
  create_time   DATETIME COMMENT '收藏时间',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;