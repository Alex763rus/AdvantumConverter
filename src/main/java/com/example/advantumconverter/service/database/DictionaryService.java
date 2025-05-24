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
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    private HashSet<LentaDictionary> dictionary;
    private Set<Car> cars;
    private Set<CarNumber> carNumbers;
    private Set<LentaCar> lentaCars;
    private Set<OzonDictionary> ozonDictionaries;
    private Set<OzonTransitTime> ozonTransitTimes;
    private Set<OzonLoadUnloadTime> ozonLoadUnloadTime;
    private Set<OzonTonnageTime> ozonTonnageTime;

    private Set<MetroAddressesDictionary> metroAddressesDictionaries;
    private Set<MetroTemperatureDictionary> metroTemperatureDictionaries;
    private Set<MetroTimeDictionary> metroTimeDictionaries;
    private Set<MetroDcAddressesDictionary> metroDcAddressesDictionaries;
    private Set<SberAddressDictionary> sberAddressDictionaries;
    private Set<LentaTsCity> lentaTsCitiesDictionaries;

    @PostConstruct
    public void init() {
        reloadDictionary();
    }

    public void reloadDictionary() {
        val addressesIter = lentaDictionaryRepository.findAll();
        dictionary = new HashSet<>();
        addressesIter.forEach(dictionary::add);

        val carsIter = carRepository.findAll();
        cars = new HashSet<>();
        carsIter.forEach(cars::add);

        val carNumbersIter = carNumberRepository.findAll();
        carNumbers = new HashSet<>();
        carNumbersIter.forEach(carNumbers::add);

        val lentaCarIter = lentaCarRepository.findAll();
        lentaCars = new HashSet<>();
        lentaCarIter.forEach(lentaCars::add);

        val ozonDictionaryIter = ozonDictionaryRepository.findAll();
        ozonDictionaries = new HashSet<>();
        ozonDictionaryIter.forEach(ozonDictionaries::add);

        val ozonTransitTimeIter = ozonTransitTimeRepository.findAll();
        ozonTransitTimes = new HashSet<>();
        ozonTransitTimeIter.forEach(ozonTransitTimes::add);

        val ozonLoadUnloadTimeIter = ozonLoadUnloadTimeRepository.findAll();
        ozonLoadUnloadTime = new HashSet<>();
        ozonLoadUnloadTimeIter.forEach(ozonLoadUnloadTime::add);

        val ozonTonnageTimeIter = ozonTonnageTimeRepository.findAll();
        ozonTonnageTime = new HashSet<>();
        ozonTonnageTimeIter.forEach(ozonTonnageTime::add);

        metroAddressesDictionaries = new HashSet<>();
        metroAddressesDictionaryRepository.findAll()
                .forEach(metroAddressesDictionaries::add);

        metroDcAddressesDictionaries = new HashSet<>();
        metroDcAddressesDictionaryRepository.findAll()
                .forEach(metroDcAddressesDictionaries::add);

        sberAddressDictionaries = new HashSet<>();
        sberAddressDictionaryRepository.findAll()
                .forEach(sberAddressDictionaries::add);

        metroTemperatureDictionaries = new HashSet<>();
        metroTemperatureDictionaryRepository.findAll()
                .forEach(metroTemperatureDictionaries::add);

        metroTimeDictionaries = new HashSet<>();
        metroTimeDictionaryRepository.findAll()
                .forEach(metroTimeDictionaries::add);

        lentaTsCitiesDictionaries = new HashSet<>();
        lentaTsCityRepository.findAll()
                .forEach(lentaTsCitiesDictionaries::add);
    }

    public Car getCar(final String carName) {
        return cars.stream().filter(e -> e.getCarName().equals(carName))
                .findFirst().orElseThrow(() -> new CarNotFoundException(carName));
    }

    public Car getCarOrElse(final String carName, final Car car) {
        return cars.stream().filter(e -> e.getCarName().equals(carName))
                .findFirst().orElse(car);
    }

    public CarNumber getCarNumberOrElse(final String carNumberName, final CarNumber carNumber) {
        return carNumbers.stream().filter(e -> e.getNumber().equals(carNumberName))
                .findFirst().orElse(carNumber);
    }

    public LentaCar getLentaCarOrElse(final String carNumberName, final LentaCar lentaCar) {
        return lentaCars.stream().filter(e -> e.getCarNumber().equals(carNumberName))
                .findFirst().orElse(lentaCar);
    }

    public LentaDictionary getDictionary(final long lentaDictionaryKey) {
        return dictionary.stream()
                .filter(e -> e.getLentaDictionaryKey() == lentaDictionaryKey)
                .findFirst().orElse(null);
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

    public String getOzonTonnageTime(Long tonnage) {
        val timeFind = ozonTonnageTime.stream()
                .filter(key -> key.getTonnage().equals(tonnage))
                .findFirst().orElse(null);
        return timeFind == null ? null : timeFind.getTime();
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

    public Integer getOzonLoadUnloadTime(String arrival) {
        val timeFind = ozonLoadUnloadTime.stream()
                .filter(key -> key.getArrival().equals(arrival))
                .findFirst().orElse(null);
        return timeFind == null ? null : timeFind.getUnloadTime();
    }


    private MetroTemperatureDictionary getMetroTemperatureDictionary(String code) {
        return metroTemperatureDictionaries.stream()
                .filter(key -> key.getTemperatureId().equals(code))
                .findFirst().orElse(null);
    }

    public String getTsCityBrief(String ts) {
        var tsCityBrief = lentaTsCitiesDictionaries.stream()
                .filter(key -> key.getTs().equals(ts))
                .findFirst().orElse(null);
        return tsCityBrief == null ? null : tsCityBrief.getCityBrief();
    }

    public Long getMetroMinTemperature(String code) {
        val dictionary = getMetroTemperatureDictionary(code);
        return dictionary == null ? null : dictionary.getMinTemperature();
    }

    public Long getMetroMaxTemperature(String code) {
        val dictionary = getMetroTemperatureDictionary(code);
        return dictionary == null ? null : dictionary.getMaxTemperature();
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

    public SberAddressDictionary getSberCity(String address) {
        return sberAddressDictionaries.stream()
                .filter(sberAddressDictionary -> sberAddressDictionary.getAddress()
                        .equals(address))
                .findFirst().orElse(null);
    }


    public String getMetroTimeEnd(Long timeId, Set<String> codes) {
        val dictionary = getMetroTimeDictionary(timeId, codes);
        return dictionary == null ? null : dictionary.getTimeEnd();
    }

    public String getMetroTimeDictionary(String code) {
        val dictionary = metroAddressesDictionaries.stream()
                .filter(key -> key.getAddressesId().equals(code))
                .findFirst().orElse(null);
        return dictionary == null ? null : dictionary.getAddressesName();
    }

    public String getMetroDcAddressName(String addressesId, String defaultValue) {
        val dictionary = getMetroDcAddressDictionary(addressesId);
        return dictionary == null ? defaultValue : dictionary.getAddressesName();
    }

    public String getMetroDcAddressBrief(String addressesId, String defaultValue) {
        val dictionary = getMetroDcAddressDictionary(addressesId);
        return dictionary == null ? defaultValue : dictionary.getAddressesBrief();
    }

    private MetroDcAddressesDictionary getMetroDcAddressDictionary(String addressesId) {
        return metroDcAddressesDictionaries.stream()
                .filter(key -> key.getAddressesId().equals(addressesId))
                .findFirst().orElse(null);
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
