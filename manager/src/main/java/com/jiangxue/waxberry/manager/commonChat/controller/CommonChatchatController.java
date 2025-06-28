package com.jiangxue.waxberry.manager.commonChat.controller;

import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.commonChat.dto.CommonChatchatDto;
import com.jiangxue.waxberry.manager.commonChat.service.CommonChatchatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "通用聊天", description = "通用聊天相关接口")
@RestController
@RequestMapping("/mgr/commonChat/")
public class CommonChatchatController {

    @Autowired
    private CommonChatchatService commonChatchatService;

    /**
     * 描述 : 获取问答接口以及参数信息接口
     * @param commonChatchatDTO
     * @return {@link ResponseEntity}
     * @throws
     */
    @Operation(summary = "获取问答接口", description = "获取问答接口以及参数信息")
    @PostMapping("functionData")
    public ResponseEntity<ApiResult<JSONObject>> source(
            @Parameter(description = "聊天请求参数") @RequestBody CommonChatchatDto commonChatchatDTO) {
        JSONObject json = commonChatchatService.functionData(commonChatchatDTO);
        return ResponseEntity.ok(ApiResult.success(json));
    }

}
