package com.jiangxue.waxberry.manager.baseMiniMode.controller;



import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelExtend;
import com.jiangxue.waxberry.manager.baseMiniMode.service.BaseMiniModelExtendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@Tag(name = "基础模型扩展", description = "基础模型扩展管理接口")
@RestController
@RequestMapping("/mgr/baseMinModel/extend/")
public class BaseMiniModelExtendController {

    @Autowired
    private BaseMiniModelExtendService baseMiniModelExtendService;

    @Operation(summary = "保存扩展", description = "保存或更新基础模型扩展信息")
    @PostMapping("/save")
    public ResponseEntity<ApiResult<BaseMiniModelExtend>> save(
            @Parameter(description = "模型扩展信息") @RequestBody BaseMiniModelExtend baseMiniModelExtend) {
        if(ObjectUtils.isEmpty(baseMiniModelExtend) || ObjectUtils.isEmpty(baseMiniModelExtend.getWaxberryId())){
            return ResponseEntity.ok(ApiResult.error("参数不能为空！"));
        }
        return ResponseEntity.ok(ApiResult.success(baseMiniModelExtendService.save(baseMiniModelExtend)));
    }

    @Operation(summary = "查询扩展", description = "根据Waxberry ID查询基础模型扩展信息")
    @GetMapping("/findByWaxberryId")
    public ResponseEntity<ApiResult<BaseMiniModelExtend>> findByWaxberryId(
            @Parameter(description = "Waxberry ID") @RequestParam("waxberryId") String waxberryId) {
        return ResponseEntity.ok(ApiResult.success(baseMiniModelExtendService.findByWaxberryId(waxberryId)));
    }

}
