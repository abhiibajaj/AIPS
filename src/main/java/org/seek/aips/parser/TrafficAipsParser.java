package org.seek.aips.parser;

import org.seek.aips.models.TrafficCountDataPoint;
import org.seek.aips.models.TrafficFileResults;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TrafficAipsParser implements TrafficTimeSeriesParser {

    @Override
    public TrafficFileResults parse(List<TrafficCountDataPoint> trafficCountDataPoints) {

        int totalCars = calculateTotalCars(trafficCountDataPoints);
        List<TrafficCountDataPoint> dailyTrafficCount = calculateDailyTrafficCount(trafficCountDataPoints);
        List<TrafficCountDataPoint> topThreeHalfHoursWithMostCars = calculateTopThreeHalfHoursWithMostCars(trafficCountDataPoints);
        List<TrafficCountDataPoint> oneAndAHalfHourWindowWithLeastCars = calculateOneAndAHalfHourWindowWithLeastCars(trafficCountDataPoints);
        return new TrafficFileResults(totalCars, dailyTrafficCount, topThreeHalfHoursWithMostCars, oneAndAHalfHourWindowWithLeastCars);

    }

    private int calculateTotalCars(List<TrafficCountDataPoint> trafficCountDataPoints) {
        return trafficCountDataPoints.stream().mapToInt(TrafficCountDataPoint::getCarCount).sum();
    }

    private List<TrafficCountDataPoint> calculateDailyTrafficCount(List<TrafficCountDataPoint> trafficCountDataPoints) {


        Map<LocalDate, Integer> carsPerDate = trafficCountDataPoints.stream().collect(
                Collectors.groupingBy(
                    dataPoint -> dataPoint.getDateTime().toLocalDate(),
                    LinkedHashMap::new,
                    Collectors.summingInt(TrafficCountDataPoint::getCarCount)
                )
        );

        List<TrafficCountDataPoint> dailyTrafficCount =  carsPerDate.entrySet().stream()
                .map(entry -> new TrafficCountDataPoint(entry.getKey(), entry.getValue()))
                .toList();

        return dailyTrafficCount;
    }

    private List<TrafficCountDataPoint> calculateTopThreeHalfHoursWithMostCars(List<TrafficCountDataPoint> trafficCountDataPoints) {
        List<TrafficCountDataPoint> topThreeHalfHoursWithMostCars = trafficCountDataPoints.stream()
                .sorted(Comparator.comparingInt(TrafficCountDataPoint::getCarCount).reversed())
                .limit(3)
                .toList();
        return topThreeHalfHoursWithMostCars;
    }

    private List<TrafficCountDataPoint> calculateOneAndAHalfHourWindowWithLeastCars(List<TrafficCountDataPoint> trafficCountDataPoints) {

        int minCars = Integer.MAX_VALUE;
        List<TrafficCountDataPoint> minPeriod = null;

        List<TrafficCountDataPoint> currentPeriod = new ArrayList<>();

        for (int i = 0; i < trafficCountDataPoints.size(); i++) {

            handleNewDateAndHalfHourGap(trafficCountDataPoints, i, currentPeriod);
            addToPeriod(trafficCountDataPoints, i, currentPeriod);

            if (isContiguousPeriod(currentPeriod)) {

                int currentCarsInOneAndAHalfHourWindow = calculateTotalCars(currentPeriod);
                if (currentCarsInOneAndAHalfHourWindow < minCars) {
                    minPeriod = new ArrayList<>(currentPeriod);
                    minCars = currentCarsInOneAndAHalfHourWindow;

                }

            }
        }

        return minPeriod;

    }

    private void handleNewDateAndHalfHourGap(List<TrafficCountDataPoint> trafficCountDataPoints, int i, List<TrafficCountDataPoint> currentPeriod) {

        if (i == 0) {
            return;
        }

        TrafficCountDataPoint currentTrafficDataPoint = trafficCountDataPoints.get(i);
        TrafficCountDataPoint previousTrafficDataPoint = trafficCountDataPoints.get(i - 1);

        boolean isNewDate = !currentTrafficDataPoint.getDateTime().toLocalDate().isEqual(currentTrafficDataPoint.getDateTime().toLocalDate());
        boolean isNotAHalfHourGap = !currentTrafficDataPoint.getDateTime().isEqual(previousTrafficDataPoint.getDateTime().plusMinutes(30));

        if (isNewDate || isNotAHalfHourGap ) {
            currentPeriod.clear();
        }
    }

    private void addToPeriod(List<TrafficCountDataPoint> trafficCountDataPoints, int i, List<TrafficCountDataPoint> currentPeriod) {
        if (currentPeriod.size() < 3) {
            currentPeriod.add(trafficCountDataPoints.get(i));
        } else {
            currentPeriod.remove(0);
            currentPeriod.add(trafficCountDataPoints.get(i));
        }
    }

    private boolean isContiguousPeriod(List<TrafficCountDataPoint> currentPeriod) {
        return currentPeriod.size() == 3;
    }
}
