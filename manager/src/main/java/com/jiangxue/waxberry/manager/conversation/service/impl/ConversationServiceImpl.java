package com.jiangxue.waxberry.manager.conversation.service.impl;


import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.framework.common.util.ObjectConvertUtil;
import com.jiangxue.waxberry.manager.conversation.constant.ConversationConstant;
import com.jiangxue.waxberry.manager.conversation.dto.ConversationDto;
import com.jiangxue.waxberry.manager.conversation.entity.Conversation;
import com.jiangxue.waxberry.manager.conversation.repository.ConversationRepository;
import com.jiangxue.waxberry.manager.conversation.service.ConversationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;


@Slf4j
@Service
@Transactional
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public Conversation add(Conversation conversation) {
        String userId = SecurityUtils.requireCurrentUserId();
        conversation.setStatus(ConversationConstant.DataStatus.ENABLE);
        conversation.setCreateTime(new Date());
        conversation.setCreator(userId);
        conversation.setModifyTime(new Date());
        conversation.setModifier(userId);
        if (conversation.getId() == null || conversation.getId().isEmpty()) {
            // 如果id为null或者空字符串，说明前端没有传递id过来，此时需要手动生成id
            String generatedId = UUID.randomUUID().toString().replace("-","");
            conversation.setId(generatedId);
        }
        return conversationRepository.save(conversation);
    }

    @Override
    public Conversation edit(Conversation conversation) {
        String userId = SecurityUtils.requireCurrentUserId();
        conversation.setStatus(ConversationConstant.DataStatus.ENABLE);
        conversation.setCreateTime(new Date());
        conversation.setCreator(userId);
        conversation.setModifyTime(new Date());
        conversation.setModifier(userId);
        return conversationRepository.save(conversation);
    }

    @Override
    public void delete(Map<String,String> map) {
        String[] ids = map.get("ids").split(",");
        for(String id : ids){
            Conversation conversation = conversationRepository.findById(id).orElse(null);
            if(!ObjectUtils.isEmpty(conversation)){
                conversation.setStatus(ConversationConstant.DataStatus.DISABLE);
                conversationRepository.saveAndFlush(conversation);
            }
        }
    }

    @Override
    public List<ConversationDto> listByUser(int pageNo, int pageSize, String chatType) {
        String userId = SecurityUtils.requireCurrentUserId();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Conversation> conversationPageList = conversationRepository.getConversationsByCreatorAndStatusOrderByModifyTime(chatType,userId, ConversationConstant.DataStatus.ENABLE, pageable);
        return ObjectConvertUtil.convertToList(conversationPageList.getContent(), ConversationDto.class);
    }

    @Override
    public Conversation getConversationById(String id) {
        Optional<Conversation> conversation = conversationRepository.findById(id);
        if(conversation.isPresent()){
            return conversation.get();
        }
        return null;
    }
}
