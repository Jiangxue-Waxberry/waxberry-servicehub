package com.jiangxue.waxberry.manager.vessel.service;


import com.jiangxue.waxberry.manager.vessel.entity.VesselConversationRelation;

import java.util.List;


public interface VesselConversationRelationService {

    List<VesselConversationRelation> findConversationRelation(String vesselId);

    VesselConversationRelation add(VesselConversationRelation vesselConversationRelation);

    void deleteConversationRelation(String id);

}
