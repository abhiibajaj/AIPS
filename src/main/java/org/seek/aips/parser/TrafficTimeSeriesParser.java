package org.seek.aips.parser;

import org.seek.aips.models.TrafficCountDataPoint;
import org.seek.aips.models.TrafficFileResults;

import java.util.List;

public interface TrafficTimeSeriesParser {

    public TrafficFileResults parse(List<TrafficCountDataPoint> trafficCountDataPointList);
}
