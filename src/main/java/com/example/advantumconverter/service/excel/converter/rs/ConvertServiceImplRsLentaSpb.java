package com.example.advantumconverter.service.excel.converter.rs;

import com.example.advantumconverter.service.excel.converter.ConvertService;
import org.springframework.stereotype.Component;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_RS_LENTA_SPB;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_RS_LENTA_SPB;

@Component
public class ConvertServiceImplRsLentaSpb extends AbstractConvertServiceImplRsLentaCity implements ConvertService {

    @Override
    public String getConverterName() {
        return FILE_NAME_RS_LENTA_SPB;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_RS_LENTA_SPB;
    }

}
