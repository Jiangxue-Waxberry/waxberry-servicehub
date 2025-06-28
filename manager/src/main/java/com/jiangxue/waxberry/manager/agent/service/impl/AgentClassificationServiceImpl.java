package com.jiangxue.waxberry.manager.agent.service.impl;

import com.jiangxue.waxberry.manager.agent.dto.AgentClassificationDTO;
import com.jiangxue.waxberry.manager.agent.entity.AgentClassification;
import com.jiangxue.waxberry.manager.agent.repository.AgentClassificationRepository;
import com.jiangxue.waxberry.manager.agent.service.AgentClassificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class AgentClassificationServiceImpl implements AgentClassificationService {


    @Autowired
    private AgentClassificationRepository agentClassificationRepository;

    @Override
    public AgentClassification addOrUpdateAgentClassification(AgentClassification appMenu) {
       return agentClassificationRepository.save(appMenu);
    }



    @Override
    public void deleteAgentClassification(String id) {
        agentClassificationRepository.deleteById(id);
    }



    @Override
    public AgentClassification findById(String id) {
        Optional<AgentClassification> optional = agentClassificationRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }else{
            return null;
        }
    }

    @Override
    public Integer getMaxSortOrder() {
        return agentClassificationRepository.getMaxSortOrder();
    }

    @Override
    public List<AgentClassificationDTO> findAllByParentId() {
        List<AgentClassificationDTO> dtoList = agentClassificationRepository.findAllByParentId("0");
        pushList(dtoList);
        return dtoList;
    }

    public void pushList(List<AgentClassificationDTO> list) {
        for (AgentClassificationDTO agentGroupDTO : list) {
            List<AgentClassificationDTO> dtoList = agentClassificationRepository.findAllByParentId(agentGroupDTO.getId());
            if(dtoList.size()>0){
                agentGroupDTO.setChildren(dtoList);
                pushList(agentGroupDTO.getChildren());
            }
        }
    }
}
