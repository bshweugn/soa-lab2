package com.example.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TypeDto {
    @NotBlank(message = "Type must not be blank")
    private String type;
}
