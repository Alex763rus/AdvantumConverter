package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.model.jpa.sielmilk.SielMilkPoints;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import org.springframework.stereotype.Component;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_SIEL_MILK;
import static com.example.advantumconverter.constant.Constant.Converter.REFRIGERATOR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_SIEL_MILK;

@Component
public class ConvertServiceImplSielMilk extends AbstractConvertServiceImplSiel implements ConvertService {

    public static final String SIEL_COMPANY_MILK_NAME = "ООО СИЭЛЬ";
    private final String S_TIME_START_5H = "5:00:00";
    private final String S_TIME_END_9H = "9:00:00";
    private final String T_TIME_START_7H_30M = "7:30:00";
    private final String T_TIME_END_18H = "18:00:00";
    private final String AREA_NAME_MILK = "Склад РЦ Тула Молоко";

    @Override
    protected Integer getStartRow() {
        return 1;
    }

    @Override
    protected String getColumnCdata() {
        return SIEL_COMPANY_MILK_NAME;
    }

    @Override
    protected String getColumnFdata() {
        return REFRIGERATOR;
    }

    @Override
    protected Integer getColumnJdata() {
        return 2000;
    }

    @Override
    protected Integer getColumnKdata() {
        return 8;
    }

    @Override
    protected Integer getColumnLdata() {
        return 2;
    }

    @Override
    protected Integer getColumnMdata() {
        return 6;
    }

    @Override
    protected String getSTime(boolean isStart, int row) {
        if (isStart) {
            return S_TIME_START_5H;
        }
        String pointName = getPointName(row);
        return dictionaryService.getSielMilkPoint(pointName)
                .map(SielMilkPoints::getTimeStart)
                .orElse(S_TIME_END_9H);
    }

    @Override
    protected String getTTime(boolean isStart, int row) {
        if (isStart) {
            return T_TIME_START_7H_30M;
        }
        String pointName = getPointName(row);
        return dictionaryService.getSielMilkPoint(pointName)
                .map(SielMilkPoints::getTimeEnd)
                .orElse(T_TIME_END_18H);
    }

    @Override
    protected String getPointName() {
        return AREA_NAME_MILK;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_SIEL_MILK;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_SIEL_MILK;
    }
}
