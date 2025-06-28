package com.jiangxue.waxberry.manager.agent.service;

import com.jiangxue.waxberry.manager.agent.dto.AgentDTO;
import org.springframework.data.domain.Page;

public interface AgentLikeService {

    Page<AgentDTO> findLikeAgentList(Integer type, int pageNo, int pageSize);

}
