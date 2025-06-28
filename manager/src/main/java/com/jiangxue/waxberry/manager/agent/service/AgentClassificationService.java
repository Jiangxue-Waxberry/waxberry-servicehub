package com.jiangxue.waxberry.manager.agent.service;



import com.jiangxue.waxberry.manager.agent.dto.AgentClassificationDTO;
import com.jiangxue.waxberry.manager.agent.entity.AgentClassification;
import java.util.List;


public interface AgentClassificationService {

	AgentClassification addOrUpdateAgentClassification(AgentClassification appMenu);

	void deleteAgentClassification(String id);

	AgentClassification findById(String id);

	Integer getMaxSortOrder();

	List<AgentClassificationDTO> findAllByParentId();

}
