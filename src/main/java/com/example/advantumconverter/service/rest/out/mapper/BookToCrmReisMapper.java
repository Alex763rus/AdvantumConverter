package com.example.advantumconverter.service.rest.out.mapper;

import com.example.advantumconverter.gen.model.*;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import org.apache.commons.lang3.NotImplementedException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.advantumconverter.constant.Constant.Converter.LOAD_THE_GOODS;
import static com.example.advantumconverter.gen.model.RoutePointDto.OperationEnum.LOAD;
import static com.example.advantumconverter.gen.model.RoutePointDto.OperationEnum.UNLOAD;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.springdoc.core.utils.Constants.DOT;

public class BookToCrmReisMapper {

    private static final String NUMBER_ZERO = "0";
    private static final String OKAY = "OKAY";
    private static final String DEFAULT_EXTERNAL_ID = "24645624";
    private static final String SLASH_AND_DOT = "\\.";

    private BookToCrmReisMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static List<RouteWithDictionaryDto> map(ConvertedBook convertedBook) {
        throw new NotImplementedException("не реализован конвертер v1");
    }

    public static List<RouteWithDictionaryDto> map(ConvertedBookV2 convertedBookV2) {
        var content = convertedBookV2.getBookV2().get(0).getExcelListContentV2();
        Map<String, List<ConvertedListDataV2>> documents = content.stream()
                .collect(Collectors.groupingBy(ConvertedListDataV2::getColumnAdata));
        List<RouteWithDictionaryDto> result = new ArrayList<>();
        documents.forEach((reisNumber, points) -> {
            var pointsDto = new ArrayList<RoutePointDto>();
            for (ConvertedListDataV2 point : points) {
                pointsDto.add(
                        RoutePointDto.init()
                                .setPoint(preparePoint(point))
                                .setStartTime(convert(point.getColumnSdata()))
                                .setFinishTime(convert(point.getColumnTdata()))
                                .setOperation(LOAD_THE_GOODS.equals(point.getColumnWdata()) ? LOAD : UNLOAD)
                                .setSequenceNumber(point.getColumnXdata())
                                .build()
                );
            }
            var mainPoint = points.get(0);
            var fioSplit = mainPoint.getColumnAeData()
                    .replace(SPACE, DOT)
                    .split(SLASH_AND_DOT);
            var route = RouteWithDictionaryDto.init()
                    .setBodyTonnageInKg(toFloat(mainPoint.getColumnJdata()))
                    .setBodyVolumeInCubicMeters(toFloat(mainPoint.getColumnIdata()))
                    .setNumberOfPallets(mainPoint.getColumnKdata())
                    .setExternalId(reisNumber)
                    .setCustomer(prepareExternalOrganization(OKAY, DEFAULT_EXTERNAL_ID))
                    .setCarrier(prepareExternalOrganization(mainPoint.getColumnDdata(), NUMBER_ZERO))
                    .setCostInRubles(BigDecimal.ZERO)
                    .setComment(mainPoint.getColumnAaData())
                    .setTemperatureMin(toBigDecimal(mainPoint.getColumnLdata()))
                    .setTemperatureMax(toBigDecimal(mainPoint.getColumnMdata()))
                    .setSecondChamberTemperatureMin(toBigDecimal(mainPoint.getColumnOdata()))
                    .setSecondChamberTemperatureMax(toBigDecimal(mainPoint.getColumnPdata()))
                    .setTech(false)
                    .setDriver(ExternalDriverDto.init()
                            .setExternalId(mainPoint.getColumnAeData())//"Кочкин. В.В."
                            .setLastName(fioSplit[0])
                            .setFirstName(fioSplit[1])
                            .setMiddleName(fioSplit[2])
                            .build())
                    .setVehicle(prepareExternalVehicle(mainPoint.getColumnAcData()))
                    .setTrailer(prepareExternalVehicle(mainPoint.getColumnAdData()))
                    .setPoints(pointsDto)
                    .build();
            result.add(route);
        });
        return result;
    }

    private static ExternalPointDto preparePoint(ConvertedListDataV2 point) {
        return ExternalPointDto.init()
                .setExternalId(point.getColumnUdata())
                .setName(point.getColumnUdata())
                .setAddress(point.getColumnVdata())
                .setLat(toBigDecimal(point.getColumnYdata()))
                .setLon(toBigDecimal(point.getColumnZdata()))
                .build();
    }

    private static ExternalOrganizationDto prepareExternalOrganization(String name, String setExternalId) {
        return ExternalOrganizationDto.init()
                .setName(name)
                .setExternalId(setExternalId)
                .build();
    }

    private static ExternalVehicleDto prepareExternalVehicle(String data) {
        return EMPTY.equals(data) ? null :
                ExternalVehicleDto.init()
                        .setExternalId(data)
                        .setGosNumber(data)
                        .build();
    }

    private static LocalDateTime convert(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = instant.atZone(zoneId);
        return zdt.toLocalDateTime();
    }

    private static BigDecimal toBigDecimal(Integer value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    private static BigDecimal toBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    private static Float toFloat(Integer value) {
        return value == null ? null : Float.valueOf(value);
    }
}
