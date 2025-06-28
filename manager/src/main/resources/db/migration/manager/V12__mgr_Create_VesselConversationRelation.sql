CREATE TABLE mgr_vessel_conversationrelation (
  id                   VARCHAR(40) NOT NULL COMMENT '主键ID',
  agent_id             VARCHAR(40) DEFAULT '' COMMENT '杨梅id',
  conversation_id      VARCHAR(40) DEFAULT '' COMMENT '会话id',
  conversation_type    INT DEFAULT 1 COMMENT '会话类型 1:生成需求报告 2:与AI沟通',
  create_time          DATETIME COMMENT '创建时间',
  creator              VARCHAR(40) DEFAULT '' COMMENT '创建人',
  modify_time          DATETIME COMMENT '修改时间',
  modifier             VARCHAR(40) DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;