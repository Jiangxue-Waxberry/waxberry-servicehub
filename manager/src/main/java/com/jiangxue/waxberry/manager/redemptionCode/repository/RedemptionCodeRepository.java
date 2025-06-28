package com.jiangxue.waxberry.manager.redemptionCode.repository;

import com.jiangxue.waxberry.manager.redemptionCode.entity.RedemptionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface RedemptionCodeRepository extends JpaRepository<RedemptionCode, String> {

    Integer countByCode(String code);

    @Query(" select count(1) from RedemptionCode c where c.code = :code and c.deadlineTime <= :deadlineTime")
    Integer countCodeIsExpired(@Param("code") String code,
                               @Param("deadlineTime") Timestamp deadlineTime);

    @Query(" select count(1) from RedemptionCode c where c.code = :code and c.status = 1")
    Integer countCodeIsUsed(@Param("code") String code);

    RedemptionCode findByCode(String code);


}
