package com.example.advantumconverter.model.jpa.metro;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "metro_time_dictionary")
public class MetroTimeDictionary {

    @EmbeddedId
    private MetroTimeDictionaryKey metroTimeDictionaryKey;

    @Column(name = "time_start")
    private String timeStart;

    @Column(name = "time_end")
    private String timeEnd;

}
