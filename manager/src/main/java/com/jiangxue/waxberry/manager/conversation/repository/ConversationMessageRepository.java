package com.jiangxue.waxberry.manager.conversation.repository;

import com.jiangxue.waxberry.manager.conversation.entity.ConversationMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ConversationMessageRepository extends JpaRepository<ConversationMessage,String> {

    @Query(value = "FROM ConversationMessage t WHERE t.conversationId=:conversationId AND t.creator=:creator AND t.status=:status ORDER BY t.createTime ASC")
    Page<ConversationMessage> getConversationMessagesByConversationId(@Param("conversationId") String conversationId,
                                                                      @Param("creator") String creator,
                                                                      @Param("status") String status, Pageable pageable);

    @Query(value = "FROM ConversationMessage t WHERE t.conversationId=:conversationId AND t.creator=:creator ")
    List<ConversationMessage> findByConversationId(@Param("conversationId") String conversationId,
                                                                      @Param("creator") String creator);
}
