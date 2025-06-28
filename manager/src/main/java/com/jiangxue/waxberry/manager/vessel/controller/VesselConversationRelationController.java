package com.jiangxue.waxberry.manager.vessel.controller;


import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.vessel.entity.VesselConversationRelation;
import com.jiangxue.waxberry.manager.vessel.service.VesselConversationRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "容器会话关系", description = "容器会话关系管理接口")
@RestController
@RequestMapping("/mgr/vessel/conversationRelation")
public class VesselConversationRelationController {

    @Autowired
    private VesselConversationRelationService vesselConversationRelationService;

    @Operation(summary = "新增会话关系", description = "新增容器会话关系")
    @PostMapping("/add")
    public ResponseEntity<ApiResult<VesselConversationRelation>> add(
            @Parameter(description = "会话关系信息") @RequestBody VesselConversationRelation vesselConversationRelation) {
        return ResponseEntity.ok(ApiResult.success(vesselConversationRelationService.add(vesselConversationRelation)));
    }

    @Operation(summary = "查询会话关系", description = "获取容器对应的会话关系列表")
    @GetMapping("/findConversationRelation")
    public ResponseEntity<ApiResult<List<VesselConversationRelation>>> findConversationRelation(
            @Parameter(description = "容器ID") @RequestParam(name="vesselId") String vesselId) {
        return ResponseEntity.ok(ApiResult.success(vesselConversationRelationService.findConversationRelation(vesselId)));
    }

    @Operation(summary = "删除会话关系", description = "删除容器对应的会话关系")
    @DeleteMapping("/deleteConversationRelation")
    public ResponseEntity<ApiResult> deleteConversationRelation(
            @Parameter(description = "关系ID") @RequestParam(name="id") String id) {
        vesselConversationRelationService.deleteConversationRelation(id);
        return ResponseEntity.ok(ApiResult.success());
    }

}
