package com.jiangxue.waxberry.manager.agent.service.impl;

import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.agent.constant.WaxberryConstant;
import com.jiangxue.waxberry.manager.agent.repository.AgentLikeRepository;
import com.jiangxue.waxberry.manager.agent.dto.AgentDTO;
import com.jiangxue.waxberry.manager.agent.service.AgentLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AgentLikeServiceImpl implements AgentLikeService {

    @Autowired
    private AgentLikeRepository agentLikeRepository;

    @Override
    public Page<AgentDTO> findLikeAgentList(Integer type, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        String currentUserId = SecurityUtils.requireCurrentUserId();
        Page<AgentDTO> agentList = agentLikeRepository.findAgentLikeList(type, WaxberryConstant.WaxberryStatus.PUBLISH,currentUserId,pageable);
        return agentList;
    }
}
