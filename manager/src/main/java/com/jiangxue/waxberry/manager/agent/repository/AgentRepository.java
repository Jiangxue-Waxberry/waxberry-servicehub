package com.jiangxue.waxberry.manager.agent.repository;

import com.jiangxue.waxberry.manager.agent.entity.Agent;
import com.jiangxue.waxberry.manager.agent.dto.AgentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;



public interface AgentRepository extends JpaRepository<Agent,String>, JpaSpecificationExecutor {





    @Query(value = "select NEW com.jiangxue.waxberry.manager.agent.dto.AgentDTO(t.id,t.name,t.agentLabel,t.discription,t.status,t.creatorId,t.createTime,t.updateTime,t.classificationId,t.ismodify,t.sortOrder,t.fileId,t.fileName,t.groupId,t.imgeFileId,t.vesselId,t.coverFileId,s.loginname,t.vesselPort,t.type,t.step,t.isCopy)  FROM Agent t left join ManagerUser s on t.creatorId = s.id where" +
            "  t.classificationId = :classifictionId AND (:type IS NULL OR t.type = :type) and t.creatorId = :creatorId ")
    Page<AgentDTO> findByClassificationIdAndCreatorId(@Param("classifictionId") String classifictionId, @Param("type") Integer type,
                                                      @Param("creatorId") String creatorId, Pageable pageable);


    @Query(value = "select NEW com.jiangxue.waxberry.manager.agent.dto.AgentDTO(t.id,t.name,t.agentLabel,t.discription,t.status,t.creatorId,t.createTime,t.updateTime,t.classificationId,t.ismodify,t.sortOrder,t.fileId,t.fileName,t.groupId,t.imgeFileId,t.vesselId,t.coverFileId,s.loginname,t.vesselPort,t.type,t.step,t.isCopy)  FROM Agent t left join ManagerUser s on t.creatorId = s.id " +
            "where t.creatorId = :creatorId AND (:type IS NULL OR t.type = :type) ")
    Page<AgentDTO> findByCreatorId(@Param("type") Integer type, @Param("creatorId") String creatorId, Pageable pageable);

    @Query(value = " SELECT NEW com.jiangxue.waxberry.manager.agent.dto.AgentDTO( "+
            "t.id, t.name, t.agentLabel, t.discription, t.status, t.creatorId, "+
                    "t.createTime, t.updateTime, t.classificationId, t.ismodify, t.sortOrder, "+
            "t.fileId, t.fileName, t.groupId, t.imgeFileId, t.vesselId, "+
            "t.coverFileId, s.loginname as creatorName, t.vesselPort, t.type, "+
            "COUNT(DISTINCT la.id) as likeCount, "+
            "COUNT(DISTINCT ca.id) as collectCount, "+
            "CASE WHEN :currentUserId IS NULL THEN 0 "+
            "ELSE SUM(CASE WHEN la.userId = :currentUserId THEN 1 ELSE 0 END) END as isLike, "+
            "CASE WHEN :currentUserId IS NULL THEN 0 "+
            "ELSE SUM(CASE WHEN ca.userId = :currentUserId THEN 1 ELSE 0 END) END as isCollect, "+
            "SUM(rma.runCount) as runCount"+
            ") "+
            "FROM Agent t "+
            "LEFT JOIN ManagerUser s ON t.creatorId = s.id "+
            "LEFT JOIN AgentLike la ON t.id = la.agentId "+
        "LEFT JOIN AgentCollect ca ON t.id = ca.agentId "+
        "LEFT JOIN AgentRun rma ON t.id = rma.agentId "+
        "WHERE (:status IS NULL OR t.status = :status) "+
        "AND (:creatorId IS NULL OR t.creatorId = :creatorId) "+
        "AND (:type IS NULL OR t.type = :type) "+
        "AND (:groupId IS NULL OR t.groupId LIKE %:groupId%) "+
        "AND (:name IS NULL OR t.name LIKE %:name%) "+
        "GROUP BY t.id, t.name, t.agentLabel, t.discription, t.status, t.creatorId, "+
            "t.createTime, t.updateTime, t.classificationId, t.ismodify, t.sortOrder, "+
            "t.fileId, t.fileName, t.groupId, t.imgeFileId, t.vesselId, "+
            "t.coverFileId, s.loginname, t.vesselPort, t.type ",
            countQuery = " SELECT COUNT(t) FROM Agent t "+
            "WHERE (:status IS NULL OR t.status = :status) "+
            "AND (:type IS NULL OR t.type = :type) "+
            "AND (:groupId IS NULL OR t.groupId LIKE %:groupId%) "+
            "AND (:name IS NULL OR t.name LIKE %:name%) ")
    Page<AgentDTO> findByStatusAndGroupId(
            @Param("status") Integer status,
            @Param("creatorId") String creatorId,
            @Param("type") Integer type,
            @Param("groupId") String groupId,
            @Param("name") String name,
            @Param("currentUserId") String currentUserId,
            Pageable pageable);

    @Query(value = "select NEW com.jiangxue.waxberry.manager.agent.dto.AgentDTO(t.id,t.name,t.agentLabel,t.discription,t.status,t.creatorId,t.createTime,t.updateTime,t.classificationId,t.ismodify,t.sortOrder,t.fileId,t.fileName,t.groupId,t.imgeFileId,t.vesselId,t.coverFileId,s.loginname,t.vesselPort,t.type,t.step,t.isCopy)  from Agent t left join ManagerUser s on t.creatorId = s.id " +
            "where t.id = :id")
    Optional<AgentDTO> findOneById(@Param("id") String id);

    @Query(value = "SELECT max(t.sortOrder) FROM Agent t  ")
    Integer getMaxSortOrder();

    @Query(value = "SELECT max(t.sortOrder) FROM Agent t where t.classificationId = :classifictionId")
    Integer getMaxSortOrderByClassificationId(@Param("classifictionId") String classifictionId);


    @Query(value = " FROM Agent t where (:name IS NULL OR t.name LIKE %:name%)")
    Page<Agent> findAll(@Param("name") String name,Pageable pageable);


    @Query(value = "FROM Agent t where t.vesselId = :vesseId")
    Optional<Agent> findByVesselId(@Param("vesseId") String vesseId);

    @Query(value = "select NEW com.jiangxue.waxberry.manager.agent.dto.AgentDTO(t.id,t.name,t.agentLabel,t.discription,t.status,t.creatorId,t.createTime,t.updateTime,t.classificationId,t.ismodify,t.sortOrder,t.fileId,t.fileName,t.groupId,t.imgeFileId,t.vesselId,t.coverFileId,s.loginname,t.vesselPort,t.type)  " +
            "from Agent t " +
            "left join ManagerUser s on t.creatorId = s.id" +
            " where t.id in(select r.agentId from AgentRun r " +
            "where (:classificationId IS NULL OR r.classificationId = :classificationId)" +
            "  AND (:userId IS NULL OR r.userId = :userId)) AND (:type IS NULL OR t.type = :type) order by t.createTime desc")
    Page<AgentDTO> findByRunMarketAgent(@Param("classificationId") String classificationId,
                                        @Param("type") Integer type,
                                        @Param("userId") String userId, Pageable pageable);

    @Query(value = "select NEW com.jiangxue.waxberry.manager.agent.dto.AgentDTO(t.id,t.name,t.agentLabel,t.discription,t.status,t.creatorId,t.createTime,t.updateTime,t.classificationId,t.ismodify,t.sortOrder,t.fileId,t.fileName,t.groupId,t.imgeFileId,t.vesselId,t.coverFileId,s.loginname,t.vesselPort,t.type)  " +
            "from Agent t " +
            "left join ManagerUser s on t.creatorId = s.id" +
            " where t.id in(select r.agentId from AgentRun r " +
            "where  r.userId = :userId) AND (:type IS NULL OR t.type = :type) order by t.createTime desc")
    Page<AgentDTO> findByRunMarketAgent(@Param("type") Integer type, @Param("userId") String userId, Pageable pageable);

    @Query(value = "select count(1) from Agent t where t.creatorId = :userId")
    Integer countAgentByCreatorId(@Param("userId") String userId);

}
