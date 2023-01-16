package org.seek.aips.io;

import java.io.IOException;

public interface TrafficTimeSeriesWriter<T> {

    public void write(T results) throws Exception;

}
