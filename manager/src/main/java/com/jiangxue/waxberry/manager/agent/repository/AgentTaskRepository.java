package com.jiangxue.waxberry.manager.agent.repository;

import com.jiangxue.waxberry.manager.agent.entity.AgentTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AgentTaskRepository extends JpaRepository<AgentTask,String>, JpaSpecificationExecutor {


    Page<AgentTask> findByAgentId(String agentId, Pageable pageable);

}
