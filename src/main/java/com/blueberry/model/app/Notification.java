package com.blueberry.model.app;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "notifications")
public class Notification {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private AppUser sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private AppUser receiver;

    private LocalDateTime timeStamp;

    private Boolean isRead;
}