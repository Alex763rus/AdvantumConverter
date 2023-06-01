package com.example.advantumconverter.model.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "support")
public class Support {

    @Id
    @Column(name = "support_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supportId;

}
