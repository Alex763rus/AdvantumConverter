package com.example.advantumconverter.service.database;

import com.example.advantumconverter.exception.CarNotFoundException;
import com.example.advantumconverter.model.jpa.Car;
import com.example.advantumconverter.model.jpa.CarRepository;
import com.example.advantumconverter.model.jpa.LentaDictionary;
import com.example.advantumconverter.model.jpa.LentaDictionaryRepository;
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

    private HashSet<LentaDictionary> dictionary;
    private Set<Car> cars;

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
    }

    public Car getCar(final String carName) {
        return cars.stream().filter(e -> e.getCarName().equals(carName))
                .findFirst().orElseThrow(() -> new CarNotFoundException(carName));
    }
    public Car getCarOrElse(final String carName, final Car car) {
        return cars.stream().filter(e -> e.getCarName().equals(carName))
                .findFirst().orElse(car);
    }

    public LentaDictionary getDictionary(final long lentaDictionaryKey) {
        return dictionary.stream()
                .filter(e -> e.getLentaDictionaryKey() == lentaDictionaryKey)
                .findFirst().orElse(null);
    }

}
