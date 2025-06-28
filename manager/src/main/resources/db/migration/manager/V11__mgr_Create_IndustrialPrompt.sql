CREATE TABLE mgr_industrial_prompt (
  id                   VARCHAR(50) NOT NULL COMMENT '主键',
  title                VARCHAR(50) NOT NULL COMMENT '提示词名称',
  content              LONGTEXT COMMENT '提示词内容',
  creator_id           VARCHAR(100) DEFAULT '' COMMENT '创建人ID',
  updater_id           VARCHAR(100) DEFAULT '' COMMENT '更新人ID',
  create_time          DATETIME COMMENT '创建时间',
  update_time          DATETIME COMMENT '更新时间',
  status               INT DEFAULT 0 COMMENT '状态',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;