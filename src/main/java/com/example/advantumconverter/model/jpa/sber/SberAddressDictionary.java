package com.example.advantumconverter.model.jpa.sber;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.common.aliasing.qual.Unique;

@Getter
@Setter
@ToString
@Entity(name = "sber_address")
public class SberAddressDictionary {

    @Id
    @Column(name = "sber_address_id")
    @Unique
    private Long sberAddressId;

    @Column(name = "city")
    private String city;

    @Column(name = "city_and_region")
    private String cityAndRegion;

    @Column(name = "address")
    private String address;

}
