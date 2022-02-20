package com.nikitaaero.cloudwiremock.client;

import feign.Capability;
import feign.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty("intercept.wiremock.url")
public class CapabilitiesConfiguration {

    @Bean
    public Capability requestToWiremockCapability(@Value("${intercept.wiremock.url}") String wiremockUrl) {
        return new RequestToWiremockCapability(
                wiremockUrl,
                new Client.Default(null, null)
        );
    }
}
