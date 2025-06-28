package com.jiangxue.waxberry.manager.baseMiniMode.service;


import com.alibaba.fastjson.JSONObject;
import com.jiangxue.waxberry.manager.agent.dto.BaseMiniModelFileDTO;
import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelFile;

import java.util.List;

public interface BaseMiniModelFileService {
    //保存文件
    BaseMiniModelFile saveFile(JSONObject json);

    //根据waxberryId查询文件列表
    List<BaseMiniModelFileDTO> findByWaxberryId(String waxberryId);

    void deleteFile(JSONObject json);
}
