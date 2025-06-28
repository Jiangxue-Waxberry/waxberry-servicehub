package com.jiangxue.waxberry.manager.agent.service;


import com.jiangxue.waxberry.manager.agent.entity.AgentTask;
import org.springframework.data.domain.Page;

public interface AgentTaskService {


    AgentTask save(AgentTask agentTask);

    void delete(String id);

    void deleteByAgentId(String agentId);

    AgentTask findById(String id);

    Page<AgentTask> findAgentTaskByAgentId(String agentId,Integer pageNo,Integer pageSize);


}
