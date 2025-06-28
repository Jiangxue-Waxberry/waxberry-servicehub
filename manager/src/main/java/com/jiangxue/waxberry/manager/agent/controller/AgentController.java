package com.jiangxue.waxberry.manager.agent.controller;

import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.agent.constant.WaxberryConstant;
import com.jiangxue.waxberry.manager.agent.dto.AgentRankDTO;
import com.jiangxue.waxberry.manager.agent.dto.AgentRunCountDTO;
import com.jiangxue.waxberry.manager.agent.dto.AgentDTO;
import com.jiangxue.waxberry.manager.agent.entity.*;
import com.jiangxue.waxberry.manager.agent.repository.AgentClassificationRepository;
import com.jiangxue.waxberry.manager.agent.service.AgentApprovalService;
import com.jiangxue.waxberry.manager.agent.service.AgentService;
import com.jiangxue.waxberry.manager.conversation.entity.Conversation;
import com.jiangxue.waxberry.manager.conversation.entity.ConversationMessage;
import com.jiangxue.waxberry.manager.conversation.service.ConversationMessageService;
import com.jiangxue.waxberry.manager.conversation.service.ConversationService;
import com.jiangxue.waxberry.manager.util.HttpClientUtils;
import com.jiangxue.waxberry.manager.util.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Tag(name = "Agent", description = "Agent管理接口")
@RestController
@RequestMapping("/mgr/agent/agent")
public class AgentController {

	private static final String DEFAULT_DOCKER_IMAGE_NAME = "waxberry:latest";
	private static final String DEFAULT_DOCKER_IMAGE_PORT = "80";

	@Autowired
	private AgentService agentService;

	@Autowired
	private AgentApprovalService agentApprovalService;

	@Autowired
	private ConversationService conversationService;

	@Autowired
	private ConversationMessageService conversationMessageService;

	@Autowired
	private AgentClassificationRepository classificationRepository;

	@Autowired
	private RedisService redisService;


	@Operation(summary = "获取分类下的Agent", description = "获取指定分类下的所有agent列表")
	@GetMapping("/findAllAgentByClassificationId")
	public ResponseEntity<ApiResult<Page<AgentDTO>>> findAllAgentByClassificationId(
			@Parameter(description = "分类ID") @RequestParam(name = "cid",required = false,defaultValue = "null") String cid,
			@Parameter(description = "排序") @RequestParam(name = "isAscending",required = false,defaultValue = "true") String isAscending,
			@Parameter(description = "类型") @RequestParam(name = "type",required = false) Integer type,
			@Parameter(description = "页码") @RequestParam(name="pageNo",required = false,defaultValue = "0") int pageNo,
			@Parameter(description = "每页大小") @RequestParam(name="pageSize",required = false,defaultValue = "5") int pageSize) {
		return ResponseEntity.ok(ApiResult.success(agentService.findByClassificationIdAndCreatorId(cid.equals("null")?null:cid,type,pageNo,pageSize,isAscending)));
	}

	@Operation(summary = "获取已发布的Agent", description = "根据条件获取已发布的agent列表")
	@GetMapping("/findByStatusAndGroupId")
	public ResponseEntity<ApiResult<Page<AgentDTO>>> findByStatusAndGroupId(
			@Parameter(description = "是否升序") @RequestParam(name = "isAscending",required = false,defaultValue = "true") String isAscending,
			@Parameter(description = "名称") @RequestParam(name = "name",required = false) String name,
			@Parameter(description = "分组ID") @RequestParam(name = "groupId",required = false) String groupId,
			@Parameter(description = "页码") @RequestParam(name="pageNo",required = false,defaultValue = "0") int pageNo,
			@Parameter(description = "每页大小") @RequestParam(name="pageSize",required = false,defaultValue = "5") int pageSize,
			@Parameter(description = "创建者ID") @RequestParam(name = "creatorId",required = false) String creatorId,
			@Parameter(description = "类型") @RequestParam(name = "type",required = false) Integer type) {
		return ResponseEntity.ok(ApiResult.success(agentService.findByStatusAndGroupId(ObjectUtils.isEmpty(groupId)?null:groupId,isAscending,
				ObjectUtils.isEmpty(name)?null:name,pageNo,pageSize,creatorId,type)));
	}

	@Operation(summary = "删除Agent", description = "根据ID删除指定的agent")
	@GetMapping("/deleteAgent")
	public ResponseEntity<ApiResult> deleteAgent(
			@Parameter(description = "Agent ID") @RequestParam(name = "id") String id) {
		Optional<Agent> marketAgent = agentService.findById(id);
		if(marketAgent.isPresent()){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("containerIds", new String[]{marketAgent.get().getVesselId()});
			JSONObject jsonObject = HttpClientUtils.sendPostJson(System.getProperty("sandBoxUrl")+"/sandboxes/batch_delete", params);
			agentService.deleteAgentById(id);
		}
		return ResponseEntity.ok(ApiResult.success());
	}

	@Operation(summary = "新增Agent", description = "创建新的agent")
	@PostMapping("/addAgent")
	public ResponseEntity<ApiResult<Agent>> addAgent(
			@Parameter(description = "Agent信息") @RequestBody JSONObject json) {
		Integer agentNum = agentService.countAgentByCreatorId();
		Integer createAgentNum = agentService.countUserAgentNum();
		if(createAgentNum>agentNum){
			String userId = SecurityUtils.requireCurrentUserId();
			json.put("creatorId",userId);
			Agent agent = agentService.addAgent(json.toJavaObject(Agent.class));
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("wbId", agent.getId());
			params.put("userId", userId);
			params.put("name", agent.getId());
			params.put("image", ObjectUtils.isEmpty(json.getString("image"))?DEFAULT_DOCKER_IMAGE_NAME:json.getString("image"));
			params.put("exposePorts", new String[]{DEFAULT_DOCKER_IMAGE_PORT});
			JSONObject jsonObject = HttpClientUtils.sendPostJson(System.getProperty("sandBoxUrl")+"/sandboxes", params);
			if(!ObjectUtils.isEmpty(jsonObject)&&jsonObject.getBoolean("success")){
				JSONObject object = jsonObject.getJSONObject("data");
				agent.setVesselId(object.getString("containerId"));
				agent.setCreatorId(userId);
				agent.setSortOrder(agentService.getMaxSortOrder()+1);
				Integer ports = object.getJSONObject("ports").getJSONArray("80").getInteger(0);
				agent.setVesselPort(ports);
			}else{
				agentService.deleteAgentById(agent.getId());
				return ResponseEntity.ok(ApiResult.error("方法异常"));
			}
			return ResponseEntity.ok(ApiResult.success(agentService.addAgent(agent)));
		}
		return ResponseEntity.ok(ApiResult.error("超过用户可创建数量"));
	}

	@Operation(summary = "移动Agent", description = "移动agent到指定分类")
	@PostMapping("/moveAgent")
	public ResponseEntity<ApiResult<Agent>> moveAgent(
			@Parameter(description = "Agent信息") @RequestBody JSONObject json) {
		Agent anget = json.toJavaObject(Agent.class);
		if(!ObjectUtils.isEmpty(anget.getId())){
			Optional<Agent> optionalAgent = agentService.findById(anget.getId());
			if(optionalAgent.isPresent()){
				Agent newAgent = optionalAgent.get();
				newAgent.setClassificationId(anget.getClassificationId());
				anget = newAgent;
			}
		}
		return ResponseEntity.ok(ApiResult.success(agentService.addAgent(anget)));
	}

	@Operation(summary = "更新Agent", description = "更新agent信息")
	@PostMapping("/updateAgent")
	public ResponseEntity<ApiResult<Agent>> updateAgent(
			@Parameter(description = "Agent信息") @RequestBody JSONObject json) {
		Agent agent = json.toJavaObject(Agent.class);
		if(!ObjectUtils.isEmpty(agent.getId())){
			Optional<Agent> optionalMarketApp = agentService.findById(agent.getId());
			if(optionalMarketApp.isPresent()){
				Agent newAgent = optionalMarketApp.get();
				newAgent.setUpdateTime(new Date());
				if(!ObjectUtils.isEmpty(json.getString("name"))){
					newAgent.setName(json.getString("name"));
				}
				if(!ObjectUtils.isEmpty(json.getString("discription"))){
					newAgent.setDiscription(json.getString("discription"));
				}
				if(!ObjectUtils.isEmpty(json.getString("classificationId"))){
					newAgent.setClassificationId(json.getString("classificationId"));
				}
				if(!ObjectUtils.isEmpty(json.getString("fileId"))){
					newAgent.setFileId(json.getString("fileId"));
				}
				if(!ObjectUtils.isEmpty(json.getString("fileName"))){
					newAgent.setFileName(json.getString("fileName"));
				}
				if(!ObjectUtils.isEmpty(json.getString("status"))){
					if(newAgent.getStatus() !=1&&json.getIntValue("status") == 1){
						//审核表发送
						newAgent.setStatus(json.getIntValue("status"));
						AgentApproval al = new AgentApproval();
						al.setApprovalCode(redisService.generateCode("auth","RA"));
						al.setAgentId(agent.getId());
						al.setAgentName(agent.getName());
						al.setCreator(SecurityUtils.requireCurrentUserId());
						al.setMobile(SecurityUtils.getCurrentUserMobile().orElse(null));
						agentApprovalService.addAgentApproval(al);
					}else{
                        newAgent.setStatus(json.getIntValue("status"));
                    }
				}
				if(!ObjectUtils.isEmpty(json.getIntValue("ismodify"))){
					newAgent.setIsmodify(json.getIntValue("ismodify"));
				}
				if(!ObjectUtils.isEmpty(json.getString("imgeFileId"))){
					newAgent.setImgeFileId(json.getString("imgeFileId"));
				}
				if(!ObjectUtils.isEmpty(json.getString("groupId"))){
					newAgent.setGroupId(json.getString("groupId"));
				}
				if(!ObjectUtils.isEmpty(json.getString("coverFileId"))){
					newAgent.setCoverFileId(json.getString("coverFileId"));
				}
				if(!ObjectUtils.isEmpty(json.getString("agentLabel"))){
					newAgent.setAgentLabel(json.getString("agentLabel"));
				}
				if(!ObjectUtils.isEmpty(json.getString("step"))){
					newAgent.setStep(json.getString("step"));
				}
				agent.setUpdateTime(new Date());
				agent = newAgent;
			}
		}
		return ResponseEntity.ok(ApiResult.success(agentService.addOrUpdateAgent(agent)));
	}

	@Operation(summary = "查询单个Agent", description = "根据ID查询单个agent信息")
	@GetMapping("/findById")
	public ResponseEntity<ApiResult<AgentDTO>> findById(
			@Parameter(description = "Agent ID") @RequestParam(name = "id") String id) {
		return ResponseEntity.ok(ApiResult.success(agentService.findOneById(id)));
	}

	@Operation(summary = "获取Agent列表", description = "分页获取agent列表")
	@GetMapping("/findAll")
	public ResponseEntity<ApiResult<List<Agent>>> findAll(
			@Parameter(description = "是否升序") @RequestParam(name = "isAscending",required = false,defaultValue = "true") String isAscending,
			@Parameter(description = "名称") @RequestParam(name = "name",required = false) String name,
			@Parameter(description = "页码") @RequestParam(name="pageNo",required = false,defaultValue = "0") int pageNo,
			@Parameter(description = "每页大小") @RequestParam(name="pageSize",required = false,defaultValue = "5") int pageSize) {
		return ResponseEntity.ok(ApiResult.success(agentService.findAll(isAscending,ObjectUtils.isEmpty(name)?null:name,pageNo,pageSize)));
	}


	@Operation(summary = "获取使用过的Agent", description = "获取用户使用过的agent列表")
	@GetMapping("/findAgentByRunAgent")
	public ResponseEntity<ApiResult<Page<AgentDTO>>> findAgentByRunAgent(
			@Parameter(description = "分类ID") @RequestParam(name = "classificationId",required = false,defaultValue = "null") String classificationId,
			@Parameter(description = "类型") @RequestParam(name="type",required = false) Integer type,
			@Parameter(description = "页码") @RequestParam(name="pageNo",required = false,defaultValue = "0") int pageNo,
			@Parameter(description = "每页大小") @RequestParam(name="pageSize",required = false,defaultValue = "5") int pageSize) {
		return ResponseEntity.ok(ApiResult.success(agentService.findAgentByRunAgent(classificationId.equals("null")?null:classificationId,type,pageNo,pageSize)));
	}

	@Operation(summary = "移动运行记录", description = "移动agent运行记录到指定分类")
	@PostMapping("/moveAgentRun")
	public ResponseEntity<ApiResult<AgentRun>> moveAgentRun(
			@Parameter(description = "运行记录信息") @RequestBody JSONObject json) {
		AgentRun AgentRun = json.toJavaObject(AgentRun.class);
		if(!ObjectUtils.isEmpty(AgentRun.getId())){
			AgentRun rmarketAgent = agentService.findByUserIdAndAgentId(SecurityUtils.requireCurrentUserId(),AgentRun.getId());
			if(!ObjectUtils.isEmpty(rmarketAgent)){
				rmarketAgent.setClassificationId(AgentRun.getClassificationId());
				AgentRun = rmarketAgent;
			}
		}
		return ResponseEntity.ok(ApiResult.success(agentService.save(AgentRun)));
	}


	@Operation(summary = "获取Agent文件树", description = "获取agent的文件树结构")
	@GetMapping("/findAgentTreeByAgentId")
	public ResponseEntity<ApiResult<JSONObject>> findAgentTreeByAgentId(
			@Parameter(description = "路径") @RequestParam(name = "path",required = false,defaultValue = "/waxberry") String path,
			@Parameter(description = "Agent ID") @RequestParam(name = "id") String id) {
		if(!ObjectUtils.isEmpty(id)){
			Optional<Agent> optionalMarketApp = agentService.findById(id);
			if(optionalMarketApp.isPresent()){
				Agent newAgent = optionalMarketApp.get();
				JSONObject jsonObject = HttpClientUtils
						.sendGet(System.getProperty("sandBoxUrl")+"/sandboxes"+"/"+newAgent.getVesselId()+"/tree?path="+path,null);
				if(!ObjectUtils.isEmpty(jsonObject)&&jsonObject.getBoolean("success")){
					return ResponseEntity.ok(ApiResult.success(jsonObject.getJSONObject("data")));
				}
			}
		}
		return ResponseEntity.ok(ApiResult.error("方法异常"));
	}

	@Operation(summary = "获取Agent日志", description = "获取agent的运行日志")
	@GetMapping("/findAgentLogs")
	public ResponseEntity<ApiResult<String>> findAgentLogs(
			@Parameter(description = "Agent ID") @RequestParam(name = "id") String id) {
		if(!ObjectUtils.isEmpty(id)){
			Optional<Agent> optionalMarketApp = agentService.findById(id);
			if(optionalMarketApp.isPresent()){
				Agent newAgent = optionalMarketApp.get();
				String url = System.getProperty("sandBoxUrl")+"/sandboxes"+"/"+newAgent.getVesselId()+"/logs?tail=all";
				return ResponseEntity.ok(ApiResult.success(HttpClientUtils.sendHttp(0, url, null, null, false, null, null)));
			}
		}
		return ResponseEntity.ok(ApiResult.error("方法异常"));
	}

	@Operation(summary = "点赞Agent", description = "对agent进行点赞操作")
	@GetMapping("/saveAgentLike")
	public ResponseEntity<ApiResult> saveAgentLike(
			@Parameter(description = "Agent ID") @RequestParam(name = "id") String id) {
		String userId = SecurityUtils.requireCurrentUserId();
		AgentLike angetLike = agentService.findLikeAgentByUserIdAndAgentId(userId,id);
		if(ObjectUtils.isEmpty(angetLike)){
			angetLike = new AgentLike();
			angetLike.setUserId(userId);
			angetLike.setAgentId(id);
			agentService.saveLikeAgent(angetLike);
		}else{
			agentService.deleteLikeAgent(angetLike.getId());
		}
		return ResponseEntity.ok(ApiResult.success());
	}

	@Operation(summary = "收藏Agent", description = "对agent进行收藏操作")
	@GetMapping("/saveAgentCollect")
	public ResponseEntity<ApiResult> saveAgentCollect(
			@Parameter(description = "Agent ID") @RequestParam(name = "id") String id) {
		String userId = SecurityUtils.requireCurrentUserId();
		AgentCollect agentCollect = agentService.findCollectAgentByUserIdAndAgentId(userId,id);
		if(ObjectUtils.isEmpty(agentCollect)){
			agentCollect = new AgentCollect();
			agentCollect.setUserId(userId);
			agentCollect.setAgentId(id);
			agentService.saveCollectAgent(agentCollect);
		}else{
			agentService.deleteCollectAgent(agentCollect.getId());
		}
		return ResponseEntity.ok(ApiResult.success());
	}

	@Operation(summary = "记录Agent运行", description = "记录agent的运行信息")
	@GetMapping("/agentRunRecord")
	public ResponseEntity<ApiResult> agentRunRecord(
			@Parameter(description = "容器ID") @RequestParam(name = "vesselId") String vesselId) {
		if(!ObjectUtils.isEmpty(vesselId)){
			String userId = SecurityUtils.requireCurrentUserId();
			Optional<Agent> optionalMarketApp = agentService.findByVesselId(vesselId);
			if(optionalMarketApp.isPresent()){
				Agent agent = optionalMarketApp.get();
				if(agent.getStatus() == Integer.valueOf(WaxberryConstant.WaxberryStatus.PUBLISH)){
					AgentRun agentRun = agentService.findByUserIdAndAgentId(userId,agent.getId());
					if(ObjectUtils.isEmpty(agentRun)){
						agentRun = new AgentRun();
						agentRun.setAgentId(agent.getId());
						agentRun.setUserId(userId);
						agentRun.setRunCount(1);
					}else{
						agentRun.setUpdateTime(new Date());
						agentRun.setRunCount(agentRun.getRunCount()+1);
					}
					agentService.save(agentRun);
				}
			}
		}
		return ResponseEntity.ok(ApiResult.success());
	}

	@Operation(summary = "上传Agent文件", description = "上传agent相关的代码文件")
	@PostMapping("/uploadAgentFile")
	public ResponseEntity<ApiResult> uploadAgentFile(
			@Parameter(description = "文件信息") @RequestBody JSONObject json) {
		try{
			agentService.uploadAgentFile(json);
			return ResponseEntity.ok(ApiResult.success());
		}catch (Exception e){
			return ResponseEntity.ok(ApiResult.error("上传失败"));
		}
	}

	@Operation(summary = "获取排行榜", description = "获取收藏和点赞数量最多的agent列表")
	@GetMapping("/topRank")
	public ResponseEntity<ApiResult<Map<String, List<AgentRankDTO>>>> topRank(
			@Parameter(description = "收藏数量限制") @RequestParam(name="collectLimit",required = false,defaultValue = "5") int collectLimit,
			@Parameter(description = "点赞数量限制") @RequestParam(name="likeLimit",required = false,defaultValue = "5") int likeLimit) {
		return ResponseEntity.ok(ApiResult.success(agentService.getAgentRankings(collectLimit, likeLimit)));
	}

	@Operation(summary = "获取运行次数", description = "根据agentId获取运行次数")
	@GetMapping("/findAgentRunCountByAgentId")
	public ResponseEntity<ApiResult<List<AgentRunCountDTO>>> findAgentRunCountByAgentId(
			@Parameter(description = "Agent ID列表") @RequestParam(name="agentIds") String agentIds) {
		return ResponseEntity.ok(ApiResult.success(agentService.findMarketAgentRunCountByAgentId(agentIds)));
	}


	@Operation(summary = "复制Agent", description = "复制指定的agent")
	@GetMapping("/copyAgent")
	public ResponseEntity<ApiResult<Agent>> copyMarketAgent(
			@Parameter(description = "Agent ID") @RequestParam(name="id") String id) {
		Optional<Agent> optionalMarketApp = agentService.findById(id);
		Integer agentNum = agentService.countAgentByCreatorId();
		Integer createAgentNum = agentService.countUserAgentNum();
		if(createAgentNum>agentNum){
			if(optionalMarketApp.isPresent()) {
				String userId = SecurityUtils.requireCurrentUserId();
				Agent newAgent = new Agent();
				Agent agent = optionalMarketApp.get();
				newAgent.setName("【复制的】-"+agent.getName());
				newAgent.setStatus(0);
				newAgent.setType(agent.getType());
				newAgent.setClassificationId(agent.getClassificationId());
				newAgent.setIsmodify(agent.getIsmodify());
				newAgent.setImgeFileId(agent.getImgeFileId());
				newAgent.setCoverFileId(agent.getCoverFileId());
				newAgent.setFileId(agent.getFileId());
				newAgent.setFileName(agent.getFileName());
				newAgent.setSortOrder(agentService.getMaxSortOrder()+1);
				newAgent.setCreatorId(userId);
				newAgent.setCreateTime(new Date());
				newAgent.setUpdateTime(new Date());
				String vesselId = agent.getVesselId();

				newAgent = agentService.addAgent(newAgent);

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("sourceId",vesselId);
				params.put("newName",newAgent.getId());
				params.put("userId",userId);
				params.put("wbId",newAgent.getId());
				params.put("pullImage",false);
				String copyUrl = System.getProperty("sandBoxUrl")+"/sandboxes/"+vesselId+"/copy";
				JSONObject jsonObject = HttpClientUtils.sendPostJson(copyUrl, params);
				if(!ObjectUtils.isEmpty(jsonObject)&&jsonObject.getBoolean("success")) {
					JSONObject object = jsonObject.getJSONObject("data");

					newAgent.setVesselId(object.getString("containerId"));
					Integer port = object.getJSONObject("ports").getJSONArray("80").getInteger(0);
					newAgent.setVesselPort(port);

					Page<ConversationMessage> conversationMessageList = conversationMessageService.getConversationMessageList(0, 1000, vesselId);
					copyConversation(object.getString("containerId"), newAgent.getName(), conversationMessageList.getContent());

					return ResponseEntity.ok(ApiResult.success(agentService.addOrUpdateAgent(newAgent)));
				}
			}
			return ResponseEntity.ok(ApiResult.error("方法异常"));
		}
		return ResponseEntity.ok(ApiResult.error("超过用户可创建数量"));
	}


	@Operation(summary = "判断agent是否超过用户创建数量", description = "判断agent是否超过用户创建数量")
	@GetMapping("/isUserCreateAgentCount")
	public ResponseEntity<ApiResult> isUserCreateAgentCount() {
		Integer agentNum = agentService.countAgentByCreatorId();
		Integer createAgentNum = agentService.countUserAgentNum();
		if(createAgentNum>agentNum){
			return ResponseEntity.ok(ApiResult.success());
		}
		return ResponseEntity.ok(ApiResult.error("超过用户可创建数量"));
	}

	@Operation(summary = "当前用户agentNum", description = "当前用户agent的创建数量")
	@GetMapping("/userAgentNum")
	public ResponseEntity<ApiResult<Map>> userAgentNum() {
		Map<String,Object> map = new HashMap<>();
		Integer hasAgentNum = agentService.countAgentByCreatorId();
		Integer canCreateNum = agentService.countUserAgentNum();
		map.put("total",canCreateNum);
		map.put("create",hasAgentNum);
		map.put("other",canCreateNum-hasAgentNum);
		return ResponseEntity.ok(ApiResult.success(map));
	}

	private void copyConversation(String conversationId,String name,List<ConversationMessage> messageList){
		Conversation conversation = new Conversation();
		conversation.setId(conversationId);
		conversation.setTitle(name);
		conversation.setChatType("8");
		conversationService.add(conversation);
		messageList.forEach(conversationMessage->{
			conversationMessage.setId(null);
			conversationMessage.setConversationId(conversationId);
			conversationMessageService.add(conversationMessage);
		});
	}

	@GetMapping("/findHomePageWaxberry")
	public ResponseEntity<ApiResult<Page<AgentDTO>>> findHomePageWaxberry() {
		Page<AgentDTO>  page = agentService.findByStatusAndGroupId(null,"true",null,0,3,null,null);
		for (AgentDTO agentDTO : page.getContent()) {
			String classificationId = agentDTO.getGroupId().split("-")[0];
			if(!ObjectUtils.isEmpty(classificationId)){
				Optional<AgentClassification> classification = classificationRepository.findById(classificationId);
				if(classification.isPresent()){
					agentDTO.setGroupId(classification.get().getName());
				}
			}
		}
		return ResponseEntity.ok(ApiResult.success(page));
	}
}
