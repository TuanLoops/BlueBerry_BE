package com.blueberry.model.app;


import com.blueberry.model.acc.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private int phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String avatarImage;

    @Column(columnDefinition = "TEXT")
    private String bannerImage;

    private String hobbies;

    private String address;

    @OneToOne
    private Account account;
}
