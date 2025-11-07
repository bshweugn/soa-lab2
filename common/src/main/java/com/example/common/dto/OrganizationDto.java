package com.example.common.dto;

import com.example.common.model.Address;
import com.example.common.model.Coordinates;
import com.example.common.model.Organization;
import com.example.common.model.OrganizationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDto {
    private long id;

    @NotBlank(message = "Name cannot be empty")
    @NotNull(message = "Name cannot be null")
    @Size(max = 255, message = "Name can't be longer than 255 characters")
    private String name;

    @Valid
    @NotNull(message = "Coordinates cannot be null")
    private Coordinates coordinates;

    private LocalDateTime creationDate;

    @Min(value = 1, message = "Number of rooms must be grater than 0")
    private long annualTurnover;

    @NotNull(message = "Full name cannot be null")
    @Size(max = 255, message = "Full name can't be grater than 255 characters")
    private String fullName;

    @Min(value = 1, message = "Employees count must be grater than 0")
    private int employeesCount;

    private OrganizationType type;

    @Valid
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
