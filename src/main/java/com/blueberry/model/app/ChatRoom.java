package com.blueberry.model.app;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    private String id;

    @ManyToMany
    @JoinTable(
            name = "chat_room_participants",
            joinColumns = @JoinColumn(name = "chat_room_id"),
            inverseJoinColumns = @JoinColumn(name = "appuser_id")
    )
    private List<AppUser> participants;

    @OneToMany
    @JoinColumn(name = "chat_room_id")
    private List<Message> messages;

    @OneToOne
    private Message lastMessage;

    @Column(nullable = false)
    private ChatRoomType chatRoomType;

    private LocalDateTime lastActivity;
}
