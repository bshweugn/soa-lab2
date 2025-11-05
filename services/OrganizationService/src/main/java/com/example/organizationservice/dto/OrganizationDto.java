package com.example.organizationservice.dto;

import com.example.organizationservice.model.Address;
import com.example.organizationservice.model.Coordinates;
import com.example.organizationservice.model.Organization;
import com.example.organizationservice.model.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDto {
    private long id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private long annualTurnover;
    private String fullName;
    private int employeesCount;
    private OrganizationType type;
    private Address officialAddress;

    public Organization toEntity(){
        var org = new Organization();
        org.setId(id);
        org.setName(name);
        org.setCoordinates(coordinates);
        org.setCreationDate(creationDate);
        org.setAnnualTurnover(annualTurnover);
        org.setFullName(fullName);
        org.setEmployeesCount(employeesCount);
        org.setType(type);
        org.setOfficialAddress(officialAddress);

        return org;
    }
}
