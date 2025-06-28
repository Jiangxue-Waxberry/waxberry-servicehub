package com.jiangxue.waxberry.manager.agent.repository;

import com.jiangxue.waxberry.manager.agent.entity.AgentUserRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface AgentUserRelationRepository extends JpaRepository<AgentUserRelation,String>, JpaSpecificationExecutor {

    AgentUserRelation findByAgentIdAndCreator(String agentId, String creator);

}
