package com.blueberry.model.app;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statusId;

    private String body;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private List<Image> imageList;

}
