package com.jiangxue.waxberry.manager.baseMiniMode.controller;


import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.agent.dto.BaseMiniModelFileDTO;
import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelFile;
import com.jiangxue.waxberry.manager.baseMiniMode.service.BaseMiniModelFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "基础模型文件", description = "基础模型文件管理接口")
@RestController
@RequestMapping("/mgr/baseMinModel/file/")
public class BaseMiniModelFileController {

    @Autowired
    private BaseMiniModelFileService baseMiniModelFileService;

    @Operation(summary = "保存文件", description = "保存工业小模型文件")
    @PostMapping("/saveFile")
    public ResponseEntity<ApiResult<BaseMiniModelFile>> saveFile(
            @Parameter(description = "文件信息") @RequestBody JSONObject json) {
        return ResponseEntity.ok(ApiResult.success(baseMiniModelFileService.saveFile(json)));
    }

    @Operation(summary = "查询文件", description = "根据Waxberry ID查询文件列表")
    @GetMapping("/findByWaxberryId")
    public ResponseEntity<ApiResult<List<BaseMiniModelFileDTO>>> findById(
            @Parameter(description = "Waxberry ID") @RequestParam("waxberryId") String waxberryId) {
        return ResponseEntity.ok(ApiResult.success(baseMiniModelFileService.findByWaxberryId(waxberryId)));
    }

    @Operation(summary = "删除文件", description = "删除指定的基础模型文件")
    @PostMapping("/deleteFile")
    public ResponseEntity<ApiResult> deleteFile(
            @Parameter(description = "文件信息") @RequestBody JSONObject json) {
        baseMiniModelFileService.deleteFile(json);
        return ResponseEntity.ok(ApiResult.success());
    }

}
