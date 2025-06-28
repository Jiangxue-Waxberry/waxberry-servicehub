package com.jiangxue.waxberry.manager.baseMiniMode.repository;

import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BaseMiniModelExtendRepository extends JpaRepository<BaseMiniModelExtend,String>, JpaSpecificationExecutor {


    @Query("SELECT c FROM BaseMiniModelExtend c WHERE c.waxberryId = :waxberryId")
    BaseMiniModelExtend findByWaxberryId(@Param("waxberryId")String waxberryId);
}
