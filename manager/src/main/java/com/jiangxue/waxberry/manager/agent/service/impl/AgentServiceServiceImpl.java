package com.jiangxue.waxberry.manager.agent.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.framework.common.util.ObjectConvertUtil;
import com.jiangxue.waxberry.manager.agent.constant.SandboxesURLConstant;
import com.jiangxue.waxberry.manager.agent.constant.WaxberryConstant;
import com.jiangxue.waxberry.manager.agent.entity.*;
import com.jiangxue.waxberry.manager.agent.repository.*;
import com.jiangxue.waxberry.manager.agent.dto.AgentRankDTO;
import com.jiangxue.waxberry.manager.agent.dto.AgentRunCountDTO;
import com.jiangxue.waxberry.manager.agent.dto.AgentDTO;
import com.jiangxue.waxberry.manager.agent.service.AgentService;
import com.jiangxue.waxberry.manager.agent.util.AgentFileUploadUtil;
import com.jiangxue.framework.common.exception.BizException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import java.io.File;
import java.io.InputStream;
import java.util.*;


@Slf4j
@Service
public class AgentServiceServiceImpl implements AgentService {


    @Autowired
    private AgentRepository agentRepository;


    @Autowired
    private AgentRunRepository agentRunRepository;

    @Autowired
    private AgentCollectRepository agentCollectRepository;

    @Autowired
    private AgentLikeRepository agentLikeRepository;



    @PersistenceContext
    private EntityManager entityManager;



    @Override
    public Agent addOrUpdateAgent(Agent agent) {
        return agentRepository.save(agent);
    }

    @Override
    public Page<AgentDTO> findByClassificationIdAndCreatorId(String classificationId, Integer type, int pageNo, int pageSize,String isAscending) {
        Sort sort = Boolean.parseBoolean(isAscending) ? Sort.by(Sort.Direction.DESC, "createTime") : Sort.by(Sort.Direction.ASC, "createTime");
        Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
        String userId = SecurityUtils.requireCurrentUserId();
        if(ObjectUtils.isEmpty(classificationId)){
            return agentRepository.findByCreatorId(type, userId,pageable);
        }else{
            return agentRepository.findByClassificationIdAndCreatorId(classificationId,type,userId,pageable);
        }
    }

    @Override
    public void deleteAgentById(String id) {
        agentRepository.deleteById(id);
    }


    @Override
    public Optional<Agent> findById(String id) {
        return agentRepository.findById(id);
    }

    @Override
    public Integer getMaxSortOrder() {
        if(!ObjectUtils.isEmpty(agentRepository.getMaxSortOrder())){
            return agentRepository.getMaxSortOrder();
        }
        return 1;
    }

    @Override
    public Integer getMaxSortOrderByClassificationId(String id) {
        return agentRepository.getMaxSortOrderByClassificationId(id);
    }


    @Override
    public Page<AgentDTO> findByStatusAndGroupId(String groupId, String isAscending, String name, int pageNo, int pageSize, String creatorId, Integer type) {
        Sort sort = Boolean.parseBoolean(isAscending) ? Sort.by(Sort.Direction.DESC, "createTime") : Sort.by(Sort.Direction.ASC, "createTime");
        Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
        String currentUserId = null;
        Optional<String> optional = SecurityUtils.getCurrentUserId();
        if(optional.isPresent()){
            currentUserId = optional.get();
        }
        Page<AgentDTO> agents = agentRepository.findByStatusAndGroupId(WaxberryConstant.WaxberryStatus.PUBLISH,creatorId,type,groupId,name,currentUserId,pageable);
        return agents;
    }

    @Override
    public List<Agent> findAll(String isAscending,String name, int pageNo, int pageSize) {
        Sort sort = Boolean.parseBoolean(isAscending) ? Sort.by(Sort.Direction.ASC, "createTime") : Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
        Page<Agent> agents = agentRepository.findAll(name,pageable);
        return ObjectConvertUtil.convertToList(agents.getContent(), Agent.class);
    }

    @Override
    public AgentDTO findOneById(String id) {
        Optional<AgentDTO> dto = agentRepository.findOneById(id);
        String userId = SecurityUtils.requireCurrentUserId();
        if(dto.isPresent()){
            dto.get().setLikeCount(agentLikeRepository.countByAgentId(dto.get().getId()));
            dto.get().setCollectCount(agentCollectRepository.countByAgentId(dto.get().getId()));
            Long likeCount = 0L;
            Long collectCount = 0L;
            Integer isLike = agentLikeRepository.countByAgentIdAndUserId(dto.get().getId(),userId);
            Integer isCollect = agentCollectRepository.countByAgentIdAndUserId(dto.get().getId(),userId);
            if(null!=isLike){
                likeCount = Long.valueOf(isLike);
            }
            if(null!=isCollect){
                collectCount = Long.valueOf(isCollect);
            }
            dto.get().setIsLike(likeCount);
            dto.get().setIsCollect(collectCount);
            dto.get().setRunCount(agentRunRepository.getAgentRunCountByAgentId(dto.get().getId()));
            return dto.get();
        }
        return null;
    }

    @Override
    public Agent addAgent(Agent agent) {
        return agentRepository.save(agent);
    }

    @Override
    public Optional<Agent> findByVesselId(String vesselId) {
        return agentRepository.findByVesselId(vesselId);
    }


    @Override
    public AgentRun save(AgentRun runMarketAgent) {
        return agentRunRepository.save(runMarketAgent);
    }

    @Override
    public AgentRun findByUserIdAndAgentId(String userId, String agentId) {
        return agentRunRepository.findByUserIdAndAgentId( userId, agentId);
    }

    @Override
    public Page<AgentDTO> findAgentByRunAgent(String classificationId, Integer type , int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        String userId = SecurityUtils.requireCurrentUserId();
        if(ObjectUtils.isEmpty(classificationId)){
            return agentRepository.findByRunMarketAgent(type,userId,pageable);
        }else{
            return agentRepository.findByRunMarketAgent(classificationId,type,userId,pageable);
        }
    }

    @Override
    public void deleteLikeAgent(String id) {
        agentLikeRepository.deleteById(id);
    }

    @Override
    public void deleteCollectAgent(String id) {
        agentCollectRepository.deleteById(id);
    }

    @Override
    public AgentLike saveLikeAgent(AgentLike agentLike) {
        return agentLikeRepository.save(agentLike);
    }

    @Override
    public AgentCollect saveCollectAgent(AgentCollect collectAgent) {
        return agentCollectRepository.save(collectAgent);
    }

    @Override
    public AgentLike findLikeAgentByUserIdAndAgentId(String userId, String agentId) {
        return agentLikeRepository.findByUserIdAndAgentId(userId,agentId);
    }

    @Override
    public AgentCollect findCollectAgentByUserIdAndAgentId(String userId, String agentId) {
        return agentCollectRepository.findByUserIdAndAgentId(userId,agentId);
    }



    @Override
    public void uploadAgentFile(JSONObject json)  throws Exception {

        if(ObjectUtils.isEmpty(json)){
            throw new BizException("参数不能为空");
        }

        String fileId = json.getString("fileId");
        if(StringUtils.isEmpty(fileId)){
            throw new BizException("文件id不能为空");
        }

        String fileName = json.getString("fileName");
        if(StringUtils.isEmpty(fileName)){
            throw new BizException("文件名称不能为空");
        }

        String path = json.getString("path");
        if(StringUtils.isEmpty(path)){
            throw new BizException("文件路径不能为空");
        }

        String containerId = json.getString("containerId");
        if(StringUtils.isEmpty(containerId)){
            throw new BizException("容器id不能为空");
        }

        InputStream inputStream = AgentFileUploadUtil.downLoadByUrl(System.getProperty("fileServerUrl").concat("download/?ID=").concat(fileId));
        if(null != inputStream){
            String tempPath = "/mgr"+ File.separator + "temp" + File.separator;
            File file = AgentFileUploadUtil.stream2file(inputStream,tempPath + fileName);
            AgentFileUploadUtil.uploadFile(
                    System.getProperty("sandBoxUrl").concat(String.format(SandboxesURLConstant.AGENT_UPLOAD_FILE_URL, json.getString("containerId"))),
                    file,
                    json);
        }

    }

    @Override
    public Map<String, List<AgentRankDTO>> getAgentRankings(int collectLimit, int likeLimit) {

            Map<String, List<AgentRankDTO>> result = new HashMap<>();
            // 获取收藏排行
            List<AgentRankDTO> collectRanking = agentCollectRepository.findTopCollectedAgents(
                    PageRequest.of(0, collectLimit));

            // 获取点赞排行
            List<AgentRankDTO> likeRanking = agentLikeRepository.findTopLikedAgents(
                    PageRequest.of(0, likeLimit));

            result.put("collectRanking", collectRanking);
            result.put("likeRanking", likeRanking);

            return result;
    }

    @Override
    public List<AgentRunCountDTO> findMarketAgentRunCountByAgentId(String agentIds) {
        if(StringUtils.isEmpty(agentIds)){
            return null;
        }
        List<String> agentIdList = Arrays.asList(agentIds.split(","));
        List<AgentRunCountDTO> agentRunCount = agentRunRepository.getAgentRunCount(agentIdList);
        return agentRunCount;
    }

    @Override
    public Integer countAgentByCreatorId() {
        return agentRepository.countAgentByCreatorId(SecurityUtils.requireCurrentUserId());
    }

    @Override
    public Integer countUserAgentNum() {
        StringBuilder dataSQL = new StringBuilder("select create_num from auth_users t where t.id = :userId ");
        Query query = entityManager.createNativeQuery(dataSQL.toString());
        query.setParameter("userId",SecurityUtils.requireCurrentUserId());
        return (Integer) query.getSingleResult();
    }

}
