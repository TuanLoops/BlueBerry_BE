package com.blueberry.model.app;


import com.blueberry.model.acc.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "appusers")
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String avatarImage;

    @Column(columnDefinition = "TEXT")
    private String bannerImage;

    private String hobbies;

    private String address;

    @OneToOne
    private User user;

    private LocalDateTime lastOnline;
}
