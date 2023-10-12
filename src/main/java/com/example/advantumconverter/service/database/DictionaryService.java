package com.example.advantumconverter.service.database;

import com.example.advantumconverter.exception.CarNotFoundException;
import com.example.advantumconverter.model.jpa.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

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

    private HashSet<LentaDictionary> dictionary;
    private Set<Car> cars;
    private Set<CarNumber> carNumbers;
    private Set<LentaCar> lentaCars;

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

}
