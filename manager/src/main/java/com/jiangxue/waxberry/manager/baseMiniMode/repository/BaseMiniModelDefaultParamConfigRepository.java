package com.jiangxue.waxberry.manager.baseMiniMode.repository;

import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelDefaultParamConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BaseMiniModelDefaultParamConfigRepository extends JpaRepository<BaseMiniModelDefaultParamConfig,String>, JpaSpecificationExecutor {

    @Query("SELECT c FROM BaseMiniModelDefaultParamConfig c WHERE c.type = :type")
    List<BaseMiniModelDefaultParamConfig> findAllByType(@Param("type")String type);
}
