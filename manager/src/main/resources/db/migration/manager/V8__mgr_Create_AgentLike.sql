CREATE TABLE mgr_agent_like (
  id            VARCHAR(100) NOT NULL COMMENT '主键',
  user_id       VARCHAR(100) DEFAULT '' COMMENT '用户主键',
  agent_id      VARCHAR(100) DEFAULT '' COMMENT 'agent主键',
  create_time   DATETIME COMMENT '点赞时间',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;