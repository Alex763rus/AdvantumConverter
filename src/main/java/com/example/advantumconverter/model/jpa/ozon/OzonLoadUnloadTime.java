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
@Entity(name = "ozon_load_unload_time")
public class OzonLoadUnloadTime {

    @Id
    @Column(name = "arrival")
    private String arrival;

    @Column(name = "unload_time")
    private Integer unloadTime;

    @Column(name = "load_time")
    private Integer loadTime;
}
