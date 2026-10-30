package com.example.advantumconverter.model.pojo.converter.v2;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
@ToString
public class ConvertedListDataRsLentaSpbNewV2 implements ConvertedListDataV2 {

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
    private String columnLdata;
    private Integer columnMdata;
    private String columnNdata;
    private Integer columnOdata;
    private String columnPdata;
    private String columnQdata;
    private String columnRdata;
    private String columnSdata;

    //==Технические поля, не участвующие в формировании файла: =============================
    private Integer techCountRepeat;
    private String techProductGroup;
    private boolean isTara = false;

    private List<Repeat> techRepeats;

    @Getter
    @AllArgsConstructor
    public static class Repeat {
        private Integer repeat;
        private String groupName;
        private String columnName;
    }
}
