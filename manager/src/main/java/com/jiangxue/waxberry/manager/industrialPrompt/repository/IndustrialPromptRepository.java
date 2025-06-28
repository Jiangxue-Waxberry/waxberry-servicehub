package com.jiangxue.waxberry.manager.industrialPrompt.repository;


import com.jiangxue.waxberry.manager.industrialPrompt.dto.IndustrialPromptDto;
import com.jiangxue.waxberry.manager.industrialPrompt.entity.IndustrialPrompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IndustrialPromptRepository extends JpaRepository<IndustrialPrompt,String>, JpaSpecificationExecutor {

    @Query("SELECT NEW com.jiangxue.waxberry.manager.industrialPrompt.dto.IndustrialPromptDto(p.id,p.title,p.content,p.status,p.creatorId,t.username,p.updaterId,p.createTime,p.updateTime) FROM IndustrialPrompt p " +
            " left join ManagerUser t on t.id = p.creatorId " +
            "WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')) " +
            "AND (:creatorId IS NULL OR :creatorId = '' OR p.creatorId = :creatorId) " +
            "AND (:creatorId IS NOT NULL AND :creatorId <> '' OR p.status = '1')")
    List<IndustrialPromptDto> searchByTitleOrContent(@Param("title")String title, @Param("creatorId")String creatorId);

    @Query("SELECT NEW com.jiangxue.waxberry.manager.industrialPrompt.dto.IndustrialPromptDto(p.id,p.title,p.content,p.status,p.creatorId,t.username,p.updaterId,p.createTime,p.updateTime) FROM IndustrialPrompt p " +
            "left join ManagerUser t on t.id = p.creatorId" +
            " WHERE 1=1 " +
            "AND (:creatorId IS NULL OR :creatorId = '' OR p.creatorId = :creatorId) " +
            "AND (:creatorId IS NOT NULL AND :creatorId <> '' OR p.status = '1')")
    List<IndustrialPromptDto> findByCreatorId(@Param("creatorId")String creatorId);

}
