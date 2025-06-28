package com.jiangxue.waxberry.manager.conversation.service.impl;

import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.conversation.constant.ConversationConstant;
import com.jiangxue.waxberry.manager.conversation.entity.ConversationMessage;
import com.jiangxue.waxberry.manager.conversation.repository.ConversationMessageRepository;
import com.jiangxue.waxberry.manager.conversation.service.ConversationMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
public class ConversationMessageServiceImpl implements ConversationMessageService {

    @Autowired
    private ConversationMessageRepository conversationMessageRepository;


    @Override
    public Page<ConversationMessage> getConversationMessageList(int pageNo, int pageSize, String conversationId) {
        String userId = SecurityUtils.requireCurrentUserId();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<ConversationMessage> conversationMessagesPageList = conversationMessageRepository.getConversationMessagesByConversationId(conversationId, userId, ConversationConstant.DataStatus.ENABLE, pageable);
        return conversationMessagesPageList;
    }

    @Override
    public ConversationMessage add(ConversationMessage conversationMessage) {
        String userId = SecurityUtils.requireCurrentUserId();
        conversationMessage.setStatus(ConversationConstant.DataStatus.ENABLE);
        conversationMessage.setCreator(userId);
        conversationMessage.setModifier(userId);
        conversationMessage.setCreateTime(new Date());
        conversationMessage.setModifyTime(new Date());
        return conversationMessageRepository.saveAndFlush(conversationMessage);
    }

    @Override
    public void delete(Map<String,String> map) {
        String[] ids = map.get("ids").split(",");
        for(String id : ids){
            ConversationMessage conversationMessage = conversationMessageRepository.findById(id).orElse(null);
            if(!ObjectUtils.isEmpty(conversationMessage)){
                conversationMessage.setStatus(ConversationConstant.DataStatus.DISABLE);
                conversationMessageRepository.saveAndFlush(conversationMessage);
            }
        }
    }

    @Override
    public void edit(String type, ConversationMessage conversationMessage) {
        ConversationMessage cm = conversationMessageRepository.getOne(conversationMessage.getId());
        if(!ObjectUtils.isEmpty(cm)){
            if(ConversationConstant.MessageType.QUERY.equals(type)){
                cm.setQuery("");
            }else if(ConversationConstant.MessageType.REPONSE.equals(type)){
                cm.setReponse("");
            }
        }
        conversationMessageRepository.saveAndFlush(cm);
    }

    @Override
    public void deleteByConversationId(Map<String, String> map) {
        if(!CollectionUtils.isEmpty(map) && !StringUtils.isEmpty(map.get("conversationId"))){
            String userId = SecurityUtils.requireCurrentUserId();
            List<ConversationMessage> conversationMessageList = conversationMessageRepository.findByConversationId(map.get("conversationId"), userId);
            if(!CollectionUtils.isEmpty(conversationMessageList)){
                List<ConversationMessage> deleteConversationMessageList = conversationMessageList.stream()
                        .peek(conversationMessage -> conversationMessage.setStatus(ConversationConstant.DataStatus.DISABLE)) // 设置状态值
                        .collect(Collectors.toList());
                conversationMessageRepository.saveAll(deleteConversationMessageList);
            }
        }
    }


}
