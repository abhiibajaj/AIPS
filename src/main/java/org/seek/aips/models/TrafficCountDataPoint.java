package org.seek.aips.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@EqualsAndHashCode
public class TrafficCountDataPoint {

    @Getter
    private LocalDateTime dateTime;

    @Getter
    private LocalDate date;
    @Getter
    private int carCount;

    public TrafficCountDataPoint(LocalDateTime dateTime, int carCount) {
        this.dateTime = dateTime;
        this.carCount = carCount;
    }

    public TrafficCountDataPoint(LocalDate date, int carCount) {
        this.date = date;
        this.carCount = carCount;
    }

    @Override
    public String toString() {
        if (dateTime != null) {
            return String.format("%s %s\n", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateTime), carCount);
        } else if (date != null) {
            return String.format("%s %s\n", date, carCount);
        }
        return null;
    }

}
