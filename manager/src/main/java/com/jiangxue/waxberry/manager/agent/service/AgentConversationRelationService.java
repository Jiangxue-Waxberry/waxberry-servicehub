package com.jiangxue.waxberry.manager.agent.service;


import com.jiangxue.waxberry.manager.agent.entity.AgentConversationRelation;


public interface AgentConversationRelationService {

    AgentConversationRelation findAgentConversationRelation(String agentId, Integer conversationType);

    AgentConversationRelation add(AgentConversationRelation conversationRelation);

}
