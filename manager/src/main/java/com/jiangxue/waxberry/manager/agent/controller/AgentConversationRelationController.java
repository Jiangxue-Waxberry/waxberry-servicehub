package com.jiangxue.waxberry.manager.agent.controller;


import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.agent.entity.AgentConversationRelation;
import com.jiangxue.waxberry.manager.agent.service.AgentConversationRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Agent会话关系", description = "Agent会话关系管理接口")
@RestController
@RequestMapping("/mgr/agent/agentConversationRelation")
public class AgentConversationRelationController {

    @Autowired
    private AgentConversationRelationService agentConversationRelationService;

    /**
     * 描述 : 新增会话关系
     * @param conversationRelation
     * @return {@link ResponseEntity}
     * @throws
     */
    @Operation(summary = "新增会话关系", description = "创建新的Agent会话关系")
    @PostMapping("/add")
    public ResponseEntity<ApiResult<AgentConversationRelation>> add(
            @Parameter(description = "会话关系信息") @RequestBody @Validated AgentConversationRelation conversationRelation) {
        return ResponseEntity.ok(ApiResult.success(agentConversationRelationService.add(conversationRelation)));
    }


    /**
     * 描述 : 获取杨梅对应的会话
     * @param
     * @return {@link ResponseEntity}
     * @throws
     */
    @Operation(summary = "获取Agent对应的会话", description = "根据Agent ID和会话类型获取对应的会话关系")
    @GetMapping("/findAgentConversationRelation")
    public ResponseEntity<ApiResult<AgentConversationRelation>> findAgentConversationRelation(
            @Parameter(description = "Agent ID") @RequestParam(name="agentId") String agentId, 
            @Parameter(description = "会话类型") @RequestParam(name="conversationType") Integer conversationType) {
        return ResponseEntity.ok(ApiResult.success(agentConversationRelationService.findAgentConversationRelation(agentId,conversationType)));
    }

}
