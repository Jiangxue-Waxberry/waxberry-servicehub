CREATE TABLE mgr_agent_conversationrelation (
  id                    VARCHAR(40) NOT NULL COMMENT '主键ID',
  vessel_id             VARCHAR(40) DEFAULT '' COMMENT '容器id',
  conversation_id       VARCHAR(40) DEFAULT '' COMMENT '会话id',
  conversation_name     VARCHAR(500) DEFAULT '' COMMENT '会话名称',
  create_time           DATETIME COMMENT '创建时间',
  creator               VARCHAR(40) DEFAULT '' COMMENT '创建人',
  modify_time           DATETIME COMMENT '修改时间',
  modifier              VARCHAR(40) DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;