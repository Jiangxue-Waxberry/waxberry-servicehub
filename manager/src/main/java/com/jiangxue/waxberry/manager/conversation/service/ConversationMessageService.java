package com.jiangxue.waxberry.manager.conversation.service;

import com.jiangxue.waxberry.manager.conversation.entity.ConversationMessage;
import org.springframework.data.domain.Page;
import java.util.Map;

public interface ConversationMessageService {

    Page<ConversationMessage> getConversationMessageList(int pageNo, int pageSize, String conversationId);

    ConversationMessage add(ConversationMessage conversationMessage);

    void delete(Map<String, String> map);

    void edit(String type, ConversationMessage conversationMessage);

    void deleteByConversationId(Map<String, String> map);
}
