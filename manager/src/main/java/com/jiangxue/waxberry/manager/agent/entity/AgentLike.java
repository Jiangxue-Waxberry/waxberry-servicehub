package com.jiangxue.waxberry.manager.agent.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name="mgr_agent_like")
@Validated
public class AgentLike implements Serializable {

	private static final long serialVersionUID = 749360940290141181L;

	@Id
	@Column
	@GeneratedValue(generator = "custom-uuid")
	@GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
	private String id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "agent_id")
	private String agentId;

	@Column(name = "create_time")
	private Date createTime = new Date();

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

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
