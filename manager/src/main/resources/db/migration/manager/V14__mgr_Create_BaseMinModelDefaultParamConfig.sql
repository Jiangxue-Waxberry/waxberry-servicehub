CREATE TABLE mgr_baseminmodel_defaultparamconfig (
  id                   VARCHAR(255) NOT NULL COMMENT '主键',
  type                 VARCHAR(255) DEFAULT '' COMMENT '参数类型',
  name                 VARCHAR(255) DEFAULT '' COMMENT '参数名称',
  datatype             VARCHAR(255) DEFAULT '' COMMENT '数据类型',
  default_value         VARCHAR(255) DEFAULT '' COMMENT '默认值',
  description          VARCHAR(255) DEFAULT '' COMMENT '描述信息',
  creator_id           VARCHAR(255) DEFAULT '' COMMENT '创建人ID',
  updater_id           VARCHAR(255) DEFAULT '' COMMENT '更新人ID',
  create_time          DATETIME COMMENT '创建时间',
  update_time          DATETIME COMMENT '更新时间',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;