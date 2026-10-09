package com.example.advantumconverter.model.jpa;

import com.example.advantumconverter.enums.HistoryActionType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryActionRepository extends CrudRepository<HistoryAction, Long> {

    long countByActionType(HistoryActionType actionType);

    @Query("select ha.messageText as messageText, " +
            "count(ha) as totalCount, " +
            "sum(case when ha.actionType = :botType then 1 else 0 end) as botCount, " +
            "sum(case when ha.actionType = :webType then 1 else 0 end) as webCount, " +
            "count(distinct ha.chatIdFrom) as userCount " +
            "from HistoryAction ha " +
            "where ha.actionType in (:botType, :webType) " +
            "and ha.messageText like '/%' " +
            "and ha.messageText not in ('/start', '/faq', '/setting_user') " +
            "group by ha.messageText")
    List<CommandStatProjection> getCommandStatistics(@Param("botType") HistoryActionType botType,
                                                     @Param("webType") HistoryActionType webType);
}
