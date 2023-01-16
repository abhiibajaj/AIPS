package org.seek.aips.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seek.aips.models.TrafficCountDataPoint;
import org.seek.aips.models.TrafficFileResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TrafficTimeSeriesFileWriterTest {

    @Autowired
    private TrafficTimeSeriesFileWriter writer;

    private String OUTPUT_DIRECTORY = "src/test/resources/io/output";

    @AfterEach
    public void cleanUp() {
        File dir = new File(OUTPUT_DIRECTORY);
        for (File file : dir.listFiles()) {
            file.delete();
        }
    }

    @Test
    public void writeTotalCarsToFileSuccessfully() throws IOException {

        // Arrange
        String totalCarsOutputFile = OUTPUT_DIRECTORY + "/totalCars.txt";

        TrafficFileResults results = new TrafficFileResults(398, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        writer.setTotalCarsOutputFile(totalCarsOutputFile);

        // Act
        writer.write(results);

        // Assert
        Path path = Paths.get(totalCarsOutputFile);
        assertTrue(Files.exists(path));
        String content = new String(Files.readAllBytes(path));
        assertEquals("398\n", content);

    }

    @Test
    public void writeDailyTrafficCountToFileSuccessfully() throws IOException {

        // Arrange
        String dailyTrafficCountOutputFile = OUTPUT_DIRECTORY + "/dailyTrafficCount.txt";

        writer.setDailyTrafficCountOutputFile(dailyTrafficCountOutputFile);

        TrafficCountDataPoint dataPoint1 = new TrafficCountDataPoint(LocalDate.of(2021, 12, 1), 179);
        TrafficCountDataPoint dataPoint2 = new TrafficCountDataPoint(LocalDate.of(2021, 12, 5), 81);
        TrafficCountDataPoint dataPoint3 = new TrafficCountDataPoint(LocalDate.of(2021, 12, 8), 134);
        TrafficCountDataPoint dataPoint4 = new TrafficCountDataPoint(LocalDate.of(2021, 12, 9), 4);
        List<TrafficCountDataPoint> dailyTrafficCount = List.of(dataPoint1, dataPoint2, dataPoint3, dataPoint4);

        TrafficFileResults results = new TrafficFileResults(398, dailyTrafficCount, new ArrayList<>(), new ArrayList<>());


        // Act
        writer.write(results);

        // Assert
        Path path = Paths.get(dailyTrafficCountOutputFile);
        assertTrue(Files.exists(path));
        String content = new String(Files.readAllBytes(path));
        assertEquals("2021-12-01 179\n" +
                "2021-12-05 81\n" +
                "2021-12-08 134\n" +
                "2021-12-09 4\n",
                content);

    }

    @Test
    public void writeTopThreeHalfHoursWithMostCarsToFileSuccessfully() throws IOException {

        // Arrange
        String topThreeHalfHourWindowWithMostCarsOutputFile = OUTPUT_DIRECTORY + "/topThreeHalfHourWindowWithMostCars.txt";

        writer.setTopThreeHalfHoursWithMostCarsOutputFile(topThreeHalfHourWindowWithMostCarsOutputFile);

        TrafficCountDataPoint dataPoint1 = new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 7, 30, 0), 46);
        TrafficCountDataPoint dataPoint2 = new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 8, 0, 0), 42);
        TrafficCountDataPoint dataPoint3 = new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 8, 18, 0, 0), 33);

        List<TrafficCountDataPoint> topThreeHalfHourWindowsWithMostCars = List.of(dataPoint1, dataPoint2, dataPoint3);

        TrafficFileResults results = new TrafficFileResults(398, new ArrayList<>(), topThreeHalfHourWindowsWithMostCars, new ArrayList<>());


        // Act
        writer.write(results);

        // Assert
        Path path = Paths.get(topThreeHalfHourWindowWithMostCarsOutputFile);
        assertTrue(Files.exists(path));
        String content = new String(Files.readAllBytes(path));
        assertEquals("2021-12-01T07:30:00 46\n" +
                        "2021-12-01T08:00:00 42\n" +
                        "2021-12-08T18:00:00 33\n",
                content);

    }

    @Test
    public void writeOneAndAHalfHourWindowWithLeastCarsSuccessfully() throws IOException {

        // Arrange
        String oneAndAHalfHourWindowsWithLeastCarsOutputFile = OUTPUT_DIRECTORY + "/oneAndAHalfHourWindowsWithLeastCars.txt";

        writer.setOneAndAHalfHourWindowWithLeastCarsOutputFile(oneAndAHalfHourWindowsWithLeastCarsOutputFile);

        TrafficCountDataPoint dataPoint1 = new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 5, 0, 0), 5);
        TrafficCountDataPoint dataPoint2 = new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 5, 30, 0), 12);
        TrafficCountDataPoint dataPoint3 = new TrafficCountDataPoint(LocalDateTime.of(2021, 12, 1, 6, 0, 0), 14);

        List<TrafficCountDataPoint> oneAndAHalfHourWindowsWithLeastCars = List.of(dataPoint1, dataPoint2, dataPoint3);

        TrafficFileResults results = new TrafficFileResults(398, new ArrayList<>(), new ArrayList<>(), oneAndAHalfHourWindowsWithLeastCars);


        // Act
        writer.write(results);

        // Assert
        Path path = Paths.get(oneAndAHalfHourWindowsWithLeastCarsOutputFile);
        assertTrue(Files.exists(path));
        String content = new String(Files.readAllBytes(path));
        assertEquals("2021-12-01T05:00:00 5\n" +
                        "2021-12-01T05:30:00 12\n" +
                        "2021-12-01T06:00:00 14\n",
                content);


    }

}
