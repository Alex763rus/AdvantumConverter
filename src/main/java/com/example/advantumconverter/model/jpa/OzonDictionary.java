package com.example.advantumconverter.model.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "ozon_dictionary")
public class OzonDictionary {

    @Id
    @Column(name = "ozon_dictionary_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ozonDictionaryId;

    @Column(name = "stock_brief")
    private String stockBrief;

    @Column(name = "stock_in_time")
    private String stockInTime;

    @Column(name = "stock_out_time")
    private String stockOutTime;

}
