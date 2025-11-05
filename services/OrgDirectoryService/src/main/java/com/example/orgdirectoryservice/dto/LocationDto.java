package com.example.orgdirectoryservice.dto;

import lombok.Data;

@Data
public class LocationDto {
    private Long id;
    private long x;
    private double y;
    private double z;
    private String name;
}
