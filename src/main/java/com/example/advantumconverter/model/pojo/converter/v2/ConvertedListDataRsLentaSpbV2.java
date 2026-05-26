package com.example.advantumconverter.model.pojo.converter.v2;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private String columnQdata;
    private String columnRdata;

    //==Технические поля, не участвующие в формировании файла: =============================
    private Integer techCountRepeat;
    private String techProductGroup;

    private List<Repeat> techRepeats;

    @Getter
    @AllArgsConstructor
    public static class Repeat {
        private Integer repeat;
        private String groupName;
        private String columnName;
    }
}
