package dev.steps.prob101.gate_filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class GateFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(GateFilterApplication.class, args);
	}

}
