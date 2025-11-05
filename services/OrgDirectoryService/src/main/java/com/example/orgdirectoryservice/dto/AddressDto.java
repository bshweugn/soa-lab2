package com.example.orgdirectoryservice.dto;

import lombok.Data;

@Data
public class AddressDto {
    private String street;
    private String zipCode;
    private LocationDto town;
}
