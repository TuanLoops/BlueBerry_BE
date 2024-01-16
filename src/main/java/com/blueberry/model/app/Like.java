package com.blueberry.model.app;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
@IdClass(Like.class)
public class Like implements Serializable {

    @Id
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Id
    @Column(name = "status_id",nullable = false)
    private Long statusId;



}
