package com.jiangxue.waxberry.manager.agent.service.impl;

import com.jiangxue.waxberry.manager.agent.entity.AgentTask;
import com.jiangxue.waxberry.manager.agent.repository.AgentTaskRepository;
import com.jiangxue.waxberry.manager.agent.service.AgentTaskService;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;



@Slf4j
@Service
@Transactional
public class AgentTaskServiceImpl implements AgentTaskService {


    @Autowired
    private AgentTaskRepository agentTaskRepository;


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AgentTask save(AgentTask agentTask) {
        return agentTaskRepository.save(agentTask);
    }

    @Override
    public void delete(String id) {
        agentTaskRepository.deleteById(id);
    }

    @Override
    public void deleteByAgentId(String agentId) {
        StringBuilder dataSQL = new StringBuilder("delete from mgr_agent_task t where t.agent_id = :agentId ");
        Query query = entityManager.createNativeQuery(dataSQL.toString());
        query.setParameter("agentId",agentId);
        query.executeUpdate();
    }

    @Override
    public AgentTask findById(String id) {
        Optional<AgentTask> optional = agentTaskRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Override
    public Page<AgentTask> findAgentTaskByAgentId(String agentId, Integer pageNo, Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
        return agentTaskRepository.findByAgentId(agentId,pageable);
    }
}
