package com.jiangxue.waxberry.manager.comments.repository;

import com.jiangxue.waxberry.manager.comments.entity.CommentsLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CommentsLikeRepository extends JpaRepository<CommentsLike,String>, JpaSpecificationExecutor {


    Optional<CommentsLike> findById(String id);

    void deleteByChildId(String childId);

    void deleteBySubjectId(String subjectId);

    @Query(value = "SELECT c FROM CommentsLike c  WHERE c.userId = :userId AND c.subjectId in (:subjectId) and c.childId is null")
    List<CommentsLike> findAllByUseridAndSubjectId(@Param("userId") String userId ,@Param("subjectId") List<String> subjectId);

    @Query(value = "SELECT c FROM CommentsLike c  WHERE c.userId = :userId AND c.childId in (:childId)")
    List<CommentsLike> findAllByUseridAndChildId(@Param("userId") String userId ,@Param("childId") List<String> childId);

    @Query(value = "SELECT count(1) FROM CommentsLike c  WHERE c.subjectId = :subjectId")
    Integer getCountBySubjectId(@Param("subjectId") String subjectId);

    @Query(value = "SELECT count(1) FROM CommentsLike c  WHERE c.childId = :childId")
    Integer getCountByChildId(@Param("childId") String childId);
}
