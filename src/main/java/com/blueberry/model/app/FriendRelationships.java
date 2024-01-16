package com.blueberry.model.app;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friend_relationships")
public class FriendRelationships {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_sender_id")
    private AppUser sender;

    @ManyToOne
    @JoinColumn(name = "user_receiver_id")
    private AppUser receiver;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private FriendRequests requestStatus;
}
