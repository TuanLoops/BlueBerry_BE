package com.blueberry.model.token;

import com.blueberry.model.acc.User;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserToken {
    private int token;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
