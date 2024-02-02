package com.example.advantumconverter.model.jpa.ozon;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "ozon_tonnage_time")
public class OzonTonnageTime {

    @Id
    @Column(name = "tonnage")
    private Long tonnage;

    @Column(name = "time")
    private String time;

}
