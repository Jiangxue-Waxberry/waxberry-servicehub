package com.jiangxue.waxberry.manager.baseMiniMode.repository;

import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BaseMiniModelRepository extends JpaRepository<BaseMiniModel,String>, JpaSpecificationExecutor {


    @Query("SELECT c FROM BaseMiniModel c WHERE c.type = :type")
    List<BaseMiniModel> findAllByType(@Param("type")String type);
}
