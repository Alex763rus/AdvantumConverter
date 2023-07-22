package com.example.advantumconverter.model.jpa;

import com.example.advantumconverter.enums.LentaDictionaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.common.aliasing.qual.Unique;

@Getter
@Setter
@ToString
@Entity(name = "lenta_dictionary")
public class LentaDictionary {

    @Id
    @Column(name = "lenta_dictionary_key")
    @Unique
    private Long lentaDictionaryKey;

    @Column(name = "type")
    private LentaDictionaryType lentaDictionaryType;

    @Column(name = "address_name")
    private String addressName;
    @Column(name = "time_stock")
    private String timeStock;

    @Column(name = "time_shop")
    private String timeShop;

    @Column(name = "region")
    private String region;

}
