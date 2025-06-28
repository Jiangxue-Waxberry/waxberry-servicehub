package com.jiangxue.waxberry.manager.redemptionCode.service.impl;

import com.jiangxue.framework.common.exception.BizException;
import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.redemptionCode.entity.RedemptionCode;
import com.jiangxue.waxberry.manager.redemptionCode.repository.RedemptionCodeRepository;
import com.jiangxue.waxberry.manager.redemptionCode.service.RedemptionCodeService;
import com.jiangxue.waxberry.manager.redemptionCode.utils.GenerateCodeUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class RedemptionCodeServiceImpl implements RedemptionCodeService {



    @Autowired
    private RedemptionCodeRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    private final String prefix = "YM";



    @Override
    public Boolean activationRedemptionCode(String code) {
        RedemptionCode rCode = repository.findByCode(code);
        if(!ObjectUtils.isEmpty(rCode)){

            //数据库查是否过期  是否使用过
            if(repository.countCodeIsUsed(code)>0){
                throw new BizException("当前验证码已经使用过");
            }else if(repository.countCodeIsExpired(code,Timestamp.valueOf(LocalDateTime.now()))>0){
                throw new BizException("当前验证码已经过期");
            }
            String userId = SecurityUtils.requireCurrentUserId();
            //为用户杨梅数+1
            updateUserCreateNum(userId);

            //更新兑换码
            rCode.setModifyTime(Timestamp.valueOf(LocalDateTime.now()));
            rCode.setStatus(1);
            rCode.setUserId(userId);
            repository.save(rCode);

        }else{
            throw new BizException("验证码无效");
        }
        return true;
    }

    public void updateUserCreateNum(String userId) {
        StringBuilder dataSQL = new StringBuilder("update auth_users t set t.create_num = t.create_num+1 where t.id =:userId ");
        Query query = entityManager.createNativeQuery(dataSQL.toString());
        query.setParameter("userId",userId);
        query.executeUpdate();
    }


}
