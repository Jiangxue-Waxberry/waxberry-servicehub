package com.jiangxue.waxberry.manager.agent.service;

import com.jiangxue.waxberry.manager.agent.entity.AgentFolder;
import java.util.List;


public interface AgentFolderService {

	AgentFolder addOrUpdateAgentFolder(AgentFolder classification);

	AgentFolder findById(String id);

	List<AgentFolder> findAll();

	Integer getMaxSortOrder();

	void deleteAgentFolder(String id);

	List<AgentFolder> findAllByCreatorId(String creatorId,String type);

	Integer getNameCount(String id,String name,Integer type);
}
