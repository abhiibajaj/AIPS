package org.seek.aips.io;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.seek.aips.exceptions.InvalidFileFormatException;
import org.seek.aips.models.TrafficCountDataPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component @Slf4j
public class TrafficTimeSeriesFileReader implements TrafficTimeSeriesReader {

    @Value("${traffic.aips.file.input}")
    @Setter
    private String inputFile;

    @Override
    public List<TrafficCountDataPoint> read() throws IOException {
        Path path = Paths.get(inputFile);

        BufferedReader bufferedReader = Files.newBufferedReader(path);
        String st;
        List<TrafficCountDataPoint> trafficCountDataPoints = new ArrayList<>();
        while ((st = bufferedReader.readLine()) != null) {
            String[] timeAndCarCount = st.split(" ");
            try {
                trafficCountDataPoints.add(new TrafficCountDataPoint(LocalDateTime.parse(timeAndCarCount[0]), Integer.parseInt(timeAndCarCount[1])));

            } catch (NullPointerException e) {
                log.error("Invalid input file format detected.");
                throw new InvalidFileFormatException(e);
            } catch (ArrayIndexOutOfBoundsException e) {
                log.error("Invalid input file format detected.");
                throw new InvalidFileFormatException(e);
            }
        }
        return trafficCountDataPoints;
    }
}
