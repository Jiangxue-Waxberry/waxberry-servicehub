package com.jiangxue.waxberry.manager.agent.service;


import com.alibaba.fastjson.JSONObject;
import com.jiangxue.waxberry.manager.agent.entity.AgentSupplementaryInfo;

public interface AgentSupplementaryInfoService {

    AgentSupplementaryInfo findByWaxberryId(String waxberryId);

    AgentSupplementaryInfo addOrUpdate(JSONObject json);

    AgentSupplementaryInfo findById(String waxberryId);

}
