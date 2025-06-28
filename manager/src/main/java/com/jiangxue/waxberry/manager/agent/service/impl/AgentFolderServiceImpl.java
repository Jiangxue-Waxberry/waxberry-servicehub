package com.jiangxue.waxberry.manager.agent.service.impl;


import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.agent.entity.AgentFolder;
import com.jiangxue.waxberry.manager.agent.repository.AgentFolderRepository;
import com.jiangxue.waxberry.manager.agent.service.AgentFolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class AgentFolderServiceImpl implements AgentFolderService {


    @Autowired
    private AgentFolderRepository agentFolderRepository;

    @Override
    public AgentFolder findById(String id) {
        Optional<AgentFolder> optional = agentFolderRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }else{
            return null;
        }
    }

    @Override
    public List<AgentFolder> findAll() {
        Sort sort = Sort.by("sortOrder").ascending();
        return agentFolderRepository.findAll(sort);
    }

    @Override
    public Integer getMaxSortOrder() {
        return agentFolderRepository.getMaxSortOrder();
    }

    @Override
    public AgentFolder addOrUpdateAgentFolder(AgentFolder classification) {
        return agentFolderRepository.save(classification);
    }

    @Override
    public void deleteAgentFolder(String id) {
        agentFolderRepository.deleteById(id);
    }

    @Override
    public List<AgentFolder> findAllByCreatorId(String creatorId,String type) {
        Sort sort = Sort.by("sortOrder").ascending();
        return agentFolderRepository.findAllByCreatorIdAndType(creatorId,Integer.parseInt(type),sort);
    }

    @Override
    public Integer getNameCount(String id, String name, Integer type) {
        String userId = SecurityUtils.requireCurrentUserId();
        if(ObjectUtils.isEmpty(id)){
            return agentFolderRepository.getNameCount(type,name,userId);
        }
        return agentFolderRepository.getNameCount(id,type,name,userId);
    }
}
