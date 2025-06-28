package com.jiangxue.waxberry.manager.agent.service.impl;

import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.agent.entity.AgentConversationRelation;
import com.jiangxue.waxberry.manager.agent.repository.AgentConversationRelationRepository;
import com.jiangxue.waxberry.manager.agent.service.AgentConversationRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;


@Slf4j
@Service
@Transactional
public class AgentConversationRelationServiceImpl implements AgentConversationRelationService {

    @Autowired
    private AgentConversationRelationRepository agentConversationRelationRepository;

    @Override
    public AgentConversationRelation findAgentConversationRelation(String agentId, Integer conversationType) {
        return agentConversationRelationRepository.findByAgentIdAndConversationType(agentId,conversationType);
    }

    @Override
    public AgentConversationRelation add(AgentConversationRelation conversationRelation) {
        String userId = SecurityUtils.requireCurrentUserId();
        conversationRelation.setCreator(userId);
        conversationRelation.setCreateTime(new Date());
        return agentConversationRelationRepository.save(conversationRelation);
    }
}
