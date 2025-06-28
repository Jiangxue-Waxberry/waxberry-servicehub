package com.jiangxue.waxberry.manager.agent.controller;


import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.agent.entity.AgentSupplementaryInfo;
import com.jiangxue.waxberry.manager.agent.service.AgentSupplementaryInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Agent补充信息", description = "Agent补充信息管理接口")
@RestController
@RequestMapping("/mgr/agent/agentSupplementaryInfo")
public class AgentSupplementaryInfoController {

	@Autowired
	private AgentSupplementaryInfoService agentSupplementaryInfoService;


	/**
	 * 描述 : 新增或者更新智能体补充信息
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "更新补充信息", description = "新增或者更新智能体补充信息")
	@PostMapping("/addOrUpdate")
	public ResponseEntity<ApiResult<AgentSupplementaryInfo>> addOrUpdate(
			@Parameter(description = "补充信息") @RequestBody JSONObject json) {
		return ResponseEntity.ok(ApiResult.success(agentSupplementaryInfoService.addOrUpdate(json)));
	}
	

	/**
	 * 描述 : 查询单个信息
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "查询补充信息", description = "根据ID查询单个智能体补充信息")
	@GetMapping("/findById")
	public ResponseEntity<ApiResult<AgentSupplementaryInfo>> findById(
			@Parameter(description = "智能体ID") @RequestParam(value = "waxberryId") String waxberryId) {
		return ResponseEntity.ok(ApiResult.success(agentSupplementaryInfoService.findByWaxberryId(waxberryId)));
	}




}
