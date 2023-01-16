package org.seek.aips.runner;

import org.seek.aips.io.TrafficTimeSeriesReader;
import org.seek.aips.io.TrafficTimeSeriesWriter;
import org.seek.aips.models.TrafficCountDataPoint;
import org.seek.aips.models.TrafficFileResults;
import org.seek.aips.parser.TrafficTimeSeriesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class TrafficAipsFileRunner implements TrafficAipsRunner {
    @Autowired
    private TrafficTimeSeriesReader trafficTimeSeriesFileReader;

    @Autowired
    private TrafficTimeSeriesWriter trafficTimeSeriesFileWriter;

    @Autowired
    private TrafficTimeSeriesParser trafficAipsParser;

    public void run() {
        try {
            List<TrafficCountDataPoint> trafficCountDataPoints = trafficTimeSeriesFileReader.read();
            TrafficFileResults results = trafficAipsParser.parse(trafficCountDataPoints);
            trafficTimeSeriesFileWriter.write(results);
        } catch (IOException e) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
