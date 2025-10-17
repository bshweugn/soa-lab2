package com.example.organizationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name cannot be empty")
    @NotNull(message = "Name cannot be null")
    @Column(nullable = false)
    private String name;

    @Valid
    @NotNull(message = "Coordinates cannot be null")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "coordinate_x")),
            @AttributeOverride(name = "y", column = @Column(name = "coordinate_y"))
    })
    private Coordinates coordinates;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate; // генерируется


    @Min(value = 1, message = "Number of rooms must be grater than 0")
    private long annualTurnover;

    @NotNull(message = "Full name cannot be null")
    @Column(nullable = false)
    private String fullName;

    @Min(value = 1, message = "Employees count must be grater than 0")
    private int employeesCount;

    @Enumerated(EnumType.STRING)
    private OrganizationType type; // может быть null

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address officialAddress; // может быть null

    @PrePersist
    protected void onCreate(){
        if(creationDate == null){
            creationDate = LocalDateTime.now();
        }
    }
}
