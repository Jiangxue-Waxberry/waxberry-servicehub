package com.jiangxue.waxberry.manager.baseMiniMode.service.impl;

import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelDefaultParamConfig;
import com.jiangxue.waxberry.manager.baseMiniMode.repository.BaseMiniModelDefaultParamConfigRepository;
import com.jiangxue.waxberry.manager.baseMiniMode.service.BaseMiniModelDefaultParamConfigService;
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
public class BaseMiniModelDefaultParamConfigServiceImpl implements BaseMiniModelDefaultParamConfigService {


    @Autowired
    private BaseMiniModelDefaultParamConfigRepository baseMiniModelDefaultParamConfigRepository;

    @Override
    public BaseMiniModelDefaultParamConfig save(BaseMiniModelDefaultParamConfig config) {
        String userId = SecurityUtils.requireCurrentUserId();
        if(!ObjectUtils.isEmpty(userId)){
            config.setCreatorId(userId);
            config.setUpdaterId(userId);
        }
        config.setCreateTime(new Date());
        config.setUpdateTime(new Date());
        return baseMiniModelDefaultParamConfigRepository.save(config);
    }

    @Override
    public BaseMiniModelDefaultParamConfig findById(String id) {
        return baseMiniModelDefaultParamConfigRepository.findById(id).orElse(null);
    }

    @Override
    public List<BaseMiniModelDefaultParamConfig> findAllByType(String type) {
        return baseMiniModelDefaultParamConfigRepository.findAllByType(type);
    }

    @Override
    public Page<BaseMiniModelDefaultParamConfig> findAll(Pageable pageable) {
        return baseMiniModelDefaultParamConfigRepository.findAll(pageable);
    }


}
