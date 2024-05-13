package com.example.advantumconverter.model.pojo.converter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
public class BookerListKey {
    private String counterparty; //Контрагент
    private String carNumber; //Гос.номер
    private String inn; //ИНН

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookerListKey listKey = (BookerListKey) o;
        return counterparty.equals(listKey.counterparty) && carNumber.equals(listKey.carNumber) && inn.equals(listKey.inn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(counterparty, carNumber, inn);
    }
}
