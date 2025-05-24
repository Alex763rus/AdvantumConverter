package com.example.advantumconverter.model.jpa.lenta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "lenta_ts_city")
public class LentaTsCity {

    @Id
    @Column(name = "ts")
    private String ts;

    @Column(name = "city_brief")
    private String cityBrief;
}
