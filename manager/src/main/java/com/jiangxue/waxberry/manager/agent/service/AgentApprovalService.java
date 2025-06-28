package com.jiangxue.waxberry.manager.agent.service;

import com.jiangxue.waxberry.manager.agent.dto.AgentApprovalDTO;
import com.jiangxue.waxberry.manager.agent.entity.AgentApproval;
import org.springframework.data.domain.Page;

public interface AgentApprovalService{

    Page<AgentApprovalDTO> findAgentApprovalList(String status,int pageNo, int pageSize,Integer sort,String sortField,String userType,String agentType,String name);

    AgentApproval addAgentApproval(AgentApproval approval);

    void updateAgentApproval(String id, String status, String approvalLanguage);
}
