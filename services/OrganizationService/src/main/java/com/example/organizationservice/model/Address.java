package com.example.organizationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255, message = "Street can't be longer than 255 characters")
    @NotBlank(message = "Street cannot be empty")
    private String street;

    @NotNull(message = "zipCode cannot be null")
    @Size(max = 21, message = "zipCode value cannot be grater than 21")
    @Column(nullable = false)
    private String zipCode;

    @Valid
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location town;

}
