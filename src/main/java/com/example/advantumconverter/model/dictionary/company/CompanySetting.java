package com.example.advantumconverter.model.dictionary.company;

import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import lombok.*;

import java.util.List;
import java.util.Map;

import static com.example.advantumconverter.constant.Constant.Company.COMPANY_ADVANTUM;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompanySetting {

    private Map<Company, List<? extends ConvertService>> companyConverter;

    public List<? extends ConvertService> getConverters(Company company){
        return companyConverter.get(company);
    }

}
