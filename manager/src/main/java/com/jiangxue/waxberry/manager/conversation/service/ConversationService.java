package com.jiangxue.waxberry.manager.conversation.service;


import com.jiangxue.waxberry.manager.conversation.dto.ConversationDto;
import com.jiangxue.waxberry.manager.conversation.entity.Conversation;
import java.util.List;
import java.util.Map;

public interface ConversationService {

    Conversation add(Conversation conversation);

    Conversation edit(Conversation conversation);

    void delete(Map<String, String> map);

    List<ConversationDto> listByUser(int pageNo, int pageSize, String chatType);

    Conversation getConversationById(String id);
}
