package com.nikitaaero.cloudwiremock.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@SpringBootApplication
public class ServiceClientRunner {

    private final ServiceClient serviceClient;

    public ServiceClientRunner(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceClientRunner.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return (args) -> {
            System.out.println(serviceClient.sum(2, 2));
            System.out.println(serviceClient.sum(1, 2));
        };
    }
}
