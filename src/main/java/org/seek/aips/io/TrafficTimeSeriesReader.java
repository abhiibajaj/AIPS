package org.seek.aips.io;

import org.seek.aips.models.TrafficCountDataPoint;

import java.io.IOException;
import java.util.List;

public interface TrafficTimeSeriesReader {

    public List<TrafficCountDataPoint> read() throws IOException;
}
