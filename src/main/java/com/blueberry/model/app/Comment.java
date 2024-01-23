package com.blueberry.model.app;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status_id",nullable = false)
    private Long statusId;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private AppUser author;

    @Column(nullable = false)
    private String body;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image image;

    private boolean isUpdated;

    @Column(nullable = false)
    private boolean isDeleted;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id")
    private List<CommentLike> likes;

    @Transient
    private boolean liked;

}
