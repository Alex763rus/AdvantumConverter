package com.example.advantumconverter.service.excel.converter.rs;

import com.example.advantumconverter.service.excel.converter.ConvertService;
import org.springframework.stereotype.Component;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_RS_LENTA;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_RS_LENTA;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Component
public class ConvertServiceImplRsLenta extends AbstractConvertServiceImplRsLenta implements ConvertService {

    @Override
    public String getConverterName() {
        return FILE_NAME_RS_LENTA;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_RS_LENTA;
    }

    @Override
    String calculatePointType(String pointOperationType, Integer pointTypeReturnMassa) {
        return switch (pointOperationType) {
            case RETURN_CONTAINERS -> POINTTYPE_D;
            case LOAD_THE_GOODS -> POINTTYPE_P;
            case UNLOAD_THE_GOODS -> POINTTYPE_PD;
            default -> EMPTY;
        };
    }

}
