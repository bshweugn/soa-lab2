package com.example.orgdirectoryservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrganizationDto {
    private Long id;
    private String name;
    private CoordinateDto coordinates;
    private LocalDateTime creationDate;
    private long annualTurnover;
    private String fullName;
    private int employeesCount;
    private String type;
    private AddressDto officialAddress;
}
