CREATE TABLE mgr_baseminmodel_extend (
  id                   VARCHAR(255) NOT NULL COMMENT '主键',
  waxberry_id          VARCHAR(255) DEFAULT '' COMMENT '关联小模型ID',
  name                 VARCHAR(255) DEFAULT '' COMMENT '扩展名称',
  type                 VARCHAR(255) DEFAULT '' COMMENT '扩展类型',
  template_name        VARCHAR(2000) DEFAULT '' COMMENT '模板名称',
  creator_id           VARCHAR(255) DEFAULT '' COMMENT '创建人ID',
  updater_id           VARCHAR(255) DEFAULT '' COMMENT '更新人ID',
  create_time          DATETIME COMMENT '创建时间',
  update_time          DATETIME COMMENT '更新时间',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;