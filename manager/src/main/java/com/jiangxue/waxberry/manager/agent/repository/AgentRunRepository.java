package com.jiangxue.waxberry.manager.agent.repository;

import com.jiangxue.waxberry.manager.agent.entity.AgentRun;
import com.jiangxue.waxberry.manager.agent.dto.AgentRunCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;



public interface AgentRunRepository extends JpaRepository<AgentRun,String>, JpaSpecificationExecutor {

    AgentRun findByUserIdAndAgentId(String userId, String agentId);

    @Query("SELECT new com.jiangxue.waxberry.manager.agent.dto.AgentRunCountDTO(r.agentId, SUM(r.runCount)) " +
            "FROM AgentRun r " +
            "WHERE r.agentId IN :agentIds " +
            "GROUP BY r.agentId")
    List<AgentRunCountDTO> getAgentRunCount(@Param("agentIds") List<String> agentIds);

    @Query(value = "SELECT SUM(rma.runCount) FROM AgentRun rma where rma.agentId = :agentId")
    Long getAgentRunCountByAgentId(@Param("agentId") String agentId);

}
