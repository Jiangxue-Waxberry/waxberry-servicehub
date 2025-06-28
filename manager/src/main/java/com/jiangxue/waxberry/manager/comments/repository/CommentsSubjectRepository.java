package com.jiangxue.waxberry.manager.comments.repository;

import com.jiangxue.waxberry.manager.comments.entity.CommentsSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


public interface CommentsSubjectRepository extends JpaRepository<CommentsSubject,String>, JpaSpecificationExecutor {


    Optional<CommentsSubject> findById(String id);

    @Query("SELECT c  FROM CommentsSubject c  WHERE c.agentId = :agentId  ORDER BY c.createTime ASC")
    Page<CommentsSubject> findAllByAgentIdDefault(@Param("agentId") String agentId, Pageable pageable);

    @Query("SELECT c  FROM CommentsSubject c  WHERE c.agentId = :agentId  ORDER BY c.createTime DESC")
    Page<CommentsSubject> findAllByAgentId(@Param("agentId") String agentId, Pageable pageable);
}
