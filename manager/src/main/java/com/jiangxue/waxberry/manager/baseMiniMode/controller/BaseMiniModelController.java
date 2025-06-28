package com.jiangxue.waxberry.manager.baseMiniMode.controller;


import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModel;
import com.jiangxue.waxberry.manager.baseMiniMode.service.BaseMiniModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "基础模型", description = "基础模型管理接口")
@RestController
@RequestMapping("/mgr/baseMinModel/model")
public class BaseMiniModelController {

    @Autowired
    private BaseMiniModelService baseMiniModelService;

    @Operation(summary = "保存模型", description = "保存或更新基础模型配置")
    @PostMapping("/save")
    public ResponseEntity<ApiResult<BaseMiniModel>> save(@Parameter(description = "模型配置信息") @RequestBody BaseMiniModel config) {
        return ResponseEntity.ok(ApiResult.success(baseMiniModelService.save(config)));
    }

    @Operation(summary = "查询模型", description = "根据ID查询基础模型信息")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<BaseMiniModel>> findById(@Parameter(description = "模型ID") @PathVariable String id) {
        return ResponseEntity.ok(ApiResult.success(baseMiniModelService.findById(id)));
    }

    @Operation(summary = "获取模型列表", description = "根据类型获取所有基础模型列表")
    @GetMapping("/getAllByType")
    public ResponseEntity<ApiResult<List<BaseMiniModel>>> findAllByType(@Parameter(description = "模型类型") @RequestParam("type") String type) {
        return ResponseEntity.ok(ApiResult.success(baseMiniModelService.findAllByType(type)));
    }
}
