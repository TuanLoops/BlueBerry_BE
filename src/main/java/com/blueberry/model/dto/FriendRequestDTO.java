package com.blueberry.model.dto;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.FriendRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestDTO {
    private Long id;
    private AppUserDTO sender;
    private AppUserDTO receiver;
    private LocalDateTime createAt;
    private FriendRequestStatus status;
}

