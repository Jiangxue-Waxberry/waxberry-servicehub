package com.jiangxue.waxberry.manager.baseMiniMode.service.impl;

import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelExtend;
import com.jiangxue.waxberry.manager.baseMiniMode.repository.BaseMiniModelExtendRepository;
import com.jiangxue.waxberry.manager.baseMiniMode.service.BaseMiniModelExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Service
@Transactional
public class BaseMiniModelExtendServiceImpl implements BaseMiniModelExtendService {

    @Autowired
    private BaseMiniModelExtendRepository baseMiniModelExtendRepository;


    @Override
    public BaseMiniModelExtend save(BaseMiniModelExtend baseMiniModelExtend) {
        BaseMiniModelExtend byWaxberryId = baseMiniModelExtendRepository.findByWaxberryId(baseMiniModelExtend.getWaxberryId());
        if(!ObjectUtils.isEmpty(byWaxberryId)){
            baseMiniModelExtendRepository.deleteById(byWaxberryId.getId());
        }
        String userId = SecurityUtils.requireCurrentUserId();
        if(!ObjectUtils.isEmpty(userId)){
            baseMiniModelExtend.setCreatorId(userId);
            baseMiniModelExtend.setUpdaterId(userId);
        }
        baseMiniModelExtend.setCreateTime(new Date());
        baseMiniModelExtend.setUpdateTime(new Date());
        return baseMiniModelExtendRepository.save(baseMiniModelExtend);
    }

    @Override
    public BaseMiniModelExtend findByWaxberryId(String id) {
        return baseMiniModelExtendRepository.findByWaxberryId(id);
    }

}
