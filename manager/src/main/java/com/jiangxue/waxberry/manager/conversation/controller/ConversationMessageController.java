package com.jiangxue.waxberry.manager.conversation.controller;


import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.conversation.entity.ConversationMessage;
import com.jiangxue.waxberry.manager.conversation.service.ConversationMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@Tag(name = "会话消息", description = "会话消息管理接口")
@RequestMapping("/mgr/conversation/message/")
@RestController
public class ConversationMessageController {

    @Autowired
    private ConversationMessageService conversationMessageService;

    @Operation(summary = "获取消息列表", description = "获取会话消息列表")
    @GetMapping("/list")
    public ResponseEntity<ApiResult<Page<ConversationMessage>>> list(
            @Parameter(description = "页码，从0开始") @RequestParam(name="pageNo",required = false,defaultValue="0") int pageNo,
            @Parameter(description = "每页大小") @RequestParam(name="pageSize",required = false,defaultValue ="10") int pageSize,
            @Parameter(description = "会话ID") @RequestParam(name="conversationId") String conversationId) {
        Page<ConversationMessage> conversationMessageList = conversationMessageService.getConversationMessageList(pageNo, pageSize, conversationId);
        return ResponseEntity.ok(ApiResult.success(conversationMessageList));
    }

    @Operation(summary = "新增消息", description = "新增会话消息")
    @PostMapping("/add")
    public ResponseEntity<ApiResult<ConversationMessage>> add(
            @Parameter(description = "消息信息") @RequestBody @Validated ConversationMessage conversationMessage) {
        return ResponseEntity.ok(ApiResult.success(conversationMessageService.add(conversationMessage)));
    }

    @Operation(summary = "删除消息", description = "删除指定的会话消息")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult> delete(
            @Parameter(description = "消息ID信息") @RequestBody Map<String,String> map) {
        conversationMessageService.delete(map);
        return ResponseEntity.ok(ApiResult.success());
    }

    @Operation(summary = "更新消息", description = "更新会话消息，用于单个删除问答")
    @PostMapping("/edit/{type}")
    public ResponseEntity<ApiResult> edit(
            @Parameter(description = "更新类型") @PathVariable String type,
            @Parameter(description = "消息信息") @RequestBody ConversationMessage conversationMessage) {
        conversationMessageService.edit(type,conversationMessage);
        return ResponseEntity.ok(ApiResult.success());
    }

    @Operation(summary = "删除会话消息", description = "删除会话下的所有消息")
    @PostMapping("/deleteByConversationId")
    public ResponseEntity<ApiResult> deleteByConversationId(
            @Parameter(description = "会话ID信息") @RequestBody Map<String,String> map) {
        conversationMessageService.deleteByConversationId(map);
        return ResponseEntity.ok(ApiResult.success());
    }
}
