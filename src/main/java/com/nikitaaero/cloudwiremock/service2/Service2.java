package com.nikitaaero.cloudwiremock.service2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Service2 {

    public static void main(String[] args) {
        SpringApplication.run(Service2.class, args);
    }
}
