package com.jiangxue.waxberry.manager.agent.repository;

import com.jiangxue.waxberry.manager.agent.entity.AgentSupplementaryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface AgentSupplementaryInfoRepository extends JpaRepository<AgentSupplementaryInfo,String>, JpaSpecificationExecutor {

    AgentSupplementaryInfo findByWaxberryId(String waxberryId);

}
