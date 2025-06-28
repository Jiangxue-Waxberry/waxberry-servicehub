package com.jiangxue.waxberry.manager.baseMiniMode.service.impl;

import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModel;
import com.jiangxue.waxberry.manager.baseMiniMode.repository.BaseMiniModelRepository;
import com.jiangxue.waxberry.manager.baseMiniMode.service.BaseMiniModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BaseMiniModelServiceImpl implements BaseMiniModelService {

    @Autowired
    private BaseMiniModelRepository baseMiniModelRepository;


    @Override
    public BaseMiniModel save(BaseMiniModel config) {
        String userId = SecurityUtils.requireCurrentUserId();
        if(!ObjectUtils.isEmpty(userId)){
            config.setCreatorId(userId);
            config.setUpdaterId(userId);
        }
        config.setCreateTime(new Date());
        config.setUpdateTime(new Date());
        return baseMiniModelRepository.save(config);
    }

    @Override
    public BaseMiniModel findById(String id) {
        return baseMiniModelRepository.findById(id).orElse(null);
    }

    @Override
    public List<BaseMiniModel> findAllByType(String type) {
        return baseMiniModelRepository.findAllByType(type);
    }

    @Override
    public Page<BaseMiniModel> findAll(Pageable pageable) {
        return baseMiniModelRepository.findAll(pageable);
    }
}
