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
public class ConvertedListDataRsLentaSpbV2 implements ConvertedListDataV2 {

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
    private String columnKdata;
    private Integer columnLdata;
    private String columnMdata;
    private Integer columnNdata;
    private String columnOdata;
    private Integer columnPdata;
    private String columnRdata;
    private String columnSdata;

    //==Технические поля, не участвующие в формировании файла: =============================
    private Integer techCountRepeat;
    private String techProductGroup;

}
