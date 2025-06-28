package com.jiangxue.waxberry.manager.agent.service.impl;

import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.agent.service.AgentUserRelationService;
import com.jiangxue.waxberry.manager.agent.repository.AgentUserRelationRepository;
import com.jiangxue.waxberry.manager.agent.entity.AgentUserRelation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
public class AgentUserRelationServiceImpl implements AgentUserRelationService {

    @Autowired
    private AgentUserRelationRepository agentUserRelationRepository;

    @Override
    public AgentUserRelation findUserRelation(String agentId) {
        return agentUserRelationRepository.findByAgentIdAndCreator(agentId,SecurityUtils.requireCurrentUserId());
    }

    @Override
    public AgentUserRelation add(AgentUserRelation agentUserRelation) {
        return agentUserRelationRepository.save(agentUserRelation);
    }

}
