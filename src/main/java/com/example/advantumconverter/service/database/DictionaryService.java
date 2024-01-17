package com.example.advantumconverter.service.database;

import com.example.advantumconverter.exception.CarNotFoundException;
import com.example.advantumconverter.model.jpa.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Slf4j
@Service
public class DictionaryService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private LentaDictionaryRepository lentaDictionaryRepository;

    @Autowired
    private CarNumberRepository carNumberRepository;

    @Autowired
    private LentaCarRepository lentaCarRepository;

    @Autowired
    private OzonDictionaryRepository ozonDictionaryRepository;

    private HashSet<LentaDictionary> dictionary;
    private Set<Car> cars;
    private Set<CarNumber> carNumbers;
    private Set<LentaCar> lentaCars;
    private Set<OzonDictionary> ozonDictionaries;

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
        return carNumbers.stream().filter(e -> e.getCarNumber().equals(carNumberName))
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
//        val windowBefore = dictionaryWithStockBrief.stream()
//                .filter(dictionary -> hTime > getHour(dictionary.getStockInTime()))
//                .sorted((p1, p2) -> ((Integer) getHour(p2.getStockInTime())).compareTo((Integer) getHour(p1.getStockInTime())))
//                .findFirst().orElse(null);
//        if (windowBefore != null) {
//            return windowBefore;
//        }
        return dictionaryWithStockBrief.stream()
                .filter(dictionary -> hTime < getHour(dictionary.getStockInTime()))
                .sorted((p1, p2) -> ((Integer) getHour(p1.getStockInTime())).compareTo((Integer) getHour(p2.getStockInTime())))
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
