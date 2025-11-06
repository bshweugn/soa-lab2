package com.example.organizationservice.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Coordinates {

    @NotNull(message = "X value cannot be null")
    private Double x;

    @NotNull(message = "Y value cannot be null")
    @DecimalMax(value = "652", message = "Y value cannot be grater than 652")
    private Double y;

}