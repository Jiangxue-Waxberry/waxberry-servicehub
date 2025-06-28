package com.jiangxue.waxberry.manager.vessel.service.impl;


import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.vessel.entity.VesselConversationRelation;
import com.jiangxue.waxberry.manager.vessel.repository.VesselConversationRelationRepository;
import com.jiangxue.waxberry.manager.vessel.service.VesselConversationRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Slf4j
@Service
@Transactional
public class VesselConversationRelationServiceImpl implements VesselConversationRelationService {

    @Autowired
    private VesselConversationRelationRepository vesselConversationRelationRepository;

    @Override
    public List<VesselConversationRelation> findConversationRelation(String vesselId) {
        return vesselConversationRelationRepository.findByVesselId(vesselId);
    }

    @Override
    public VesselConversationRelation add(VesselConversationRelation vesselConversationRelation) {
        String userId = SecurityUtils.requireCurrentUserId();
        vesselConversationRelation.setCreator(userId);
        vesselConversationRelation.setCreateTime(new Date());
        return vesselConversationRelationRepository.save(vesselConversationRelation);
    }

    @Override
    public void deleteConversationRelation(String id) {
        vesselConversationRelationRepository.deleteById(id);
    }

}
