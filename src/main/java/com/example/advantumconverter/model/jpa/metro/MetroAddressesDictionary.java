package com.example.advantumconverter.model.jpa.metro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "metro_addresses_dictionary")
public class MetroAddressesDictionary {

    @Id
    @Column(name = "addresses_id")
    private String addressesId;

    @Column(name = "addresses_name")
    private String addressesName;

}
