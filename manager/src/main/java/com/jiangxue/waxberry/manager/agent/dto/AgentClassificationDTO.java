package com.jiangxue.waxberry.manager.agent.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@Validated
public class AgentClassificationDTO implements Serializable {

	private static final long serialVersionUID = 749360940290141181L;

	private String id;

	private String name;

	private Integer sortOrder;

	private Date createTime;

	private Date updateTime;

	private String parentId;

	private String creatorId;

	private Integer isdisable;

	private List<AgentClassificationDTO> children;

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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Integer getIsdisable() {
		return isdisable;
	}

	public void setIsdisable(Integer isdisable) {
		this.isdisable = isdisable;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<AgentClassificationDTO> getChildren() {
		return children;
	}

	public void setChildren(List<AgentClassificationDTO> children) {
		this.children = children;
	}

	public AgentClassificationDTO(String id, String name, Integer sortOrder, Date createTime, Date updateTime, String parentId, String creatorId, Integer isdisable) {
		this.id = id;
		this.name = name;
		this.sortOrder = sortOrder;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.parentId = parentId;
		this.creatorId = creatorId;
		this.isdisable = isdisable;
	}
}
