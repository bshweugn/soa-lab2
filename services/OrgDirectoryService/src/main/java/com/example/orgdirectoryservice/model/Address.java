package com.example.orgdirectoryservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1)
    @NotBlank(message = "Street cannot be empty")
    private String street; // может быть null, если не null — не пустая строка

    @NotNull(message = "zipCode cannot be null")
    @Size(max = 21, message = "zipCode value cannot be grater than 21")
    @Column(nullable = false)
    private String zipCode;

    @Valid
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location town; // может быть null

}