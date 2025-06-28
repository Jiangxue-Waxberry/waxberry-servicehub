package com.jiangxue.waxberry.manager.baseMiniMode.repository;

import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelParamConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BaseMiniModelParamConfigRepository extends JpaRepository<BaseMiniModelParamConfig,String>, JpaSpecificationExecutor {


    @Query("SELECT c FROM BaseMiniModelParamConfig c WHERE c.waxberryId = :waxberryId")
    List<BaseMiniModelParamConfig> findByWaxberryId(@Param("waxberryId") String waxberryId);
}
