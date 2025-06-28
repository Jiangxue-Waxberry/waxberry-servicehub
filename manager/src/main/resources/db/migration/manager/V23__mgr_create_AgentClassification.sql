CREATE TABLE mgr_agent_classification(
  id                  VARCHAR(100) NOT NULL COMMENT '主键',
  name                VARCHAR(100)  DEFAULT '' COMMENT '分组名称',
  sort_order          INT DEFAULT 0 COMMENT '排序',
  create_time         DATETIME COMMENT 'agent分组创建时间',
  update_time         DATETIME COMMENT 'agent分组更新时间',
  creator_id           VARCHAR(100) DEFAULT ''  COMMENT 'agent分组创建人',
  isdisable           INT DEFAULT 0 COMMENT 'agent分组是否禁用(0-启用,1-禁用)',
  parent_id           VARCHAR(100) DEFAULT ''  COMMENT '父分组ID',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;