CREATE TABLE mgr_agent_task (
  id                   VARCHAR(100) NOT NULL COMMENT '主键',
  name                 VARCHAR(100) DEFAULT '' COMMENT '任务名称',
  content              LONGTEXT COMMENT '任务内容',
  status               INT COMMENT '任务状态',
  agent_id             VARCHAR(100) DEFAULT '' COMMENT 'agent主键',
  creator_id           VARCHAR(100) DEFAULT '' COMMENT '创建人',
  updater_id           VARCHAR(100) DEFAULT '' COMMENT '更新人',
  create_time          DATETIME COMMENT '创建时间',
  update_time          DATETIME COMMENT '更新时间',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;