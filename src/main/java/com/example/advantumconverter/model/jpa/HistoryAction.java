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
@Entity(name = "history_action")
public class HistoryAction {

    @Id
    @Column(name = "history_action_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyActionId;

    @Column(name = "chat_id_from", nullable = false)
    private Long chatIdFrom;
    @Column(name = "chat_id_to")
    private Long chatIdTo;

    @Column(name = "messsage_text", length = 1000)
    private String messageText;

    @Column(name = "callback_menu_name")
    private String callbackMenuName;

    @Column(name = "action_date")
    private Timestamp actionDate;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "action_type")
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
