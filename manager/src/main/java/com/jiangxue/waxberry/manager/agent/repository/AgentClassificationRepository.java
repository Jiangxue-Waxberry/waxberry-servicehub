package com.jiangxue.waxberry.manager.agent.repository;

import com.jiangxue.waxberry.manager.agent.dto.AgentClassificationDTO;
import com.jiangxue.waxberry.manager.agent.entity.AgentClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;


public interface AgentClassificationRepository extends JpaRepository<AgentClassification,String>, JpaSpecificationExecutor {

    Optional<AgentClassification> findById(String id);

    @Query(value = "SELECT max(t.sortOrder) FROM AgentClassification t  ")
    Integer getMaxSortOrder();

    @Query(value = "select  NEW com.jiangxue.waxberry.manager.agent.dto.AgentClassificationDTO(t.id,t.name,t.sortOrder,t.createTime,t.updateTime,t.parentId,t.creatorId,t.isdisable) FROM AgentClassification t where t.parentId = :parentId ORDER BY t.sortOrder asc")
    List<AgentClassificationDTO> findAllByParentId(@Param("parentId") String parentId);



}
