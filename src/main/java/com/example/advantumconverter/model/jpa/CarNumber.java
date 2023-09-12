package com.example.advantumconverter.model.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "car_number")
public class CarNumber {

    @Id
    @Column(name = "car_number_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carNumberId;
    
    @Column(name = "number")
    private String carNumber;
}
