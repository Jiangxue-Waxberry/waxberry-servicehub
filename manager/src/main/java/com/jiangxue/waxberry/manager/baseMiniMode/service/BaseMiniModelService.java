package com.jiangxue.waxberry.manager.baseMiniMode.service;

import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseMiniModelService {

    // 保存或更新
    BaseMiniModel save(BaseMiniModel config);

    // 根据ID查询
    BaseMiniModel findById(String id);

    // 查询所有
    List<BaseMiniModel> findAllByType(String type);

    // 分页查询
    Page<BaseMiniModel> findAll(Pageable pageable);


}
