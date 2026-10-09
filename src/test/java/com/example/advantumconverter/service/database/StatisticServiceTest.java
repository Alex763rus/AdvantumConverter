package com.example.advantumconverter.service.database;

import com.example.advantumconverter.enums.HistoryActionType;
import com.example.advantumconverter.model.jpa.CommandStatProjection;
import com.example.advantumconverter.model.jpa.ConversionStatSnapshot;
import com.example.advantumconverter.model.jpa.ConversionStatSnapshotRepository;
import com.example.advantumconverter.model.jpa.HistoryActionRepository;
import com.example.advantumconverter.model.jpa.StatSnapshot;
import com.example.advantumconverter.model.jpa.StatSnapshotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.advantumconverter.enums.HistoryActionType.USER_ACTION;
import static com.example.advantumconverter.enums.HistoryActionType.WEB_ACTION;
import static com.example.advantumconverter.enums.HistoryActionType.WEB_ERROR_ACTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StatisticServiceTest {

    @Mock
    private HistoryActionRepository historyActionRepository;
    @Mock
    private StatSnapshotRepository statSnapshotRepository;
    @Mock
    private ConversionStatSnapshotRepository conversionStatSnapshotRepository;

    private StatisticService service;

    @BeforeEach
    void setUp() {
        service = new StatisticService(historyActionRepository, statSnapshotRepository, conversionStatSnapshotRepository);
    }

    private CommandStatProjection projection(String message, Long total, Long bot, Long web, Long user) {
        var projection = org.mockito.Mockito.mock(CommandStatProjection.class);
        when(projection.getMessageText()).thenReturn(message);
        when(projection.getTotalCount()).thenReturn(total);
        when(projection.getBotCount()).thenReturn(bot);
        when(projection.getWebCount()).thenReturn(web);
        when(projection.getUserCount()).thenReturn(user);
        return projection;
    }

    private List<Timestamp> dates(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> new Timestamp(1000L * i))
                .collect(Collectors.toList());
    }

    @Test
    void init_buildsEmptyCaches() {
        when(statSnapshotRepository.findAllByOrderBySnapshotDateAsc()).thenReturn(List.of());
        when(conversionStatSnapshotRepository.findAllByOrderBySnapshotDateAsc()).thenReturn(List.of());

        service.init();

        assertThat(service.getCommandChartData()).isEmpty();
        assertThat(service.getConversionChartData()).isEmpty();
    }

    @Test
    void init_repositoryFails_doesNotThrow() {
        when(statSnapshotRepository.findAllByOrderBySnapshotDateAsc())
                .thenThrow(new RuntimeException("db down"));

        service.init();

        assertThat(service.getCommandChartData()).isEmpty();
    }

    @Test
    void init_buildsCachesFromSnapshots() {
        var snapshot = new StatSnapshot();
        snapshot.setSnapshotDate(new Timestamp(1000L));
        snapshot.setMessageText("cmd");
        snapshot.setTotalCount(3L);
        snapshot.setBotCount(1L);
        snapshot.setWebCount(2L);
        snapshot.setUserCount(2L);
        when(statSnapshotRepository.findAllByOrderBySnapshotDateAsc()).thenReturn(List.of(snapshot));

        var conversion = new ConversionStatSnapshot();
        conversion.setSnapshotDate(new Timestamp(1000L));
        conversion.setSuccessCount(1L);
        conversion.setErrorCount(0L);
        when(conversionStatSnapshotRepository.findAllByOrderBySnapshotDateAsc()).thenReturn(List.of(conversion));

        service.init();

        assertThat(service.getCommandChartData()).containsKey("cmd");
        assertThat(service.getCommandChartData().get("cmd")).hasSize(1);
        assertThat(service.getConversionChartData()).hasSize(1);
    }

    @Test
    void takeSnapshot_savesSnapshots() {
        var projection = projection("cmd", null, 1L, 2L, 3L);
        when(historyActionRepository.getCommandStatistics(USER_ACTION, WEB_ACTION))
                .thenReturn(List.of(projection));
        when(historyActionRepository.countByActionType(WEB_ACTION)).thenReturn(5L);
        when(historyActionRepository.countByActionType(WEB_ERROR_ACTION)).thenReturn(2L);
        when(statSnapshotRepository.findDistinctSnapshotDates()).thenReturn(List.of());
        when(conversionStatSnapshotRepository.findDistinctSnapshotDates()).thenReturn(List.of());
        when(statSnapshotRepository.findAllByOrderBySnapshotDateAsc()).thenReturn(List.of());
        when(conversionStatSnapshotRepository.findAllByOrderBySnapshotDateAsc()).thenReturn(List.of());

        service.takeSnapshot();

        verify(statSnapshotRepository).save(any(StatSnapshot.class));
        verify(conversionStatSnapshotRepository).save(any(ConversionStatSnapshot.class));
        verify(statSnapshotRepository, never()).deleteBySnapshotDateIn(any());
        verify(conversionStatSnapshotRepository, never()).deleteBySnapshotDateIn(any());
    }

    @Test
    void takeSnapshot_trimsExcessSnapshots() {
        when(historyActionRepository.getCommandStatistics(USER_ACTION, WEB_ACTION)).thenReturn(List.of());
        when(statSnapshotRepository.findDistinctSnapshotDates()).thenReturn(dates(101));
        when(conversionStatSnapshotRepository.findDistinctSnapshotDates()).thenReturn(dates(101));
        when(statSnapshotRepository.findAllByOrderBySnapshotDateAsc()).thenReturn(List.of());
        when(conversionStatSnapshotRepository.findAllByOrderBySnapshotDateAsc()).thenReturn(List.of());

        service.takeSnapshot();

        var captor = ArgumentCaptor.forClass(List.class);
        verify(statSnapshotRepository).deleteBySnapshotDateIn(captor.capture());
        assertThat(captor.getValue()).hasSize(1);
        verify(conversionStatSnapshotRepository).deleteBySnapshotDateIn(any());
    }
}
