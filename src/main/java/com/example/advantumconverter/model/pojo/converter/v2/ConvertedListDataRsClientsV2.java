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
public class ConvertedListDataRsClientsV2 implements ConvertedListDataV2{

    private String columnAdata;
    private Integer columnBdata;
    private String columnCdata;
    private String columnDdata;
    private Date columnEdata;
    private Date columnFdata;
    private String columnGdata;
    private String columnHdata;
    private Integer columnIdata;
    private Double columnJdata;
    private Double columnKdata;
    private String columnLdata;
    private String columnMdata;
    private String columnNdata;
//    private Integer columnOdata;
//    private Integer columnPdata;
//    private Integer columnQdata;
//    private Integer columnRdata;
//    private Date columnSdata;
//    private Date columnTdata;
//    private String columnUdata;
//    private String columnVdata;
//    private String columnWdata;
//    private Integer columnXdata;
//    private Double columnYdata;
//    private Double columnZdata;
//    private String columnAaData;
//    private String columnAbData;
//    private String columnAcData;
//    private String columnAdData;
//    private String columnAeData;
//    private Integer columnAfData;
//    private String columnAgData;
//    private String columnAhData;
//    private Integer columnAiData;
//    private String columnAjData;
//    private String columnAkData;
//    private String columnAlData;
//    private String columnAmData;
//    private String columnAnData;
//    private String columnAoData;
//    private String columnApData;

    //==Технические поля, не участвующие в формировании файла: =============================
    private String techFullFio;

}
