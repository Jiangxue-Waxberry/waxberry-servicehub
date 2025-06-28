package com.jiangxue.waxberry.manager.industrialPrompt.controller;



import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.industrialPrompt.entity.IndustrialPrompt;
import com.jiangxue.waxberry.manager.industrialPrompt.service.IndustrialPromptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Tag(name = "工业提示词", description = "工业提示词管理接口")
@RestController
@RequestMapping("/mgr/industrialPrompts/")
public class IndustrialPromptController {

    @Autowired
    private IndustrialPromptService industrialPromptService;

    @Operation(summary = "保存提示词", description = "保存新的工业提示词")
    @PostMapping("/save")
    public ResponseEntity<ApiResult<IndustrialPrompt>> save(
            @Parameter(description = "提示词信息") @RequestBody IndustrialPrompt prompt) {
        if (ObjectUtils.isEmpty(prompt)) {
            return ResponseEntity.ok(ApiResult.error("参数不能为空"));
        }
        return ResponseEntity.ok(ApiResult.success(industrialPromptService.save(prompt)));
    }

    @Operation(summary = "更新提示词", description = "更新现有的工业提示词")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<IndustrialPrompt>> update(
            @Parameter(description = "提示词信息") @RequestBody IndustrialPrompt prompt) {
        if (ObjectUtils.isEmpty(prompt) || ObjectUtils.isEmpty(prompt.getId())) {
            return ResponseEntity.ok(ApiResult.error("参数不能为空"));
        }
        return ResponseEntity.ok(ApiResult.success(industrialPromptService.update(prompt)));
    }

    @Operation(summary = "查询提示词", description = "根据ID查询工业提示词")
    @GetMapping("/getById")
    public ResponseEntity<ApiResult<IndustrialPrompt>> getById(
            @Parameter(description = "提示词ID") @RequestParam(value = "id") String id) {
        if(ObjectUtils.isEmpty(id)){
            return ResponseEntity.ok(ApiResult.error("参数不能为空"));
        }
        return ResponseEntity.ok(ApiResult.success(industrialPromptService.findById(id)));
    }

    @Operation(summary = "查询所有提示词", description = "根据条件查询所有工业提示词")
    @GetMapping("/getAll")
    public ResponseEntity<ApiResult<Map<String, Object>>> getAll(
            @Parameter(description = "标题") @RequestParam(value = "title",required = false) String title,
            @Parameter(description = "排序方式") @RequestParam(value = "sort") String sort,
            @Parameter(description = "创建者ID") @RequestParam(value = "creatorId") String creatorId) {
        return ResponseEntity.ok(ApiResult.success(industrialPromptService.findAll(title,sort,creatorId)));
    }

    @Operation(summary = "删除提示词", description = "删除指定的工业提示词")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult> delete(
            @Parameter(description = "提示词ID列表") @RequestBody JSONObject jsonObject) {
        if(ObjectUtils.isEmpty(jsonObject) || StringUtils.isEmpty(jsonObject.getString("ids"))){
            return ResponseEntity.ok(ApiResult.error("参数不能为空"));
        }
        industrialPromptService.deleteById(jsonObject.getString("ids"));
        return ResponseEntity.ok(ApiResult.success());
    }
}
