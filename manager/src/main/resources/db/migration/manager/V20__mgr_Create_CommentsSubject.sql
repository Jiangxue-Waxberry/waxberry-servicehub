CREATE TABLE mgr_comments_subject (
  id                  VARCHAR(50) NOT NULL COMMENT '主键ID',
  content             VARCHAR(500) NOT NULL COMMENT '正文内容',
  user_id             VARCHAR(50) NOT NULL COMMENT '作者ID',
  user_name           VARCHAR(400) NOT NULL COMMENT '作者名称',
  like_count          INT DEFAULT 0 COMMENT '点赞数',
  comment_count       INT DEFAULT 0 COMMENT '评论数',
  agent_id            VARCHAR(50) NOT NULL COMMENT '智能体ID',
  file_id             VARCHAR(400) DEFAULT '' COMMENT '文件ID',
  audio_id            VARCHAR(400) DEFAULT '' COMMENT '音频ID',
  create_time         DATETIME COMMENT '创建时间',
  creator             VARCHAR(40) DEFAULT '' COMMENT '创建用户',
  update_time         DATETIME COMMENT '更新时间',
  modifier            VARCHAR(40) DEFAULT '' COMMENT '更新用户',
  file_name           VARCHAR(500) DEFAULT '' COMMENT '文件名',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;