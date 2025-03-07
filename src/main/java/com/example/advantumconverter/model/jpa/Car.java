package com.example.advantumconverter.model.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "car")
public class Car {

    @Id
    @Column(name = "car_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;
    @Column(name = "car_name")
    private String carName;
    @Column(name = "tonnage")
    private int tonnage;
    @Column(name = "pallet")
    private int pallet;
    @Column(name = "temperature_min")
    private int temperatureMin;
    @Column(name = "temperature_max")
    private int temperatureMax;
}
