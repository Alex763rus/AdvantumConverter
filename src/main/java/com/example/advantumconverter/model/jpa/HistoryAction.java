package com.example.advantumconverter.model.jpa;

import com.example.advantumconverter.enums.HistoryActionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
public class HistoryAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyActionId;

    @Column(nullable = false)
    private Long chatIdFrom;
    @Column
    private Long chatIdTo;

    private String messageText;

    @Column
    private String callbackMenuName;

    @Column
    private Timestamp actionDate;

    @Column
    private String fileName;

    @Column
    private HistoryActionType actionType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryAction that = (HistoryAction) o;
        return Objects.equals(historyActionId, that.historyActionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyActionId);
    }
}
