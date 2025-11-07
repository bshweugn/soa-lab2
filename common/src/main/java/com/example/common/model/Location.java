package com.example.common.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "X value can't be null")
    private Long x;
    private double y;
    private double z;

    @Size(max = 255, message = "Town name can't be longer than 255 characters")
    @NotNull(message = "Town name can't be null")
    private String name; // может быть null

}
