package com.victorprado.financeappbackendjava.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class ProfileDTO {

    @NotBlank(message = "The name is required")
    private String name;

    @NotBlank(message = "The lastname is required")
    private String lastname;

    @Email(message = "The email should be valid")
    private String email;

    @NotNull(message = "The salary is required")
    private BigDecimal salary;

    private String password;
    private Map<String, String> properties;
}
