package com.jiangxue.waxberry.manager.agent.controller;

import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.agent.entity.AgentTask;
import com.jiangxue.waxberry.manager.agent.service.AgentTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@Tag(name = "Agent任务", description = "Agent任务管理接口")
@RestController
@RequestMapping("/mgr/agent/agentTask")
public class AgentTaskController {
    private Logger logger = LoggerFactory.getLogger(AgentTaskController.class);

    @Autowired
    private AgentTaskService agentTaskService;

    @Operation(summary = "新增任务", description = "创建新的Agent任务")
    @PostMapping("/addAgentTask")
    public ResponseEntity<ApiResult<AgentTask>> addAgentTask(
            @Parameter(description = "任务信息") @RequestBody JSONObject json){
        String userId = SecurityUtils.requireCurrentUserId();
        AgentTask agentTask = json.toJavaObject(AgentTask.class);
        agentTask.setStatus(0);
        agentTask.setCreateTime(new Date());
        agentTask.setUpdateTime(new Date());
        agentTask.setCreatorId(userId);
        agentTask.setUpdaterId(userId);
        return ResponseEntity.ok(ApiResult.success(agentTaskService.save(agentTask)));
    }

    @Operation(summary = "更新任务", description = "更新Agent任务信息")
    @PostMapping("/updateAgentTask")
    public ResponseEntity<ApiResult<AgentTask>> updateAgentTask(
            @Parameter(description = "任务信息") @RequestBody JSONObject json){
        String id = json.getString("id");
        if(!ObjectUtils.isEmpty(id)){
            AgentTask optionalMarketAgentTask =  agentTaskService.findById(id);
            if(!ObjectUtils.isEmpty(optionalMarketAgentTask)){
                AgentTask agentTask = optionalMarketAgentTask;
                if(!ObjectUtils.isEmpty(json.getString("name"))){
                    agentTask.setName(json.getString("name"));
                }
                if(!ObjectUtils.isEmpty(json.getString("content"))){
                    agentTask.setContent(json.getString("content"));
                }
                if(!ObjectUtils.isEmpty(json.getString("status"))){
                    agentTask.setStatus(json.getInteger("status"));
                }
                return ResponseEntity.ok(ApiResult.success(agentTaskService.save(agentTask)));
            }

        }
        return ResponseEntity.ok(ApiResult.error("方法异常"));
    }

    @Operation(summary = "删除任务", description = "根据ID删除指定的Agent任务")
    @GetMapping("/deleteAgentTaskById")
    public ResponseEntity<ApiResult> deleteAgentTaskById(
            @Parameter(description = "任务ID") @RequestParam(name = "id") String id) {
        agentTaskService.delete(id);
        return ResponseEntity.ok(ApiResult.success());
    }

    @Operation(summary = "删除Agent任务", description = "根据Agent ID删除相关的所有任务")
    @GetMapping("/deleteAgentTaskByAgentId")
    public ResponseEntity<ApiResult> deleteAgentTaskByAgentId(
            @Parameter(description = "Agent ID") @RequestParam(name = "agentId") String agentId) {
        agentTaskService.deleteByAgentId(agentId);
        return ResponseEntity.ok(ApiResult.success());
    }

    @Operation(summary = "获取Agent任务", description = "根据Agent ID获取相关的任务列表")
    @GetMapping("/findAgentTaskByAgentId")
    public ResponseEntity<ApiResult<Page<AgentTask>>> findAgentTaskByAgentId(
            @Parameter(description = "Agent ID") @RequestParam(name = "agentId") String agentId,
            @Parameter(description = "页码") @RequestParam(name="pageNo",required = false,defaultValue = "0") int pageNo,
            @Parameter(description = "每页大小") @RequestParam(name="pageSize",required = false,defaultValue = "5") int pageSize) {
        return ResponseEntity.ok(ApiResult.success(agentTaskService.findAgentTaskByAgentId(agentId,pageNo,pageSize)));
    }

    @Operation(summary = "查询任务", description = "根据ID查询单个Agent任务信息")
    @GetMapping("/findById")
    public ResponseEntity<ApiResult<AgentTask>> findById(
            @Parameter(description = "任务ID") @RequestParam(name = "id") String id) {
        return ResponseEntity.ok(ApiResult.success(agentTaskService.findById(id)));
    }

}
