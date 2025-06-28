package com.jiangxue.waxberry.manager.baseMiniMode.controller;



import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelDefaultParamConfig;
import com.jiangxue.waxberry.manager.baseMiniMode.service.BaseMiniModelDefaultParamConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "基础模型默认参数", description = "基础模型默认参数配置管理接口")
@RestController
@RequestMapping("/mgr/baseMinModel/defaultParamConfig/")
public class BaseMiniModelDefaultParamConfigController {

    @Autowired
    private BaseMiniModelDefaultParamConfigService baseMiniModelDefaultParamConfigService;

    @Operation(summary = "保存配置", description = "保存或更新基础模型默认参数配置")
    @PostMapping("/save")
    public ResponseEntity<ApiResult<BaseMiniModelDefaultParamConfig>> save(
            @Parameter(description = "参数配置信息") @RequestBody BaseMiniModelDefaultParamConfig config) {
        return ResponseEntity.ok(ApiResult.success(baseMiniModelDefaultParamConfigService.save(config)));
    }

    @Operation(summary = "查询配置", description = "根据ID查询基础模型默认参数配置")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<BaseMiniModelDefaultParamConfig>> findById(
            @Parameter(description = "配置ID") @PathVariable String id) {
        return ResponseEntity.ok(ApiResult.success(baseMiniModelDefaultParamConfigService.findById(id)));
    }

    @Operation(summary = "获取配置列表", description = "根据类型获取所有基础模型默认参数配置列表")
    @GetMapping("/getAllByType")
    public ResponseEntity<ApiResult<List<BaseMiniModelDefaultParamConfig>>> findAllByType(
            @Parameter(description = "配置类型") @RequestParam("type") String type) {
        return ResponseEntity.ok(ApiResult.success(baseMiniModelDefaultParamConfigService.findAllByType(type)));
    }

}
