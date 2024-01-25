package com.blueberry.service.token;

import com.blueberry.model.acc.Token;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TokenStore {
    private final Set<Token> activeTokens = new HashSet<>();

    public void storeToken(Token token) {
        activeTokens.add(token);
    }

    public void removeToken(String token) {
        if (token != null && !token.isEmpty()) {
            for (Token t : activeTokens) {
                if (t.getToken().contains(token)) {
                    activeTokens.remove(t);
                    break;
                }
            }
        }
    }

    public boolean isTokenLogin(String token) {
        if (token != null && !token.isEmpty()) {
            for (Token item : activeTokens) {
                if (item.getToken().contains(token) && item.isLogin()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isTokenActive(String token) {
        if (token != null && !token.isEmpty()) {
            for (Token item : activeTokens) {
                if (item.getToken().contains(token) && !item.isLogin()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Token getTokenByEmail(String email, boolean isLogin) {
        for (Token token : activeTokens) {
            if (token.getEmail().equals(email) && (token.isLogin() == isLogin)) {
                return token;
            }
        }
        return null;
    }
}
