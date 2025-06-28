package com.jiangxue.waxberry.manager.agent.repository;

import com.jiangxue.waxberry.manager.agent.entity.AgentCollect;
import com.jiangxue.waxberry.manager.agent.dto.AgentRankDTO;
import com.jiangxue.waxberry.manager.agent.dto.AgentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface AgentCollectRepository extends JpaRepository<AgentCollect,String>, JpaSpecificationExecutor {

    void deleteById(String id);

    AgentCollect findByUserIdAndAgentId(String userId, String agentId);

    Long countByAgentId(String agentId);

    Integer countByAgentIdAndUserId(String agentId,String userId);

    @Query("SELECT new com.jiangxue.waxberry.manager.agent.dto.AgentRankDTO(c.agentId, a.name, a.agentLabel, a.discription, a.coverFileId, COUNT(c.id)) " +
            "FROM AgentCollect c JOIN Agent a ON c.agentId = a.id " +
            "GROUP BY c.agentId, a.name, a.agentLabel, a.discription, a.coverFileId " +
            "ORDER BY COUNT(c.id) DESC")
    List<AgentRankDTO> findTopCollectedAgents(Pageable pageable);


    @Query(value = " SELECT NEW com.jiangxue.waxberry.manager.agent.dto.AgentDTO( "+
            "t.id, t.name, t.agentLabel, t.discription, t.status, t.creatorId, "+
            "t.createTime, t.updateTime, t.classificationId, t.ismodify, t.sortOrder, "+
            "t.fileId, t.fileName, t.groupId, t.imgeFileId, t.vesselId, "+
            "t.coverFileId, s.username, t.vesselPort, t.type, "+
            "COUNT(DISTINCT la.id), "+
            "COUNT(DISTINCT ca.id), "+
            "CASE WHEN :currentUserId IS NULL THEN 0 "+
            "ELSE SUM(CASE WHEN la.userId = :currentUserId THEN 1 ELSE 0 END) END, "+
            "CASE WHEN :currentUserId IS NULL THEN 0 "+
            "ELSE SUM(CASE WHEN ca.userId = :currentUserId THEN 1 ELSE 0 END) END, "+
            "SUM(rma.runCount) "+
            ") "+
            "FROM AgentCollect ca "+
            "INNER JOIN Agent t ON ca.agentId = t.id "+
            "LEFT JOIN ManagerUser s ON t.creatorId = s.id "+
            "LEFT JOIN AgentLike la ON t.id = la.agentId "+
            "LEFT JOIN AgentRun rma ON t.id = rma.agentId "+
            "WHERE (:status IS NULL OR t.status = :status) "+
            "AND (:type IS NULL OR t.type = :type) "+
            "AND (:currentUserId IS NULL OR ca.userId = :currentUserId) "+
            "GROUP BY t.id, t.name, t.agentLabel, t.discription, t.status, t.creatorId, "+
            "t.createTime, t.updateTime, t.classificationId, t.ismodify, t.sortOrder, "+
            "t.fileId, t.fileName, t.groupId, t.imgeFileId, t.vesselId, "+
            "t.coverFileId, s.username, t.vesselPort, t.type ORDER BY t.createTime",
            countQuery = " SELECT COUNT(ca) FROM AgentCollect ca INNER JOIN Agent t ON ca.agentId = t.id "+
                    "WHERE (:status IS NULL OR t.status = :status) "+
                    "AND (:type IS NULL OR t.type = :type) "+
                    "AND (:currentUserId IS NULL OR ca.userId = :currentUserId) ")
    Page<AgentDTO> findAgentCollectList(@Param("type") Integer type, @Param("status") Integer status, @Param("currentUserId") String currentUserId, Pageable pageable);


}
