package com.jiangxue.waxberry.manager.agent.controller;

import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.agent.dto.AgentClassificationDTO;
import com.jiangxue.waxberry.manager.agent.entity.AgentClassification;
import com.jiangxue.waxberry.manager.agent.service.AgentClassificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Tag(name = "Agent Classification", description = "Agent分类管理接口")
@RestController
@RequestMapping("/mgr/agent/agentClassification")
public class AgentClassificationController {

	@Autowired
	private AgentClassificationService agentClassificationService;

	/**
	 * 描述 : 获取所有app分类
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "获取所有分类", description = "获取系统中所有的Agent分类信息")
	@GetMapping("/findAllAgentClassification")
	public ResponseEntity<ApiResult<List<AgentClassificationDTO>>> findAllAgentClassification() {
		return ResponseEntity.ok(ApiResult.success(agentClassificationService.findAllByParentId()));
	}

	/**
	 * 描述 : 更新app分类
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "更新分类", description = "新增或更新Agent分类信息")
	@PostMapping("/addOrUpdateAgentClassification")
	public ResponseEntity<ApiResult<AgentClassification>> addOrUpdateAgentClassification(
			@Parameter(description = "分类信息") @RequestBody JSONObject json) {
		AgentClassification agentClassification = json.toJavaObject(AgentClassification.class);
		if(ObjectUtils.isEmpty(agentClassification.getId())){
			agentClassification.setCreatorId(SecurityUtils.requireCurrentUserId());
			if(ObjectUtils.isEmpty(agentClassificationService.getMaxSortOrder())){
				agentClassification.setSortOrder(1);
			}else{
				agentClassification.setSortOrder(agentClassificationService.getMaxSortOrder()+1);
			}
		}
		agentClassification.setUpdateTime(new Date());
		return ResponseEntity.ok(ApiResult.success(agentClassificationService.addOrUpdateAgentClassification(agentClassification)));
	}

	/**
	 * 描述 : 删除app分类
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "删除分类", description = "根据ID删除指定的Agent分类")
	@GetMapping("/deleteAgentClassification")
	public ResponseEntity<ApiResult> deleteAgentClassification(
			@Parameter(description = "分类ID") @RequestParam(value = "id") String id) {
		agentClassificationService.deleteAgentClassification(id);
		return ResponseEntity.ok(ApiResult.success());
	}

	/**
	 * 描述 : 查询单个
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "查询单个分类", description = "根据ID查询单个Agent分类信息")
	@GetMapping("/findById")
	public ResponseEntity<ApiResult<AgentClassification>> findById(
			@Parameter(description = "分类ID") @RequestParam(value = "id") String id) {
		return ResponseEntity.ok(ApiResult.success(agentClassificationService.findById(id)));
	}

}
