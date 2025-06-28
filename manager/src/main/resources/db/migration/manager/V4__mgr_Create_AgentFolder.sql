CREATE TABLE mgr_agent_folder (
  id                   VARCHAR(100) NOT NULL COMMENT '主键',
  name                 VARCHAR(100) DEFAULT '' COMMENT 'agent文件夹名称',
  sort_order           INT DEFAULT 0 COMMENT 'agent文件夹排序',
  create_time          DATETIME COMMENT 'agent文件夹创建时间',
  creator_id            VARCHAR(100) DEFAULT '' COMMENT 'agent文件夹创建人',
  type                 INT DEFAULT 0 COMMENT '类型',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;