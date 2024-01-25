package com.blueberry.model.app;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "messages")
public class Message {
    @Id
    private String id;

    @Column(name = "chat_room_id", nullable = false)
    private String chatRoomId;

    @ManyToOne
    private AppUser sender;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timeStamp;

    @Column(nullable = false)
    private Boolean isEdited;
}
