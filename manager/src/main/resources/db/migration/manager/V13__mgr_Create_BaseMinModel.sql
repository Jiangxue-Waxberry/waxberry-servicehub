CREATE TABLE mgr_baseminmodel_model (
  id                   VARCHAR(255) NOT NULL COMMENT '主键',
  name                 VARCHAR(255) DEFAULT '' COMMENT '名称',
  type                 VARCHAR(255) DEFAULT '' COMMENT '基模类型（视觉模型/数据模型）',
  file_id               VARCHAR(255) DEFAULT '' COMMENT '文件ID',
  label                VARCHAR(255) DEFAULT '' COMMENT '标签',
  description          VARCHAR(255) DEFAULT '' COMMENT '描述信息',
  creator_id           VARCHAR(255) DEFAULT '' COMMENT '创建人ID',
  updater_id           VARCHAR(255) DEFAULT '' COMMENT '更新人ID',
  create_time          DATETIME COMMENT '创建时间',
  update_time          DATETIME COMMENT '更新时间',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;