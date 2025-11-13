package com.example.advantumconverter.model.pojo.converter.v2;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
@ToString
public class ConvertedListDataRsLentaClientsV2 implements ConvertedListDataV2 {

    private String columnAdata;
    private Date columnBdata;
    private String columnCdata;
    private String columnDdata;
    private String columnEdata;
    private String columnFdata;
    private String columnGdata;
    private String columnHdata;
    private String columnIdata;
    private String columnJdata;
    private Integer columnKdata;
    private String columnLdata;
    private Integer columnMdata;
    private String columnNdata;

    //==Технические поля, не участвующие в формировании файла: =============================
    private String techCountRepeat;

}
