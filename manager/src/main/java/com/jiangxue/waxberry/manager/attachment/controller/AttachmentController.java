package com.jiangxue.waxberry.manager.attachment.controller;



import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.attachment.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Tag(name = "附件管理", description = "附件管理接口")
@RestController
@RequestMapping("/mgr/attachment")
public class AttachmentController {

	@Autowired
	private AttachmentService attachmentService;

	/**
	 * 描述 : 根据文件ids获取文件信息
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "获取文件信息", description = "根据文件IDs获取文件详细信息")
	@PostMapping("/getByFileIds")
	public ResponseEntity<ApiResult<Map<String,Object>>> getFileInfos(
			@Parameter(description = "文件ID列表") @RequestBody JSONObject json) {
		return ResponseEntity.ok(ApiResult.success(attachmentService.getFileInfos(json)));
	}



}
