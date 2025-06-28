package com.jiangxue.waxberry.manager.comments.repository;

import com.jiangxue.waxberry.manager.comments.entity.CommentsComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;


public interface CommentsCommentRepository extends JpaRepository<CommentsComment,String>, JpaSpecificationExecutor {


    Optional<CommentsComment> findById(String id);

    @Query("SELECT c  FROM CommentsComment c  WHERE c.subjectId = :subjectId  ORDER BY c.createTime ASC")
    Page<CommentsComment> findAllBySubjectId(@Param("subjectId") String subjectId, Pageable pageable);

    @Query("SELECT c  FROM CommentsComment c  WHERE c.subjectId = :subjectId ")
    List<CommentsComment> findAllBySubjectId(@Param("subjectId") String subjectId);

    void deleteBySubjectId(String subjectId);

    @Query(value = "SELECT count(1) FROM CommentsComment c  WHERE c.subjectId = :subjectId")
    Integer getCountBySubjectId(@Param("subjectId") String subjectId);
}
