CREATE TABLE mgr_conversation_conversation (
  id                   VARCHAR(100) NOT NULL COMMENT '主键',
  title                VARCHAR(4000) DEFAULT '' COMMENT '会话名称',
  chat_type            INT DEFAULT 0 COMMENT '会话类型（1=文本，2=语音等）',
  status               INT DEFAULT 0 COMMENT '状态;0:有效;1:已删除',
  create_time          DATETIME COMMENT '创建时间',
  creator              VARCHAR(40) DEFAULT '' COMMENT '创建者ID',
  modify_time          DATETIME COMMENT '最后修改时间',
  modifier             VARCHAR(40) DEFAULT '' COMMENT '最后修改人ID',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;