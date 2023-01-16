# AIPS

## Introduction

This program reads a file, where each line contains a timestamp (in yyyy-mm-
ddThh:mm:ss format, i.e. ISO 8601) for the beginning of a half-hour and the number of cars seen that half hour. 

An example file can be found [here](io/input/input.txt). Clean input is presumed as these files are machine-generated.

The program outputs four files: 
- The number of cars seen in total
- A sequence of lines where each line contains a date (in yyyy-mm-dd format) and the number of cars seen on that day (eg. 2016-11-23 289) for all days listed in the input file.
- The top 3 half hours with most cars, in the same format as the input file
- The 1.5 hour period with least cars (i.e. 3 contiguous half hour records)

Example output files for the input example file can be found [here](io/output/).

## Getting Started
### Dependencies
- Apache Maven 3.8.6
- JDK 19

## Build and Develop

The tool chain is standard Maven. To build this project run:
```
mvn install
```
and install the JAR to the local Maven repository.

Load the `pom.xml` file into the IDE of choice.

## Testing

Testing locally can be done with Maven as well:

```
mvn clean test
```

## Running locally

The project can be run locally as below:

```
mvn spring-boot:run
```

By default, this will run the application over the example [input file](io/input/input.txt). The output will be appended to the files
in the [output directory](io/output/).

These input and output directory/files are externalised properties and can be changed as required in the `application.yml` file.

The relevant properties are:
```
traffic:
  aips:
    file:
      input: io/input/input.txt
      output:
        totalCars: io/output/totalCars.txt
        dailyTrafficCount: io/output/dailyTrafficCount.txt
        topThreeHalfHoursWithMostCars: io/output/topThreeHalfHoursWithMostCars.txt
        oneAndAHalfHourWindowWithLeastCars: io/output/oneAndAHalfHourWindowWithLeastCars.txt
```

## Deployment

There is no automated deployment pipleline configured for this project at the moment.


