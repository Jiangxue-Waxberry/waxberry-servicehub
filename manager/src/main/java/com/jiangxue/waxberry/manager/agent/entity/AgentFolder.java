package com.jiangxue.waxberry.manager.agent.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name="mgr_agent_folder")
@Validated
public class AgentFolder implements Serializable {

	private static final long serialVersionUID = 749360940290141181L;

	@Id
	@Column
	@GeneratedValue(generator = "custom-uuid")
	@GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "sort_order")
	private Integer sortOrder;

	@Column(name = "create_time")
	private Date createTime = new Date();


	@Column(name = "type")
	private Integer type;

	@Column(name = "creator_id")
	private String creatorId;



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

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
