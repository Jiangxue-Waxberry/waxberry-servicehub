package com.jiangxue.waxberry.manager.agent.controller;



import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.agent.entity.AgentFolder;
import com.jiangxue.waxberry.manager.agent.service.AgentFolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Agent文件夹", description = "Agent文件夹管理接口")
@RestController
@RequestMapping("/mgr/agent/agentFolder")
public class AgentFolderController {

	@Autowired
	private AgentFolderService agentFolderService;


	/**
	 * 描述 : 获取当前登录用户的所有agent文件夹
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "获取用户文件夹", description = "获取当前登录用户的所有agent文件夹")
	@GetMapping("/findAllAgentFolderByCreatorId")
	public ResponseEntity<ApiResult<List<AgentFolder>>> findAllAgentFolderByCreatorId(
			@Parameter(description = "文件夹类型") @RequestParam(value = "type",required = false,defaultValue = "0") String type) {
		return ResponseEntity.ok(ApiResult.success(agentFolderService.findAllByCreatorId(SecurityUtils.requireCurrentUserId(),type)));
	}



	/**
	 * 描述 : 更新app分类
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "更新文件夹", description = "新增或更新agent文件夹信息")
	@PostMapping("/addOrUpdateAgentFolder")
	public ResponseEntity<ApiResult<AgentFolder>> addOrUpdateAgentFolder(
			@Parameter(description = "文件夹信息") @RequestBody JSONObject json) {
		Integer count = 0;
		AgentFolder AgentFolder = json.toJavaObject(AgentFolder.class);
		if(!ObjectUtils.isEmpty(AgentFolder.getId())){
			AgentFolder optionalAppMenu =  agentFolderService.findById(AgentFolder.getId());
			if(!ObjectUtils.isEmpty(optionalAppMenu)){
				if(!ObjectUtils.isEmpty(AgentFolder.getName())){
					count = agentFolderService.getNameCount(AgentFolder.getId(),json.getString("name"),AgentFolder.getType());
				}
			}
		}else{
			AgentFolder.setCreatorId(SecurityUtils.requireCurrentUserId());
			if(ObjectUtils.isEmpty(agentFolderService.getMaxSortOrder())){
				AgentFolder.setSortOrder(1);
			}else{
				AgentFolder.setSortOrder(agentFolderService.getMaxSortOrder()+1);
			}
			count = agentFolderService.getNameCount(AgentFolder.getId(),AgentFolder.getName(),AgentFolder.getType());
		}
		if(count>0){
			return ResponseEntity.ok(ApiResult.error(AgentFolder.getName()+"文件夹已存在，请重新命名"));
		}
		return ResponseEntity.ok(ApiResult.success(agentFolderService.addOrUpdateAgentFolder(AgentFolder)));
	}

	/**
	 * 描述 : 删除app分类
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "删除文件夹", description = "删除指定的agent文件夹")
	@GetMapping("/deleteAgentFolder")
	public ResponseEntity<ApiResult> deleteAgentFolder(
			@Parameter(description = "文件夹ID") @RequestParam(value = "id") String id) {
		agentFolderService.deleteAgentFolder(id);
		return ResponseEntity.ok(ApiResult.success());
	}

	/**
	 * 描述 : 查询单个agent分类
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "查询文件夹", description = "根据ID查询单个agent文件夹信息")
	@GetMapping("/findById")
	public ResponseEntity<ApiResult<AgentFolder>> findById(
			@Parameter(description = "文件夹ID") @RequestParam(value = "id") String id) {
		return ResponseEntity.ok(ApiResult.success(agentFolderService.findById(id)));
	}

}
