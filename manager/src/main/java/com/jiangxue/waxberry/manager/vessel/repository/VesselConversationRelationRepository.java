package com.jiangxue.waxberry.manager.vessel.repository;

import com.jiangxue.waxberry.manager.vessel.entity.VesselConversationRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 *  VesselConversationRelationRepository
 * @Description : 容器与会话关系Dao
 * @Author hgy
 * @Date 17:15 2025/5/6
 * @Version 1.0
 */
public interface VesselConversationRelationRepository extends JpaRepository<VesselConversationRelation,String>, JpaSpecificationExecutor {

    List<VesselConversationRelation> findByVesselId(String vesselId);

}
