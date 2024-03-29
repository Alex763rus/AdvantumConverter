package com.example.advantumconverter.model.pojo.booker;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
public class BookerInputData {

    private String client; //Площадка
    private String counterparty; //Контрагент
    private String inn; //ИНН
    private String carNumber; //Гос.номер
    private Double rate; //Тариф, руб

    private Integer rnic;
    private Integer x5;
    private Integer ashan;
    private Integer metro;
    private Integer ozon;
    private Integer av;
    private Integer billa;
    private Integer magnit;
    private Integer loginet;
    private Integer oboz;
    private Integer rezident;
    private Integer verniy;
    private Integer atmc;

    //Вычисляемые поля:
    private Integer ref;
    private Double rateTc; //Тариф, а ТС, руб (вычисляется)
}
