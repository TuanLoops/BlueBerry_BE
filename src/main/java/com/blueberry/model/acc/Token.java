package com.blueberry.model.acc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    String token;
    String email;
    boolean isLogin;
}
