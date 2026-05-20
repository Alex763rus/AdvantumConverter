package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.model.jpa.siel.SielPoints;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import org.springframework.stereotype.Component;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_SIEL;
import static com.example.advantumconverter.constant.Constant.Converter.PROM_TOVAR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_SIEL;

@Component
public class ConvertServiceImplSiel extends AbstractConvertServiceImplSiel implements ConvertService {
    private final String S_TIME_START_2H = "2:00:00";
    private final String S_TIME_END_9H = "9:00:00";
    private final String T_TIME_START_7H = "7:00:00";
    private final String T_TIME_END_14H = "14:00:00";
    private final String AREA_NAME = "Склад РЦ Тула Хлеб";

    @Override
    protected Integer getStartRow() {
        return 6;
    }

    @Override
    protected String getColumnCdata() {
        return SIEL_COMPANY_NAME;
    }

    @Override
    protected String getColumnFdata() {
        return PROM_TOVAR;
    }

    @Override
    protected Integer getColumnJdata() {
        return 1500;
    }

    @Override
    protected Integer getColumnKdata() {
        return 350;
    }

    @Override
    protected Integer getColumnLdata() {
        return null;
    }

    @Override
    protected Integer getColumnMdata() {
        return null;
    }

    @Override
    protected String getSTime(boolean isStart, int row) {
        if (isStart) {
            return S_TIME_START_2H;
        }
        String pointName = getPointName(row);
        return dictionaryService.getSielPoint(pointName)
                .map(SielPoints::getTimeStart)
                .orElse(S_TIME_END_9H);
    }

    @Override
    protected String getTTime(boolean isStart, int row) {
        if (isStart) {
            return T_TIME_START_7H;
        }
        String pointName = getPointName(row);
        return dictionaryService.getSielPoint(pointName)
                .map(SielPoints::getTimeEnd)
                .orElse(T_TIME_END_14H);
    }

    @Override
    protected String getPointName() {
        return AREA_NAME;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_SIEL;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_SIEL;
    }

}
