package com.jiangxue.waxberry.manager.comments.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;
import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Data
@Entity
@Table(name="mgr_comments_like")
@Validated
public class CommentsLike implements Serializable {

	private static final long serialVersionUID = 749360940290141181L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "subject_id")
	private String subjectId;

	@Column(name = "child_id")
	private String childId;

	@Column(name = "like_flag")
	private String likeFlag;

	@Column(name = "create_time")
	private Timestamp createTime;

	@Column(name = "update_time")
	private Timestamp  updateTime;

	@Column(name = "creator")
	private String creator;

	@Column(name = "modifier")
	private String modifier;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getLikeFlag() {
		return likeFlag;
	}

	public void setLikeFlag(String likeFlag) {
		this.likeFlag = likeFlag;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
}
