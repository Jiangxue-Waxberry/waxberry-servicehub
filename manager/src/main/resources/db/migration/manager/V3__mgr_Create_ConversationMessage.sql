CREATE TABLE mgr_conversation_message (
  id                   CHAR(40) NOT NULL COMMENT '主键',
  conversation_id      CHAR(40) DEFAULT '' COMMENT '会话ID',
  kb_id                VARCHAR(400) DEFAULT '' COMMENT '知识库ID',
  parent_id            CHAR(40) DEFAULT '' COMMENT '父消息ID',
  extend               VARCHAR(400) DEFAULT '' COMMENT '扩展信息（语言/文档ID）',
  reponse_file_id      VARCHAR(4000) DEFAULT '' COMMENT '关联文件ID',
  meta_data            VARCHAR(4000) DEFAULT '' COMMENT '元数据',
  feedback_score       INT DEFAULT 0 COMMENT '反馈评分（0-255）',
  feedback_reason      VARCHAR(4000) DEFAULT '' COMMENT '反馈原因',
  status               INT DEFAULT 0 COMMENT '状态;0:有效;1:已删除',
  create_time          DATETIME COMMENT '创建时间',
  creator              CHAR(40) DEFAULT '' COMMENT '创建者ID',
  modify_time          DATETIME COMMENT '最后修改时间',
  modifier             CHAR(40) DEFAULT '' COMMENT '最后修改人ID',
  query                LONGTEXT COMMENT '用户问题',
  reponse              LONGTEXT COMMENT 'AI回答',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;