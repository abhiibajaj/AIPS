package org.seek.aips.io;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seek.aips.models.TrafficCountDataPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TrafficTimeSeriesFileReaderTest {

    @Autowired
    private TrafficTimeSeriesFileReader reader;

    @Test
    public void readValidFileInputReturnsExpectedDataPoints() throws IOException {

        // Arrange
        String testFileContent = "2021-12-01T05:00:00 5\n2021-12-01T05:30:00 12\n2021-12-01T06:00:00 14";
        Path testFile = Files.createTempFile("input", ".txt");
        reader.setInputFile(testFile.toString());
        Files.write(testFile, testFileContent.getBytes());


        // Act
        List<TrafficCountDataPoint> trafficCountDataPoints = reader.read();

        // Assert
        assertEquals(3, trafficCountDataPoints.size());

        assertEquals(LocalDateTime.of(2021, 12, 1, 5, 0, 0), trafficCountDataPoints.get(0).getDateTime());
        assertEquals(5, trafficCountDataPoints.get(0).getCarCount());

        assertEquals(LocalDateTime.of(2021, 12, 1, 5, 30, 0), trafficCountDataPoints.get(1).getDateTime());
        assertEquals(12, trafficCountDataPoints.get(1).getCarCount());

        assertEquals(LocalDateTime.of(2021, 12, 1, 6, 0, 0), trafficCountDataPoints.get(2).getDateTime());
        assertEquals(14, trafficCountDataPoints.get(2).getCarCount());

    }

    @Test
    public void fileNotFoundReturnsNoSuchFileException() throws IOException {

        // Arrange
       reader.setInputFile("invalidPath.txt");

        // Act & Assert
        assertThrows(NoSuchFileException.class, () -> reader.read());
    }
}
