package com.example.advantumconverter.rest;

import com.example.advantumconverter.service.database.StatisticService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticControllerTest {

    @Mock
    private StatisticService statisticService;

    @InjectMocks
    private StatisticController controller;

    @Test
    void showStatistics_returnsView() {
        assertThat(controller.showStatistics()).isEqualTo("admin/statistics");
    }

    @Test
    void getCommandsData_returnsCache() {
        when(statisticService.getCommandChartData()).thenReturn(Map.of());

        var response = controller.getCommandsData();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void getConversionsData_returnsCache() {
        when(statisticService.getConversionChartData()).thenReturn(List.of());

        var response = controller.getConversionsData();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void takeSnapshotNow_delegates() {
        var response = controller.takeSnapshotNow();

        assertThat(response.getBody()).isEqualTo("Снимок статистики сделан");
        verify(statisticService).takeSnapshot();
    }
}
