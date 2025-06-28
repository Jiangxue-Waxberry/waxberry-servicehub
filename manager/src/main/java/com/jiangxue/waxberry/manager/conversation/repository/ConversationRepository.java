package com.jiangxue.waxberry.manager.conversation.repository;

import com.jiangxue.waxberry.manager.conversation.entity.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ConversationRepository extends JpaRepository<Conversation,String> {

    @Query(value = "FROM Conversation t WHERE t.chatType=:chatType AND t.creator=:creator AND t.status=:status ORDER BY t.createTime DESC")
    Page<Conversation> getConversationsByCreatorAndStatusOrderByModifyTime(@Param("chatType") String chatType, @Param("creator") String creator, @Param("status") String status, Pageable pageable);


    long countByIdIn(List<String> ids);

}
