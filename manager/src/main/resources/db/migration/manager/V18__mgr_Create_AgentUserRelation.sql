CREATE TABLE mgr_agent_userrelation (
  id                VARCHAR(40) NOT NULL COMMENT '主键ID',
  agent_id          VARCHAR(40) DEFAULT '' COMMENT '智能体id',
  vessel_id         VARCHAR(40) DEFAULT '' COMMENT '容器id',
  vessel_port       INT DEFAULT 0 COMMENT '容器端口',
  create_time       DATETIME COMMENT '创建时间',
  creator           VARCHAR(40) DEFAULT '' COMMENT '创建人',
  modify_time       DATETIME COMMENT '修改时间',
  modifier          VARCHAR(40) DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;