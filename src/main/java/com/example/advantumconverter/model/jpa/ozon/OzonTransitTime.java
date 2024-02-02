package com.example.advantumconverter.model.jpa.ozon;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "ozon_transit_time")
public class OzonTransitTime {

    @EmbeddedId
    private OzonTransitTimeKey ozonTransitTimeKey;

    @Column(name = "transit_time")
    private String transitTime;
}
