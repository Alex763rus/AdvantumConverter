package com.example.advantumconverter.service.database;

import com.example.advantumconverter.model.jpa.CommandStatProjection;
import com.example.advantumconverter.model.jpa.ConversionStatSnapshot;
import com.example.advantumconverter.model.jpa.ConversionStatSnapshotRepository;
import com.example.advantumconverter.model.jpa.HistoryActionRepository;
import com.example.advantumconverter.model.jpa.StatSnapshot;
import com.example.advantumconverter.model.jpa.StatSnapshotRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.advantumconverter.enums.HistoryActionType.USER_ACTION;
import static com.example.advantumconverter.enums.HistoryActionType.WEB_ACTION;
import static com.example.advantumconverter.enums.HistoryActionType.WEB_ERROR_ACTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticService {

    private static final int MAX_SNAPSHOTS = 100;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final HistoryActionRepository historyActionRepository;
    private final StatSnapshotRepository statSnapshotRepository;
    private final ConversionStatSnapshotRepository conversionStatSnapshotRepository;

    private volatile Map<String, List<SnapshotPoint>> commandCache = new HashMap<>();
    private volatile List<ConversionPoint> conversionCache = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            rebuildCaches();
        } catch (Exception ex) {
            log.warn("Не удалось построить кеш статистики при старте: {}", ex.getMessage());
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0,6,12,18 * * *")
    public synchronized void takeSnapshot() {
        log.info("Taking stat snapshot");
        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (CommandStatProjection stat : historyActionRepository.getCommandStatistics(USER_ACTION, WEB_ACTION)) {
            StatSnapshot snapshot = new StatSnapshot();
            snapshot.setSnapshotDate(now);
            snapshot.setMessageText(stat.getMessageText());
            snapshot.setTotalCount(orZero(stat.getTotalCount()));
            snapshot.setBotCount(orZero(stat.getBotCount()));
            snapshot.setWebCount(orZero(stat.getWebCount()));
            snapshot.setUserCount(orZero(stat.getUserCount()));
            statSnapshotRepository.save(snapshot);
        }
        trimSnapshots();

        ConversionStatSnapshot conversionSnapshot = new ConversionStatSnapshot();
        conversionSnapshot.setSnapshotDate(now);
        conversionSnapshot.setSuccessCount(historyActionRepository.countByActionType(WEB_ACTION));
        conversionSnapshot.setErrorCount(historyActionRepository.countByActionType(WEB_ERROR_ACTION));
        conversionStatSnapshotRepository.save(conversionSnapshot);
        trimConversionSnapshots();

        rebuildCaches();
        log.info("Stat snapshot taken");
    }

    @Transactional(readOnly = true)
    public Map<String, List<SnapshotPoint>> getCommandChartData() {
        return commandCache;
    }

    @Transactional(readOnly = true)
    public List<ConversionPoint> getConversionChartData() {
        return conversionCache;
    }

    private void rebuildCaches() {
        Map<String, List<SnapshotPoint>> commands = new HashMap<>();
        for (StatSnapshot snapshot : statSnapshotRepository.findAllByOrderBySnapshotDateAsc()) {
            SnapshotPoint point = new SnapshotPoint(
                    formatDate(snapshot.getSnapshotDate()),
                    orZero(snapshot.getTotalCount()),
                    orZero(snapshot.getBotCount()),
                    orZero(snapshot.getWebCount()),
                    orZero(snapshot.getUserCount()));
            commands.computeIfAbsent(snapshot.getMessageText(), k -> new ArrayList<>()).add(point);
        }
        commands.values().forEach(points -> points.sort(Comparator.comparing(SnapshotPoint::dateLabel)));
        commandCache = commands;

        List<ConversionPoint> conversions = new ArrayList<>();
        for (ConversionStatSnapshot snapshot : conversionStatSnapshotRepository.findAllByOrderBySnapshotDateAsc()) {
            conversions.add(new ConversionPoint(
                    formatDate(snapshot.getSnapshotDate()),
                    orZero(snapshot.getSuccessCount()),
                    orZero(snapshot.getErrorCount())));
        }
        conversionCache = conversions;
    }

    @Transactional
    protected void trimSnapshots() {
        List<Timestamp> toDelete = oldestOverLimit(statSnapshotRepository.findDistinctSnapshotDates());
        if (!toDelete.isEmpty()) {
            statSnapshotRepository.deleteBySnapshotDateIn(toDelete);
        }
    }

    @Transactional
    protected void trimConversionSnapshots() {
        List<Timestamp> toDelete = oldestOverLimit(conversionStatSnapshotRepository.findDistinctSnapshotDates());
        if (!toDelete.isEmpty()) {
            conversionStatSnapshotRepository.deleteBySnapshotDateIn(toDelete);
        }
    }

    private List<Timestamp> oldestOverLimit(List<Timestamp> dates) {
        if (dates.size() <= MAX_SNAPSHOTS) {
            return List.of();
        }
        return dates.stream()
                .sorted()
                .limit(dates.size() - MAX_SNAPSHOTS)
                .collect(Collectors.toList());
    }

    private String formatDate(Timestamp timestamp) {
        return timestamp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(FORMATTER);
    }

    private static long orZero(Long value) {
        return value == null ? 0L : value;
    }

    public record SnapshotPoint(String dateLabel, Long total, Long bot, Long web, Long user) {
    }

    public record ConversionPoint(String dateLabel, Long success, Long error) {
    }
}
