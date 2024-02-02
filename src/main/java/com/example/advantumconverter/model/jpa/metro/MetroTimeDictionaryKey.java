package com.example.advantumconverter.model.jpa.metro;

import jakarta.persistence.Column;
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
public class MetroTimeDictionaryKey implements Serializable {

    @Column(name = "time_id")
    private Long timeId;

    @Column(name = "code")
    private String code;

    @Column(name = "priority")
    private Long priority;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetroTimeDictionaryKey that = (MetroTimeDictionaryKey) o;
        return timeId.equals(that.timeId) && code.equals(that.code) && priority.equals(that.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeId, code, priority);
    }
}
