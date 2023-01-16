package org.seek.aips.io;

import lombok.Setter;
import org.seek.aips.models.TrafficCountDataPoint;
import org.seek.aips.models.TrafficFileResults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Component
public class TrafficTimeSeriesFileWriter implements TrafficTimeSeriesWriter<TrafficFileResults> {


    @Value("${traffic.aips.file.output.totalCars}")
    @Setter
    private String totalCarsOutputFile;

    @Value("${traffic.aips.file.output.dailyTrafficCount}")
    @Setter
    private String dailyTrafficCountOutputFile;

    @Value("${traffic.aips.file.output.topThreeHalfHoursWithMostCars}")
    @Setter
    private String topThreeHalfHoursWithMostCarsOutputFile;

    @Value("${traffic.aips.file.output.oneAndAHalfHourWindowWithLeastCars}")
    @Setter
    private String oneAndAHalfHourWindowWithLeastCarsOutputFile;

    @Override
    public void write(TrafficFileResults results) throws IOException {
        writeTotalCarsToFile(results);
        writeDailyTrafficCount(results);
        writeTopThreeHalfHoursWithMostCars(results);
        writeOneAndAHalfHourWindowWithLeastCars(results);

    }

    private void writeTotalCarsToFile(TrafficFileResults results) throws IOException {

        Path path = Paths.get(totalCarsOutputFile);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND,
                StandardOpenOption.CREATE)) {
            String totalCars = String.format("%d\n", results.getTotalCars());
            writer.write(totalCars);
        }
    }

    private void writeDailyTrafficCount(TrafficFileResults results) throws IOException {
        writeTrafficDataPoints(dailyTrafficCountOutputFile, results.getDailyTrafficCount());
    }

    private void writeTopThreeHalfHoursWithMostCars(TrafficFileResults results) throws IOException {
        writeTrafficDataPoints(topThreeHalfHoursWithMostCarsOutputFile, results.getTopThreeHalfHoursWithMostCars());
    }

    private void writeOneAndAHalfHourWindowWithLeastCars(TrafficFileResults results) throws IOException {
        writeTrafficDataPoints(oneAndAHalfHourWindowWithLeastCarsOutputFile, results.getOneAndAHalfHourWindowWithLeastCars());
    }

    private void writeTrafficDataPoints(String filePath, List<TrafficCountDataPoint> trafficCountDataPoints) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND,
                StandardOpenOption.CREATE)) {

            for (var entry : trafficCountDataPoints) {
                writer.write(entry.toString());
            }

        }
    }

}
