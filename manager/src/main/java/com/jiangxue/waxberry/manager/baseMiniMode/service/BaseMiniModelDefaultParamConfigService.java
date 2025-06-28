package com.jiangxue.waxberry.manager.baseMiniMode.service;

import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelDefaultParamConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseMiniModelDefaultParamConfigService {

    // 保存或更新
    BaseMiniModelDefaultParamConfig save(BaseMiniModelDefaultParamConfig config);

    // 根据ID查询
    BaseMiniModelDefaultParamConfig findById(String id);

    // 查询所有
    List<BaseMiniModelDefaultParamConfig> findAllByType(String type);

    // 分页查询
    Page<BaseMiniModelDefaultParamConfig> findAll(Pageable pageable);

}
