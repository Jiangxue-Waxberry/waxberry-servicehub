package com.jiangxue.waxberry.manager.baseMiniMode.service;


import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelExtend;

public interface BaseMiniModelExtendService {

    // 保存或更新
    BaseMiniModelExtend save(BaseMiniModelExtend config);

    // 根据ID查询
    BaseMiniModelExtend findByWaxberryId(String waxberryId);

}
