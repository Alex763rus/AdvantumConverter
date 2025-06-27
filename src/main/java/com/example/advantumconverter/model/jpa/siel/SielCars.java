package com.example.advantumconverter.model.jpa.siel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "siel_cars")
public class SielCars {

    @Id
    @Column(name = "car_number")
    private String carNumber;

    @Column(name = "carrier_name")
    private String carrierName;

}
