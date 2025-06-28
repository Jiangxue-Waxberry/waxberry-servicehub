package com.jiangxue.waxberry.manager.agent.controller;

import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.agent.dto.AgentDTO;
import com.jiangxue.waxberry.manager.agent.service.AgentCollectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Agent Collect", description = "Agent收藏管理接口")
@RestController
@RequestMapping("/mgr/agent/agentCollect")
public class AgentCollectController {

	@Autowired
	private AgentCollectService agentCollectService;

	/**
	 * 描述 : 获取当前用户收藏的agent列表
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "获取收藏列表", description = "分页获取当前用户收藏的agent列表")
	@GetMapping("/findAgentCollectList")
	public ResponseEntity<ApiResult<Page<AgentDTO>>> findAgentCollectList(
			@Parameter(description = "类型") @RequestParam(name="type",required = false) Integer type,
			@Parameter(description = "页码") @RequestParam(name="pageNo",required = false,defaultValue = "0") int pageNo,
			@Parameter(description = "每页大小") @RequestParam(name="pageSize",required = false,defaultValue = "5") int pageSize) {
		return ResponseEntity.ok(ApiResult.success(agentCollectService.findAgentCollectList(type,pageNo, pageSize)));
	}

}
