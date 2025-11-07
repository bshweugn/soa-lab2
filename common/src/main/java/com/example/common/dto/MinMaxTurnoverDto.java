package com.example.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MinMaxTurnoverDto {

    @NotNull(message = "min turnover can't be null")
    @Min(value = 0, message = "min turnover must be grater than 0")
    private Long minTurnover;

    @NotNull(message = "min turnover can't be null")
    @Min(value = 0, message = "min turnover must be grater than 0")
    private Long maxTurnover;
}
