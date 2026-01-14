package com.example.advantumconverter.service.database;

import com.example.advantumconverter.exception.CarNotFoundException;
import com.example.advantumconverter.model.jpa.Car;
import com.example.advantumconverter.model.jpa.CarNumber;
import com.example.advantumconverter.model.jpa.CarNumberRepository;
import com.example.advantumconverter.model.jpa.CarRepository;
import com.example.advantumconverter.model.jpa.lenta.*;
import com.example.advantumconverter.model.jpa.metro.*;
import com.example.advantumconverter.model.jpa.ozon.*;
import com.example.advantumconverter.model.jpa.sber.SberAddressDictionary;
import com.example.advantumconverter.model.jpa.sber.SberAddressDictionaryRepository;
import com.example.advantumconverter.model.jpa.siel.SielCars;
import com.example.advantumconverter.model.jpa.siel.SielCarsRepository;
import com.example.advantumconverter.model.jpa.siel.SielPoints;
import com.example.advantumconverter.model.jpa.siel.SielPointsRepository;
import com.example.advantumconverter.model.jpa.spar.SparWindows;
import com.example.advantumconverter.model.jpa.spar.SparWindowsRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplSiel.SIEL_COMPANY_NAME;
import static org.example.tgcommons.utils.DateConverterUtils.TEMPLATE_TIME;
import static org.example.tgcommons.utils.DateConverterUtils.convertDateFormat;

@Slf4j
@Service
@AllArgsConstructor
public class DictionaryService {

    private final CarRepository carRepository;
    private final LentaDictionaryRepository lentaDictionaryRepository;
    private final CarNumberRepository carNumberRepository;
    private final LentaCarRepository lentaCarRepository;
    private final SielPointsRepository sielPointsRepository;
    private final SielCarsRepository sielCarsRepository;
    private final OzonDictionaryRepository ozonDictionaryRepository;
    private final OzonTransitTimeRepository ozonTransitTimeRepository;
    private final OzonLoadUnloadTimeRepository ozonLoadUnloadTimeRepository;
    private final OzonTonnageTimeRepository ozonTonnageTimeRepository;
    private final MetroTimeDictionaryRepository metroTimeDictionaryRepository;
    private final MetroTemperatureDictionaryRepository metroTemperatureDictionaryRepository;
    private final MetroAddressesDictionaryRepository metroAddressesDictionaryRepository;
    private final MetroDcAddressesDictionaryRepository metroDcAddressesDictionaryRepository;
    private final SberAddressDictionaryRepository sberAddressDictionaryRepository;
    private final LentaTsCityRepository lentaTsCityRepository;
    private final SparWindowsRepository sparWindowsRepository;

    private Map<Long, LentaDictionary> lentaDictionaries;
    private Map<String, Car> cars;
    private Map<String, CarNumber> carNumbers;
    private Map<String, LentaCar> lentaCars;
    private Set<OzonDictionary> ozonDictionaries;
    private Set<OzonTransitTime> ozonTransitTimes;
    private Map<String, OzonLoadUnloadTime> ozonLoadUnloadTime;
    private Map<Long, OzonTonnageTime> ozonTonnageTime;
    private Map<String, SielPoints> sielPoints;
    private Map<String, SielCars> sielCars;
    private Map<String, SparWindows> sparWindows;

    private Map<String, MetroAddressesDictionary> metroAddressesDictionaries;
    private Map<String, MetroTemperatureDictionary> metroTemperatureDictionaries;
    private Set<MetroTimeDictionary> metroTimeDictionaries;
    private Map<String, MetroDcAddressesDictionary> metroDcAddressesDictionaries;
    private Map<String, SberAddressDictionary> sberAddressDictionaries;
    private Map<String, LentaTsCity> lentaTsCitiesDictionaries;

    @PostConstruct
    public void init() {
        reloadDictionary();
    }

    public void reloadDictionary() {
        val addressesIter = lentaDictionaryRepository.findAll();
        lentaDictionaries = new HashMap<>();
        addressesIter.forEach(e -> lentaDictionaries.put(e.getLentaDictionaryKey(), e));

        val carsIter = carRepository.findAll();
        cars = new HashMap<>();
        carsIter.forEach(e -> cars.put(e.getCarName(), e));

        val carNumbersIter = carNumberRepository.findAll();
        carNumbers = new HashMap<>();
        carNumbersIter.forEach(e -> carNumbers.put(e.getNumber(), e));

        val lentaCarIter = lentaCarRepository.findAll();
        lentaCars = new HashMap<>();
        lentaCarIter.forEach(e -> lentaCars.put(e.getCarNumber(), e));

        val sielPointsIter = sielPointsRepository.findAll();
        sielPoints = new HashMap<>();
        sielPointsIter.forEach(e -> sielPoints.put(e.getPointName(), e));

        val sielCarsIter = sielCarsRepository.findAll();
        sielCars = new LinkedCaseInsensitiveMap<>();
        sielCarsIter.forEach(e -> sielCars.put(e.getCarNumber(), e));

        val sparWindowsIter = sparWindowsRepository.findAll();
        sparWindows = new HashMap<>();
        sparWindowsIter.forEach(e -> sparWindows.put(e.getPointName(), e));

        val ozonDictionaryIter = ozonDictionaryRepository.findAll();
        ozonDictionaries = new HashSet<>();
        ozonDictionaryIter.forEach(ozonDictionaries::add);

        val ozonTransitTimeIter = ozonTransitTimeRepository.findAll();
        ozonTransitTimes = new HashSet<>();
        ozonTransitTimeIter.forEach(ozonTransitTimes::add);

        val ozonLoadUnloadTimeIter = ozonLoadUnloadTimeRepository.findAll();
        ozonLoadUnloadTime = new HashMap<>();
        ozonLoadUnloadTimeIter.forEach(e -> ozonLoadUnloadTime.put(e.getArrival(), e));

        val ozonTonnageTimeIter = ozonTonnageTimeRepository.findAll();
        ozonTonnageTime = new HashMap<>();
        ozonTonnageTimeIter.forEach(e -> ozonTonnageTime.put(e.getTonnage(), e));

        metroAddressesDictionaries = new HashMap<>();
        metroAddressesDictionaryRepository.findAll()
                .forEach(e -> metroAddressesDictionaries.put(e.getAddressesId(), e));

        metroDcAddressesDictionaries = new HashMap<>();
        metroDcAddressesDictionaryRepository.findAll()
                .forEach(e -> metroDcAddressesDictionaries.put(e.getAddressesId(), e));

        sberAddressDictionaries = new HashMap<>();
        sberAddressDictionaryRepository.findAll()
                .forEach(e -> sberAddressDictionaries.put(e.getAddress(), e));

        metroTemperatureDictionaries = new HashMap<>();
        metroTemperatureDictionaryRepository.findAll()
                .forEach(e -> metroTemperatureDictionaries.put(e.getTemperatureId(), e));

        metroTimeDictionaries = new HashSet<>();
        metroTimeDictionaryRepository.findAll()
                .forEach(metroTimeDictionaries::add);

        lentaTsCitiesDictionaries = new HashMap<>();
        lentaTsCityRepository.findAll()
                .forEach(e -> lentaTsCitiesDictionaries.put(e.getTs(), e));
    }


    private Optional<Car> getCar(final String carName) {
        return Optional.ofNullable(cars.get(carName));
    }

    public Car getCarOrElseThrow(final String carName) {
        return getCar(carName)
                .orElseThrow(() -> new CarNotFoundException(carName));
    }

    public Car getCarOrElse(final String carName, final Car car) {
        return getCar(carName).orElse(car);
    }

    public Optional<CarNumber> getCarNumber(final String carNumberName) {
        return Optional.ofNullable(carNumbers.get(carNumberName));
    }

    public Optional<LentaCar> getLentaCar(final String carNumberName) {
        return Optional.ofNullable(lentaCars.get(carNumberName));
    }

    @Cacheable("getLentaDictionaries")
    public LentaDictionary getLentaDictionaries(final long lentaDictionaryKey) {
        return lentaDictionaries.get(lentaDictionaryKey);
    }

    public Optional<SielPoints> getSielPoint(final String pointName) {
        return Optional.ofNullable(sielPoints.get(pointName));
    }

    public Optional<SparWindows> getSparWindows(final String pointName) {
        return Optional.ofNullable(sparWindows.get(pointName));
    }

    public String getSielCarrierName(final String carNumber) {
        return Optional.ofNullable(sielCars.get(carNumber))
                .map(SielCars::getCarrierName)
                .orElse(SIEL_COMPANY_NAME);
    }

    public OzonDictionary getBestDictionary(final String stockBrief, int hTime) {
        val dictionaryWithStockBrief = ozonDictionaries.stream()
                .filter(dictionary -> dictionary.getStockBrief().equals(stockBrief))
                .collect(Collectors.toList());

        val windowDictionary = dictionaryWithStockBrief.stream()
                .filter(dictionary -> hTime >= getHour(dictionary.getStockInTime()))
                .filter(dictionary -> getHour(dictionary.getStockOutTime()) == 0 || hTime < getHour(dictionary.getStockOutTime()))
                .findFirst().orElse(null);
        if (windowDictionary != null) {
            return windowDictionary;
        }
        return dictionaryWithStockBrief.stream()
                .filter(dictionary -> hTime < getHour(dictionary.getStockInTime()))
                .sorted((p1, p2) -> ((Integer) getHour(p1.getStockInTime())).compareTo((Integer) getHour(p2.getStockInTime())))
                .findFirst().orElse(null);
    }

    public Optional<String> getOzonTonnageTime(Long tonnage) {
        return Optional.ofNullable(ozonTonnageTime.get(tonnage))
                .map(OzonTonnageTime::getTime);
    }

    public String getOzonTransitTime(String departure, String arrival) {
        val timeFind = ozonTransitTimes.stream()
                .filter(key ->
                        (key.getOzonTransitTimeKey().getArrival().equals(arrival))
                                && (key.getOzonTransitTimeKey().getDeparture().equals(departure))
                )
                .findFirst().orElse(null);
        return timeFind == null ? null : timeFind.getTransitTime();
    }

    public Optional<Integer> getOzonLoadUnloadTime(String arrival) {
        return Optional.ofNullable(ozonLoadUnloadTime.get(arrival))
                .map(OzonLoadUnloadTime::getUnloadTime);
    }


    private Optional<MetroTemperatureDictionary> getMetroTemperatureDictionary(String code) {
        return Optional.ofNullable(metroTemperatureDictionaries.get(code));
    }

    public String getTsCityBrief(String ts) {
        return Optional.ofNullable(lentaTsCitiesDictionaries.get(ts))
                .map(LentaTsCity::getCityBrief)
                .orElse(null);
    }

    public Long getMetroMinTemperature(String code) {
        return getMetroTemperatureDictionary(code)
                .map(MetroTemperatureDictionary::getMinTemperature)
                .orElse(null);
    }

    public Long getMetroMaxTemperature(String code) {
        return getMetroTemperatureDictionary(code)
                .map(MetroTemperatureDictionary::getMaxTemperature)
                .orElse(null);
    }

    private MetroTimeDictionary getMetroTimeDictionary(Long timeId, Set<String> codes) {
        val dictionaries = metroTimeDictionaries.stream()
                .filter(dict -> dict.getMetroTimeDictionaryKey().getTimeId().equals(timeId))
                .filter(dict -> codes.contains(dict.getMetroTimeDictionaryKey().getCode()))
                .sorted(Comparator.comparing(p -> (p.getMetroTimeDictionaryKey().getPriority())))
                .toList();
        return dictionaries.isEmpty() ? null : dictionaries.get(0);
    }

    public String getMetroTimeStart(Long timeId, Set<String> codes) {
        val dictionary = getMetroTimeDictionary(timeId, codes);
        return dictionary == null ? null : dictionary.getTimeStart();
    }

    public Optional<SberAddressDictionary> getSberCity(String address) {
        return Optional.ofNullable(sberAddressDictionaries.get(address));
    }


    public String getMetroTimeEnd(Long timeId, Set<String> codes) {
        val dictionary = getMetroTimeDictionary(timeId, codes);
        return dictionary == null ? null : dictionary.getTimeEnd();
    }

    public String getMetroTimeDictionary(String code) {
        return Optional.ofNullable(metroAddressesDictionaries.get(code))
                .map(MetroAddressesDictionary::getAddressesName)
                .orElse(null);
    }

    public String getMetroDcAddressName(String addressesId, String defaultValue) {
        return getMetroDcAddressDictionary(addressesId)
                .map(MetroDcAddressesDictionary::getAddressesName)
                .orElse(defaultValue);
    }

    public String getMetroDcAddressBrief(String addressesId, String defaultValue) {
        return getMetroDcAddressDictionary(addressesId)
                .map(MetroDcAddressesDictionary::getAddressesBrief)
                .orElse(defaultValue);
    }

    private Optional<MetroDcAddressesDictionary> getMetroDcAddressDictionary(String addressesId) {
        return Optional.ofNullable(metroDcAddressesDictionaries.get(addressesId));
    }

    private int getHour(String time) {
        try {
            val date = convertDateFormat(time, TEMPLATE_TIME);
            return date.getHours();
        } catch (ParseException e) {
            throw new RuntimeException("Ошибка при сравнении времени справочника озона: " + e);
        }
    }
}
