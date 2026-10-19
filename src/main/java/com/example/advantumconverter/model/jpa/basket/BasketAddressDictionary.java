package com.example.advantumconverter.model.jpa.basket;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@Entity(name = "basket_address")
public class BasketAddressDictionary {

    @Id
    private UUID id;
    private String region;
    private String address;

}
