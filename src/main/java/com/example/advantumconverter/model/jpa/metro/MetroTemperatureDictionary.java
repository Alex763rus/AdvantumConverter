package com.example.advantumconverter.model.jpa.metro;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "metro_temperature_dictionary")
public class MetroTemperatureDictionary {

    @Id
    @Column(name = "temperature_id")
    private String temperatureId;

    @Column(name = "min_temperature")
    private Long minTemperature;

    @Column(name = "max_temperature")
    private Long maxTemperature;

}
