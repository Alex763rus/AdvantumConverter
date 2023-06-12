package com.example.advantumconverter.model.jpa;

import org.glassfish.grizzly.http.util.TimeStamp;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface HistoryActionRepository extends CrudRepository<HistoryAction, Long> {

    List<HistoryAction> findByChatIdFromEqualsAndActionDateAfter(Long chatIdFrom, Date actionDate);
    List<HistoryAction> findByChatIdToEqualsAndActionDateAfter(Long chatIdTo, Date actionDate);

}
