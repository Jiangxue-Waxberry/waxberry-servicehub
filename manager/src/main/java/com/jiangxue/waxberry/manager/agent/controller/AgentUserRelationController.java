package com.jiangxue.waxberry.manager.agent.controller;


import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.agent.entity.Agent;
import com.jiangxue.waxberry.manager.agent.entity.AgentUserRelation;
import com.jiangxue.waxberry.manager.agent.service.AgentService;
import com.jiangxue.waxberry.manager.agent.service.AgentUserRelationService;
import com.jiangxue.waxberry.manager.util.HttpClientUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Tag(name = "Agent用户关系", description = "Agent用户关系管理接口")
@RestController
@RequestMapping("/mgr/agent/agentUserRelation")
public class AgentUserRelationController {

    @Autowired
    private AgentUserRelationService agentUserRelationService;

    @Autowired
    private AgentService agentService;

    @Operation(summary = "新增关系", description = "创建新的Agent用户关系")
    @PostMapping("/add")
    public ResponseEntity<ApiResult<AgentUserRelation>> add(
            @Parameter(description = "Agent ID") @RequestParam(name="agentId") String agentId) {
        Optional<Agent> optionalMarketApp = agentService.findById(agentId);
        if(optionalMarketApp.isPresent()) {

            String userId = SecurityUtils.requireCurrentUserId();
            Agent agent = optionalMarketApp.get();
            String vesselId = agent.getVesselId();
            //复制容器
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("sourceId",vesselId);
            params.put("userId",userId);
            params.put("wbId",agent.getId());
            params.put("pullImage",false);
            String copyUrl = System.getProperty("sandBoxUrl")+"/sandboxes/"+vesselId+"/copy";
            JSONObject jsonObject = HttpClientUtils.sendPostJson(copyUrl, params);
            if(!ObjectUtils.isEmpty(jsonObject)&&jsonObject.getBoolean("success")) {

                JSONObject object = jsonObject.getJSONObject("data");
                Integer port = object.getJSONObject("ports").getJSONArray("80").getInteger(0);

                AgentUserRelation agentUserRelation = new AgentUserRelation();
                agentUserRelation.setAgentId(agentId);
                agentUserRelation.setVesselId(object.getString("containerId"));
                agentUserRelation.setVesselPort(port);
                agentUserRelation.setCreator(userId);
                agentUserRelation.setCreateTime(new Date());
                return ResponseEntity.ok(ApiResult.success(agentUserRelationService.add(agentUserRelation)));
            }
        }
        return ResponseEntity.ok(ApiResult.error("访问异常"));
    }

    @Operation(summary = "获取用户关系", description = "获取智能体对应的用户关系")
    @GetMapping("/findUserRelation")
    public ResponseEntity<ApiResult<AgentUserRelation>> findUserRelation(
            @Parameter(description = "Agent ID") @RequestParam(name="agentId") String agentId) {
        return ResponseEntity.ok(ApiResult.success(agentUserRelationService.findUserRelation(agentId)));
    }

}
