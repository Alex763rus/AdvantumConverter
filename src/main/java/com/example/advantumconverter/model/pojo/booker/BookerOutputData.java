package com.example.advantumconverter.model.pojo.booker;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
public class BookerOutputData {

    private String inn; //ИНН
    //Вычисляемые поля:
    private Integer carNumberCount; //Количество автомобилей в рамках inn
    private Double rateTcSum; //Общая сумма
}
