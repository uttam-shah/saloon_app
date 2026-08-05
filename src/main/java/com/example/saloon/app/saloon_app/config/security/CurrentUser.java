package com.example.saloon.app.saloon_app.config.security;

import org.springframework.security.core.context.SecurityContextHolder;

public final class CurrentUser {

    private CurrentUser() {
    }

    public static String id() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new org.springframework.security.core.AuthenticationException("Not authenticated") {
            };
        }
        return (String) authentication.getPrincipal();
    }
}
