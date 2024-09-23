package com.victor.financeapp.backend.service.context;

import com.victor.financeapp.backend.service.dto.UserDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityContext {

    public static String getUserId() {
        return getAuthentication().getClaim("sub");
    }

    public static String getUserEmail() {
        return getAuthentication().getClaim("email");
    }

    public static String getUserName() {
        return getAuthentication().getClaim("name").toString().split(" ")[0];
    }

    public static String getUserLastName() {
        return getAuthentication().getClaim("name").toString().split(" ")[1];
    }

    private static Jwt getAuthentication() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static UserDTO getUser() {
        return UserDTO.builder()
                .id(getUserId())
                .name(getUserName())
                .lastname(getUserLastName())
                .email(getUserEmail())
                .build();
    }
}
