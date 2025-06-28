package com.jiangxue.waxberry.manager.agent.service.impl;

import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.agent.constant.WaxberryConstant;
import com.jiangxue.waxberry.manager.agent.repository.AgentCollectRepository;
import com.jiangxue.waxberry.manager.agent.dto.AgentDTO;
import com.jiangxue.waxberry.manager.agent.service.AgentCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


@Slf4j
@Service
public class AgentCollectServiceImpl implements AgentCollectService {

    @Autowired
    private AgentCollectRepository agentCollectRepository;

    @Override
    public Page<AgentDTO> findAgentCollectList(Integer type, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<AgentDTO> agentList = agentCollectRepository.findAgentCollectList(type, WaxberryConstant.WaxberryStatus.PUBLISH,SecurityUtils.requireCurrentUserId(),pageable);
        return agentList;
    }
}
