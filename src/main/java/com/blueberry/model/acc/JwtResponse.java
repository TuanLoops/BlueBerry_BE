package com.blueberry.model.acc;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class JwtResponse {

    private Long id;
    private String token;
    private String type = "Bearer";
    private String email;
    private Collection<? extends GrantedAuthority> roles;

    public JwtResponse(String accessToken, Long id, String email, Collection<? extends GrantedAuthority> roles) {
        this.token = accessToken;
        this.email = email;
        this.roles = roles;
        this.id = id;
    }

}
