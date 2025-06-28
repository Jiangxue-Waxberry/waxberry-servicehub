package com.jiangxue.waxberry.manager.baseMiniMode.service;

import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelParamConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseMiniModelParamConfigService {

    // 保存或更新
    List<BaseMiniModelParamConfig> saveAll(List<BaseMiniModelParamConfig> configs);

    // 根据ID查询
    List<BaseMiniModelParamConfig> findById(String waxberryId);

    // 查询所有
    List<BaseMiniModelParamConfig> findAll();

    // 分页查询
    Page<BaseMiniModelParamConfig> findAll(Pageable pageable);

}
