package com.example.advantumconverter.model.jpa.ozon;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@ToString
public class OzonTransitTimeKey implements Serializable {

    private String departure;
    private String arrival;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OzonTransitTimeKey that = (OzonTransitTimeKey) o;
        return Objects.equals(departure, that.departure) && Objects.equals(arrival, that.arrival);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departure, arrival);
    }
}
