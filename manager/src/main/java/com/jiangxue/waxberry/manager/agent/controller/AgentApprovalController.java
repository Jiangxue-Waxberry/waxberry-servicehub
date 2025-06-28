package com.jiangxue.waxberry.manager.agent.controller;


import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.agent.dto.AgentApprovalDTO;
import com.jiangxue.waxberry.manager.agent.service.AgentApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Agent Approval", description = "Agent审批管理接口")
@RestController
@RequestMapping("/mgr/agent/agentApproval")
public class AgentApprovalController {

    @Autowired
    private AgentApprovalService agentApprovalService;

    @Operation(summary = "获取AgentApproval", description = "获取所有的AgentApproval列表")
    @GetMapping("/findAgentApprovalList")
    public ResponseEntity<ApiResult<Page<AgentApprovalDTO>>> findAgentApprovalList(
            @Parameter(description = "agent名称") @RequestParam(name = "name",required = false) String name,
            @Parameter(description = "审核状态") @RequestParam(name = "approvalStatus",required = false,defaultValue = "PROCESS,PASS,REFUSE") String approvalStatus,
            @Parameter(description = "用户类型") @RequestParam(name = "userType",required = false,defaultValue = "ADMIN,PERSONAL,ENTERPRISE,COLLEGE") String userType,
            @Parameter(description = "agent类型") @RequestParam(name = "agentType",required = false,defaultValue = "0,1") String agentType,
            @Parameter(description = "排序") @RequestParam(name = "sort",required = false,defaultValue = "0") Integer sort,
            @Parameter(description = "排序字段") @RequestParam(name = "sortField",required = false,defaultValue = "approvalCreateTime") String sortField,
            @Parameter(description = "页码") @RequestParam(name="pageNo",required = false,defaultValue = "1") int pageNo,
            @Parameter(description = "每页大小") @RequestParam(name="pageSize",required = false,defaultValue = "5") int pageSize
    ){
        return ResponseEntity.ok(ApiResult.success(agentApprovalService.findAgentApprovalList(approvalStatus,pageNo,pageSize,sort,sortField,userType,agentType,name)));
    }

    @Operation(summary = "更新AgentApproval", description = "更新AgentApproval")
    @PostMapping("/updateAgentApproval")
    public ResponseEntity<ApiResult> updateAgentApproval(
            @RequestBody JSONObject json
    ){
        if(ObjectUtils.isEmpty(json.getString("id"))){
            return ResponseEntity.ok(ApiResult.error("参数id不可为空"));
        }
        if(ObjectUtils.isEmpty(json.getString("status"))){
            return ResponseEntity.ok(ApiResult.error("参数status不可为空"));
        }
        agentApprovalService.updateAgentApproval(json.getString("id"),json.getString("status"),json.getString("approvalLanguage"));
        return ResponseEntity.ok(ApiResult.success());
    }

}
