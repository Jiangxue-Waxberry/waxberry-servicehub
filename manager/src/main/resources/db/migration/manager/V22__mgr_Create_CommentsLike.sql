CREATE TABLE mgr_comments_like (
  id                VARCHAR(50) NOT NULL COMMENT '主键ID',
  user_id           VARCHAR(50) NOT NULL COMMENT '用户ID',
  subject_id        VARCHAR(50) DEFAULT '' COMMENT '主体评论ID',
  child_id          VARCHAR(50) DEFAULT '' COMMENT '子评论ID',
  like_flag         VARCHAR(50) NOT NULL COMMENT '点赞标识',
  create_time       DATETIME COMMENT '创建时间',
  creator           VARCHAR(40) DEFAULT ''  COMMENT '创建用户',
  update_time       DATETIME COMMENT '更新时间',
  modifier          VARCHAR(40) COMMENT '更新用户',
  PRIMARY KEY (id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;