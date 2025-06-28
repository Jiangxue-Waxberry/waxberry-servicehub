package com.jiangxue.waxberry.manager.agent.service;


import com.alibaba.fastjson.JSONObject;
import com.jiangxue.waxberry.manager.agent.dto.AgentRankDTO;
import com.jiangxue.waxberry.manager.agent.dto.AgentRunCountDTO;
import com.jiangxue.waxberry.manager.agent.dto.AgentDTO;
import com.jiangxue.waxberry.manager.agent.entity.*;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface AgentService {


     Agent addOrUpdateAgent(Agent agent);

     Agent addAgent(Agent agent);

     Page<AgentDTO> findByClassificationIdAndCreatorId(String classificationId, Integer type, int pageNo, int pageSize,String isAscending);


     Page<AgentDTO> findByStatusAndGroupId(String groupId, String isAscending, String name, int pageNo, int pageSize, String creatorId, Integer type);


     void deleteAgentById(String id);

     Optional<Agent> findById(String id);

     Integer getMaxSortOrder();

     Integer getMaxSortOrderByClassificationId(String id);


     List<Agent> findAll(String isAscending,String name,int pageNo,int pageSize);

     AgentDTO findOneById(String id);

     Optional<Agent> findByVesselId(String vesselId);

     AgentRun save(AgentRun runMarketAgent);

     AgentRun findByUserIdAndAgentId(String userId,String agentId);

     Page<AgentDTO> findAgentByRunAgent(String classificationId, Integer type, int pageNo, int pageSize);


     void deleteLikeAgent(String id);

     void deleteCollectAgent(String id);

     AgentLike saveLikeAgent(AgentLike agentLike);

     AgentCollect saveCollectAgent(AgentCollect collectAgent);

     AgentLike findLikeAgentByUserIdAndAgentId(String userId,String agentId);

     AgentCollect findCollectAgentByUserIdAndAgentId(String userId,String agentId);

     void uploadAgentFile(JSONObject json) throws Exception;

     Map<String, List<AgentRankDTO>> getAgentRankings(int collectLimit, int likeLimit);

     List<AgentRunCountDTO> findMarketAgentRunCountByAgentId(String agentIds);

     Integer countAgentByCreatorId();

     Integer countUserAgentNum();


}
