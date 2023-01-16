package org.seek.aips.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seek.aips.models.TrafficCountDataPoint;
import org.seek.aips.models.TrafficFileResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TrafficAipsParserTest {

    @Autowired
    private TrafficAipsParser parser;

    public List<TrafficCountDataPoint> generateSeedDataPoints() {
        List<TrafficCountDataPoint> seedDataPoints = new ArrayList<>();

        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 5, 0,0), 5));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 5, 30,0), 12));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 6, 0,0), 14));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 6, 30,0), 15));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 7, 0,0), 25));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 7, 30,0), 46));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 8, 0,0), 42));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 15, 0,0), 9));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 15, 30,0), 11));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 23, 30,0), 0));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 5, 9, 30,0), 18));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 5, 10, 30,0), 15));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 5, 11, 30,0), 7));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 5, 12, 30,0), 6));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 5, 13, 30,0), 9));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 5, 14, 30,0), 11));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 5, 15, 30,0), 15));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 8, 18, 0,0), 33));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 8, 19, 0,0), 28));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 8, 20, 0,0), 25));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 8, 21, 0,0), 21));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 8, 22, 0,0), 16));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 8, 23, 0,0), 11));
        seedDataPoints.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 9, 0, 0,0), 4));

        return seedDataPoints;

    }

    @Test
    public void calculateTotalCarsSuccessfully() {

        // Arrange
        List<TrafficCountDataPoint> seedDataPoints = generateSeedDataPoints();

        // Act
        TrafficFileResults results = parser.parse(seedDataPoints);

        // Assert
        int totalCars = results.getTotalCars();
        assertEquals(398, totalCars);

    }

    @Test
    public void calculateDailyTrafficCountSuccessfully() {

        // Arrange
        List<TrafficCountDataPoint> seedDataPoints = generateSeedDataPoints();
        List<TrafficCountDataPoint> dailyTrafficCountExpected = new ArrayList<>();
        dailyTrafficCountExpected.add(new TrafficCountDataPoint(LocalDate.of(2021, 12, 1), 179));
        dailyTrafficCountExpected.add(new TrafficCountDataPoint(LocalDate.of(2021, 12, 5), 81));
        dailyTrafficCountExpected.add(new TrafficCountDataPoint(LocalDate.of(2021, 12, 8), 134));
        dailyTrafficCountExpected.add(new TrafficCountDataPoint(LocalDate.of(2021, 12, 9), 4));

        // Act
        TrafficFileResults results = parser.parse(seedDataPoints);

        // Assert
        List<TrafficCountDataPoint> dailyTrafficCount = results.getDailyTrafficCount();
        assertNotNull(dailyTrafficCount);
        assertEquals(dailyTrafficCountExpected, dailyTrafficCount);

    }

    @Test
    public void calculateTopThreeHalfHoursWithMostCarsSuccessfully() {

        // Arrange
        List<TrafficCountDataPoint> seedDataPoints = generateSeedDataPoints();
        List<TrafficCountDataPoint> topThreeHalfHoursWithMostCarsExpected = new ArrayList<>();
        topThreeHalfHoursWithMostCarsExpected.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 7, 30, 0), 46));
        topThreeHalfHoursWithMostCarsExpected.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 8, 0, 0), 42));
        topThreeHalfHoursWithMostCarsExpected.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 8, 18, 0, 0), 33));

        // Act
        TrafficFileResults results = parser.parse(seedDataPoints);

        // Assert
        List<TrafficCountDataPoint> topThreeHalfHoursWithMostCars = results.getTopThreeHalfHoursWithMostCars();
        assertNotNull(topThreeHalfHoursWithMostCars);
        assertEquals(topThreeHalfHoursWithMostCarsExpected, topThreeHalfHoursWithMostCars);

    }

    @Test
    public void calculateOneAndAHalfHourWindowWithLeastCarsSuccessfully() {

        // Arrange
        List<TrafficCountDataPoint> seedDataPoints = generateSeedDataPoints();
        List<TrafficCountDataPoint> oneAndAHalfHourWindowWithLeastCarsExpected = new ArrayList<>();
        oneAndAHalfHourWindowWithLeastCarsExpected.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 5, 0, 0), 5));
        oneAndAHalfHourWindowWithLeastCarsExpected.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 5, 30, 0), 12));
        oneAndAHalfHourWindowWithLeastCarsExpected.add(new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 6, 0, 0), 14));

        // Act
        TrafficFileResults results = parser.parse(seedDataPoints);

        // Assert
        List<TrafficCountDataPoint> oneAndAHalfHourWindowWithLeastCars = results.getOneAndAHalfHourWindowWithLeastCars();
        assertNotNull(oneAndAHalfHourWindowWithLeastCars);
        assertEquals(oneAndAHalfHourWindowWithLeastCarsExpected, oneAndAHalfHourWindowWithLeastCars);

    }

}
