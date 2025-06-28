package com.jiangxue.waxberry.manager.agent.controller;



import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.agent.dto.AgentDTO;
import com.jiangxue.waxberry.manager.agent.service.AgentLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Agent点赞", description = "Agent点赞管理接口")
@RestController
@RequestMapping("/mgr/agent/agentLike")
public class AgentLikeController {

	@Autowired
	private AgentLikeService agentLikeService;


	/**
	 * 描述 : 获取当前用户喜欢的agent列表
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "获取点赞列表", description = "获取当前用户喜欢的agent列表")
	@GetMapping("/findAgentLikeList")
	public ResponseEntity<ApiResult<Page<AgentDTO>>> findLikeAgentList(
			@Parameter(description = "Agent类型") @RequestParam(name="type",required = false) Integer type,
			@Parameter(description = "页码") @RequestParam(name="pageNo",required = false,defaultValue = "0") int pageNo,
			@Parameter(description = "每页大小") @RequestParam(name="pageSize",required = false,defaultValue = "5") int pageSize) {
		return ResponseEntity.ok(ApiResult.success(agentLikeService.findLikeAgentList(type,pageNo, pageSize)));
	}

}
