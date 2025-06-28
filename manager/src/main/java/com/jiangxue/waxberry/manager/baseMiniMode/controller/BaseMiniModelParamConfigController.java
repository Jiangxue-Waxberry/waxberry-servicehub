package com.jiangxue.waxberry.manager.baseMiniMode.controller;



import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelParamConfig;
import com.jiangxue.waxberry.manager.baseMiniMode.service.BaseMiniModelParamConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "基础模型参数配置", description = "基础模型参数配置管理接口")
@RestController
@RequestMapping("/mgr/baseMinModel/paramConfig/")
public class BaseMiniModelParamConfigController {

    @Autowired
    private BaseMiniModelParamConfigService baseMiniModelParamConfigService;

    @Operation(summary = "批量保存", description = "批量保存基础模型参数配置")
    @PostMapping("/saveAll")
    public ResponseEntity<ApiResult<List<BaseMiniModelParamConfig>>> saveAll(
            @Parameter(description = "参数配置列表") @RequestBody List<BaseMiniModelParamConfig> configs) {
        return ResponseEntity.ok(ApiResult.success(baseMiniModelParamConfigService.saveAll(configs)));
    }

    @Operation(summary = "根据ID查询", description = "根据Waxberry ID查询参数配置列表")
    @GetMapping("/{waxberryId}")
    public ResponseEntity<ApiResult<List<BaseMiniModelParamConfig>>> findById(
            @Parameter(description = "Waxberry ID") @PathVariable String waxberryId) {
        return ResponseEntity.ok(ApiResult.success(baseMiniModelParamConfigService.findById(waxberryId)));
    }

    @Operation(summary = "查询所有", description = "查询所有基础模型参数配置")
    @GetMapping("/findAll")
    public ResponseEntity<ApiResult<List<BaseMiniModelParamConfig>>> findAll() {
        return ResponseEntity.ok(ApiResult.success(baseMiniModelParamConfigService.findAll()));
    }

}
