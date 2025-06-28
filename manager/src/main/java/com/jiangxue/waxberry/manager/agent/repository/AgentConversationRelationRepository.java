package com.jiangxue.waxberry.manager.agent.repository;

import com.jiangxue.waxberry.manager.agent.entity.AgentConversationRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface AgentConversationRelationRepository extends JpaRepository<AgentConversationRelation,String>, JpaSpecificationExecutor {

    AgentConversationRelation findByAgentIdAndConversationType(String agentId, Integer conversationType);

}
