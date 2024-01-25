package com.blueberry.repository;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.ChatRoom;
import com.blueberry.model.app.ChatRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    @Query("SELECT DISTINCT cr FROM ChatRoom cr JOIN cr.participants p WHERE p.id = :userId ORDER BY :field DESC ")
    List<ChatRoom> findAllChatRoomsByUserId(@Param("userId") Long userId, @Param("field") String fieldName);

    @Query("SELECT DISTINCT cr FROM ChatRoom cr JOIN cr.participants p WHERE p.id = :userId AND cr.chatRoomType = :type")
    List<ChatRoom> findAllPrivateChatRoomsByUserId(@Param("userId") Long userId, @Param("type") ChatRoomType chatRoomType);
}
