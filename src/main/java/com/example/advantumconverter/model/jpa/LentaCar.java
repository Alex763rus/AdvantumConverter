package com.example.advantumconverter.model.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "lenta_car")
public class LentaCar {

    @Id
    @Column(name = "car_number")
    private String carNumber;

    @Column(name = "model")
    private String model;

    @Column(name = "year_issue")
    private Long yearIssue;

    @Column(name = "tonnage")
    private int tonnage;

    @Column(name = "vin")
    private String vin;

    @Column(name = "ax")
    private Long ax;

    @Column(name = "region")
    private String region;
}
