package com.jiangxue.waxberry.manager.redemptionCode.controller;

import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.redemptionCode.service.RedemptionCodeService;
import com.jiangxue.waxberry.manager.redemptionCode.utils.GenerateCodeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Tag(name = "Redemption Code", description = "兑换码管理接口")
@RestController
@RequestMapping("/mgr/redemptionCode")
public class RedemptionCodeController {

    @Autowired
    private RedemptionCodeService redemptionCodeService;

    /**
     * 描述 : 兑换兑换码
     * @param
     * @return {@link ResponseEntity}
     * @throws
     */
    @Operation(summary = "使用兑换码", description = "使用兑换码")
    @PostMapping("/activationRedemptionCode")
    public ResponseEntity<ApiResult<Boolean>> activationRedemptionCode(
            @RequestBody JSONObject json
    ) {

        if(ObjectUtils.isEmpty(json.getString("code"))){
            return ResponseEntity.ok(ApiResult.error("参数code不可为空"));
        }
        String code = json.getString("code");
        if(code.length()==12&& GenerateCodeUtils.validateCode(code.substring(2,code.length()))){
            return ResponseEntity.ok(ApiResult.success(redemptionCodeService.activationRedemptionCode(code)));
        }else{
            return ResponseEntity.ok(ApiResult.error("验证码无效"));
        }
    }



}
