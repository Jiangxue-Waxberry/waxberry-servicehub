package com.jiangxue.waxberry.manager.agent.repository;

import com.jiangxue.waxberry.manager.agent.entity.AgentFolder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;


public interface AgentFolderRepository extends JpaRepository<AgentFolder,String>, JpaSpecificationExecutor {


    Optional<AgentFolder> findById(String id);

    @Query(value = "SELECT max(t.sortOrder) FROM AgentFolder t")
    Integer getMaxSortOrder();

    List<AgentFolder> findAllByCreatorIdAndType(String creatorId, Integer type, Sort sort);

    @Query(value = "SELECT count(1) FROM AgentFolder t where t.id not in (:id) and t.type = :type and t.name = :name and t.creatorId=:userId")
    Integer getNameCount(@Param("id") String id,
                         @Param("type") Integer type,
                         @Param("name") String name,@Param("userId") String userId);

    @Query(value = "SELECT count(1) FROM AgentFolder t where t.type = :type and t.name = :name and t.creatorId=:userId")
    Integer getNameCount(@Param("type") Integer type,
                         @Param("name") String name,@Param("userId") String userId);

}
