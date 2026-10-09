package com.example.advantumconverter.rest;

import com.example.advantumconverter.service.database.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/statistics")
    public String showStatistics() {
        return "admin/statistics";
    }

    @GetMapping("/statistics/commands-data")
    @ResponseBody
    public ResponseEntity<Map<String, List<StatisticService.SnapshotPoint>>> getCommandsData() {
        return ResponseEntity.ok(statisticService.getCommandChartData());
    }

    @GetMapping("/statistics/conversions-data")
    @ResponseBody
    public ResponseEntity<List<StatisticService.ConversionPoint>> getConversionsData() {
        return ResponseEntity.ok(statisticService.getConversionChartData());
    }

    @PostMapping("/statistics/snapshot")
    @ResponseBody
    public ResponseEntity<String> takeSnapshotNow() {
        statisticService.takeSnapshot();
        return ResponseEntity.ok("Снимок статистики сделан");
    }
}
