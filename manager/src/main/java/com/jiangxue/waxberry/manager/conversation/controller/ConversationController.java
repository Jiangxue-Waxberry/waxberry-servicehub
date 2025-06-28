package com.jiangxue.waxberry.manager.conversation.controller;

import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.conversation.dto.ConversationDto;
import com.jiangxue.waxberry.manager.conversation.entity.Conversation;
import com.jiangxue.waxberry.manager.conversation.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Tag(name = "会话管理", description = "会话相关接口")
@RequestMapping("/mgr/conversation/")
@RestController
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Operation(summary = "获取会话列表", description = "根据用户获取会话列表")
    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<ConversationDto>>> list(
            @Parameter(description = "页码，从0开始") @RequestParam(name="pageNo",required = false,defaultValue = "0") int pageNo,
            @Parameter(description = "每页大小") @RequestParam(name="pageSize",required = false,defaultValue = "5") int pageSize,
            @Parameter(description = "聊天类型") @RequestParam(name="chatType") String chatType) {
        List<ConversationDto> list = conversationService.listByUser(pageNo,pageSize,chatType);
        return ResponseEntity.ok(ApiResult.success(list));
    }

    @Operation(summary = "新增会话", description = "创建新的会话")
    @PostMapping("/add")
    public ResponseEntity<ApiResult<Conversation>> add(
            @Parameter(description = "会话信息") @RequestBody @Validated Conversation conversation) {
        return ResponseEntity.ok(ApiResult.success(conversationService.add(conversation)));
    }

    @Operation(summary = "编辑会话", description = "修改现有会话信息")
    @PostMapping("/edit")
    public ResponseEntity<ApiResult<Conversation>> edit(
            @Parameter(description = "会话信息") @RequestBody @Validated Conversation conversation) {
        return ResponseEntity.ok(ApiResult.success(conversationService.edit(conversation)));
    }

    @Operation(summary = "删除会话", description = "删除指定的会话")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult> delete(
            @Parameter(description = "会话ID信息") @RequestBody Map<String,String> map) {
        conversationService.delete(map);
        return ResponseEntity.ok(ApiResult.success());
    }

    @Operation(summary = "查询会话", description = "根据ID查询会话信息")
    @GetMapping("/getConversationById")
    public ResponseEntity<ApiResult<Conversation>> getConversationById(
            @Parameter(description = "会话ID") @RequestParam(name="id") String id) {
        Conversation conversation = conversationService.getConversationById(id);
        return ResponseEntity.ok(ApiResult.success(conversation));
    }

}
