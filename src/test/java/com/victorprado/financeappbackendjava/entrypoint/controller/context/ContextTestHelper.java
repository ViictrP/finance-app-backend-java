package com.victorprado.financeappbackendjava.entrypoint.controller.context;

import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.mockito.ArgumentMatchers.anyString;

public final class ContextTestHelper {

    private ContextTestHelper() {

    }

    public static void mockSecurity() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Jwt jwt = Mockito.mock(Jwt.class);
        org.springframework.security.core.context.SecurityContext securityContext = Mockito.mock(org.springframework.security.core.context.SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(jwt.getClaim(anyString())).thenReturn("TEST");
        Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn(jwt);
        SecurityContextHolder.setContext(securityContext);
    }
}
