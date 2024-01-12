package com.blueberry.service.token;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TokenStore {
    private final Set<String> activeTokens = new HashSet<>();
    public void storeToken(String token) {
        activeTokens.add(token);
    }

    public void removeToken(String token) {
        activeTokens.remove(token);
    }

    public boolean isTokenActive(String token) {
        return activeTokens.contains(token);
    }
}
