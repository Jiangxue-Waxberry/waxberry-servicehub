CREATE TABLE mgr_agent_supplementaryinfo (
  id                      VARCHAR(100) NOT NULL COMMENT '主键',
  waxberry_id             VARCHAR(100) NOT NULL COMMENT '杨梅ID',
  role_instruction        LONGTEXT COMMENT '角色指令',
  prologue                TEXT COMMENT '简介',
  recommended_question_one TEXT COMMENT '推荐问题一',
  recommended_question_two TEXT COMMENT '推荐问题二',
  recommended_question_three TEXT COMMENT '推荐问题三',
  creator_id              VARCHAR(500) DEFAULT '' COMMENT '创建人ID',
  updater_id              VARCHAR(500) DEFAULT '' COMMENT '更新人ID',
  create_time             DATETIME COMMENT '创建时间',
  update_time             DATETIME COMMENT '更新时间',
  use_tool_box            VARCHAR(80) DEFAULT '' COMMENT '使用工具箱',
  impose                  TEXT COMMENT '强制设置',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;