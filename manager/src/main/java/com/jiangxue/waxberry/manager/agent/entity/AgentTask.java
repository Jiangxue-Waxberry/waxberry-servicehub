package com.jiangxue.waxberry.manager.agent.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name="mgr_agent_task")
@Validated
public class AgentTask implements Serializable {

	private static final long serialVersionUID = 749360940290141181L;

	@Id
	@Column
	@GeneratedValue(generator = "custom-uuid")
	@GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "content", columnDefinition = "LONGTEXT")
	private String content;

	@Column(name = "status")
	private Integer status = 0;

	@Column(name = "agent_id")
	private String agentId;

	@Column(name = "CREATOR_ID")
	private String creatorId;

	@Column(name = "UPDATER_ID")
	private String updaterId;

	@Column(name = "create_time")
	private Date createTime = new Date();

	@Column(name = "update_time")
	private Date updateTime = new Date();


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getUpdaterId() {
		return updaterId;
	}

	public void setUpdaterId(String updaterId) {
		this.updaterId = updaterId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public AgentTask() {
	}

	public AgentTask(String id, String name, String content, Integer status, String agentId, String creatorId, String updaterId, Date createTime, Date updateTime){
		this.id=id;
		this.name=name;
		this.content=content;
		this.status=status;
		this.agentId=agentId;
		this.creatorId=creatorId;
		this.updaterId=updaterId;
		this.createTime=createTime;
		this.updateTime=updateTime;
	}
}
