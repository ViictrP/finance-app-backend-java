package com.victorprado.financeappbackendjava.service.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UpdateProfileDTO {
    @NotEmpty(message = "You cannot erase all your properties!")
    private Map<String, String> properties;
}
