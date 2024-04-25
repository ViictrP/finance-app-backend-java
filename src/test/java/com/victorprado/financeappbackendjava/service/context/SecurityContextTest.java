package com.victorprado.financeappbackendjava.service.context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SecurityContextTest {

    @BeforeEach
    public void beforeEach() {
        ContextTestHelper.mockSecurity();
    }

    @Test
    @DisplayName("Should return user id")
    void test1() {
        var id = SecurityContext.getUserId();
        assertNotNull(id);
    }

    @Test
    @DisplayName("Should return user name")
    void test2() {
        var name = SecurityContext.getUserName();
        assertNotNull(name);
    }


    @Test
    @DisplayName("Should return user lastname")
    void test3() {
        var lastName = SecurityContext.getUserLastName();
        assertNotNull(lastName);
    }

    @Test
    @DisplayName("Should return user email")
    void test4() {
        var email = SecurityContext.getUserEmail();
        assertNotNull(email);
    }

    @Test
    @DisplayName("Should return user")
    void test5() {
        var user = SecurityContext.getUser();
        assertNotNull(user);
    }
}
