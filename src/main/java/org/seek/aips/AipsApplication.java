package org.seek.aips;

import org.seek.aips.models.TrafficCountDataPoint;
import org.seek.aips.parser.TrafficTimeSeriesParser;
import org.seek.aips.io.TrafficTimeSeriesReader;
import org.seek.aips.runner.TrafficAipsRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class AipsApplication {

	@Autowired
	private TrafficAipsRunner trafficAipsFileRunner;

	public static void main(String[] args) throws IOException {
		ApplicationContext context = SpringApplication.run(AipsApplication.class, args);
		AipsApplication app = context.getBean(AipsApplication.class);
		app.run();
	}

	private void run() throws IOException {
		trafficAipsFileRunner.run();
	}

}
