package com.jiangxue.waxberry.manager.agent.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.exception.BizException;
import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.agent.entity.Agent;
import com.jiangxue.waxberry.manager.agent.repository.AgentRepository;
import com.jiangxue.waxberry.manager.agent.service.AgentSupplementaryInfoService;
import com.jiangxue.waxberry.manager.agent.constant.WaxberryConstant;
import com.jiangxue.waxberry.manager.agent.repository.AgentSupplementaryInfoRepository;
import com.jiangxue.waxberry.manager.agent.entity.AgentSupplementaryInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class AgentSupplementaryInfoServiceImpl implements AgentSupplementaryInfoService {

    @Autowired
    private AgentSupplementaryInfoRepository agentSupplementaryInfoRepository;

    @Autowired
    private AgentRepository agentRepository;


    @Override
    public AgentSupplementaryInfo addOrUpdate(JSONObject json) {
        if(Objects.isNull(json)){
            throw new BizException("参数不能为空");
        }

        if(Objects.isNull(json.getString("waxberryId"))){
            throw new BizException("智能体ID不能为空");
        }

        Optional<Agent> agent = agentRepository.findById(json.getString("waxberryId"));
        if(!agent.isPresent()){
            throw new BizException("智能体不存在");
        }
        Agent newAgent = agent.get();
        if(!StringUtils.isEmpty(json.getString("waxberryName"))){
            newAgent.setName(agent.get().getName());
        }

        if(!StringUtils.isEmpty(json.getString("waxberryDesc"))){
            newAgent.setDiscription(agent.get().getDiscription());
        }
        //更新智能体相关信息
        agentRepository.save(newAgent);

        AgentSupplementaryInfo agentSupplementaryInfo = agentSupplementaryInfoRepository.findByWaxberryId(json.getString("waxberryId"));
        String userId = SecurityUtils.requireCurrentUserId();
        if(Objects.isNull(agentSupplementaryInfo)) {
            AgentSupplementaryInfo agentSupplementaryInfoNew = new AgentSupplementaryInfo();
            agentSupplementaryInfoNew.setWaxberryId(json.getString("waxberryId"));
            agentSupplementaryInfoNew.setUseToolBox(ObjectUtils.isEmpty(json.getString("useToolBox"))? WaxberryConstant.AgentUseToolBox.USE:json.getString("useToolBox"));
            agentSupplementaryInfoNew.setImpose(json.getString("impose"));
            agentSupplementaryInfoNew.setRoleInstruction(json.getString("roleInstruction"));
            agentSupplementaryInfoNew.setPrologue(json.getString("prologue"));
            agentSupplementaryInfoNew.setRecommendedQuestionOne(json.getString("recommendedQuestionOne"));
            agentSupplementaryInfoNew.setRecommendedQuestionTwo(json.getString("recommendedQuestionTwo"));
            agentSupplementaryInfoNew.setRecommendedQuestionThree(json.getString("recommendedQuestionThree"));
            agentSupplementaryInfoNew.setCreatorId(userId);
            agentSupplementaryInfoNew.setUpdaterId(userId);
            agentSupplementaryInfoNew.setCreateTime(new Date());
            agentSupplementaryInfoNew.setUpdateTime(new Date());
            return agentSupplementaryInfoRepository.save(agentSupplementaryInfoNew);
        }else{
            if(!StringUtils.isEmpty(json.getString("useToolBox"))){
                agentSupplementaryInfo.setUseToolBox(json.getString("useToolBox"));
            }
            if(!StringUtils.isEmpty(json.getString("roleInstruction"))){
                agentSupplementaryInfo.setRoleInstruction(json.getString("roleInstruction"));
            }
            if(!StringUtils.isEmpty(json.getString("prologue"))){
                agentSupplementaryInfo.setPrologue(json.getString("prologue"));
            }
            agentSupplementaryInfo.setRecommendedQuestionOne(json.getString("recommendedQuestionOne"));
            agentSupplementaryInfo.setRecommendedQuestionTwo(json.getString("recommendedQuestionTwo"));
            agentSupplementaryInfo.setRecommendedQuestionThree(json.getString("recommendedQuestionThree"));
            if(!StringUtils.isEmpty(json.getString("impose"))){
                agentSupplementaryInfo.setImpose(json.getString("impose"));
            }
            agentSupplementaryInfo.setUpdaterId(userId);
            agentSupplementaryInfo.setUpdateTime(new Date());
            return agentSupplementaryInfoRepository.save(agentSupplementaryInfo);
        }
    }

    @Override
    public AgentSupplementaryInfo findByWaxberryId(String waxberryId) {
        return agentSupplementaryInfoRepository.findByWaxberryId(waxberryId);
    }

    @Override
    public AgentSupplementaryInfo findById(String id) {
        return agentSupplementaryInfoRepository.findById(id).orElse(null);
    }



}
