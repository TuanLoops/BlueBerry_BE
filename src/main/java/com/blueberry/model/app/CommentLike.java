package com.blueberry.model.app;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment_likes")
@IdClass(CommentLike.class)
public class CommentLike implements Serializable {

    @Id
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Id
    @Column(name = "comment_id",nullable = false)
    private Long commentId;

}
