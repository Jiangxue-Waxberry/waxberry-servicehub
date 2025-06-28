package com.jiangxue.waxberry.manager.agent.service;


import com.jiangxue.waxberry.manager.agent.entity.AgentUserRelation;


public interface AgentUserRelationService {

    AgentUserRelation findUserRelation(String agentId);

    AgentUserRelation add(AgentUserRelation agentUserRelation);

}
