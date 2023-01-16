package org.seek.aips.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class TrafficFileResults {

    @Getter
    private int totalCars;
    @Getter
    private List<TrafficCountDataPoint> dailyTrafficCount;
    @Getter
    private List<TrafficCountDataPoint> topThreeHalfHoursWithMostCars;
    @Getter
    private List<TrafficCountDataPoint> oneAndAHalfHourWindowWithLeastCars;

}
