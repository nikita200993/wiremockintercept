package com.nikitaaero.cloudwiremock.client;

import feign.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Configuration(proxyBeanMethods = false)
public class CapabilitiesConfiguration {

    private final String wireMockUrl;

    public CapabilitiesConfiguration(@Value("${wiremock.url}") String wireMockUrl) {
        this.wireMockUrl = wireMockUrl;
    }


    @Bean
    public Capability wrapClient() {
        return new HardcodedTargetCapability();
    }

    private static class HardCodedHostFirstClient implements Client {

        private static final Logger logger = LoggerFactory.getLogger(HardCodedHostFirstClient.class);

        private final Client delegate;
        private final String targetUrl;
        private final String serviceName;

        private HardCodedHostFirstClient(Client delegate, String url, String serviceName) {
            this.delegate = delegate;
            this.targetUrl = url;
            this.serviceName = serviceName;
        }

        @Override
        public Response execute(Request originalRequest, Request.Options options) {
            return formNewRequest(originalRequest)
                    .flatMap(newRequest -> executeCatching(newRequest, options))
                    .filter(response -> response.status() >= 200 && response.status() < 300)
                    .orElseGet(() -> executeRethrowingAsRuntime(originalRequest, options));
        }

        private Optional<Request> formNewRequest(Request originalRequest) {
            try {
                URI originalUri = new URI(originalRequest.url());
                URI targetUri = new URI(targetUrl);
                URI expandedTargetUri = new URI(
                        targetUri.getScheme() == null ? originalUri.getScheme() : targetUri.getScheme(),
                        originalUri.getUserInfo(),
                        targetUri.getHost(),
                        targetUri.getPort() == -1 ? 8080 : targetUri.getPort(),
                        "/" + serviceName + originalUri.getPath(),
                        originalUri.getQuery(),
                        originalUri.getFragment()
                );
                return Optional.of(
                        Request.create(
                                originalRequest.httpMethod(),
                                expandedTargetUri.toString(),
                                originalRequest.headers(),
                                Request.Body.create(originalRequest.body()),
                                originalRequest.requestTemplate()
                        )
                );

            } catch (Exception ex) {
                logger.error("Failed to transform request {}.", originalRequest, ex);
                return Optional.empty();
            }
        }

        private Optional<Response> executeCatching(Request request, Request.Options options) {
            try {
                return Optional.ofNullable(delegate.execute(request, options));
            } catch (Exception ex) {
                logger.error("Failed to execute request.", ex);
                return Optional.empty();
            }
        }

        private Response executeRethrowingAsRuntime(Request request, Request.Options options) {
            try {
                return delegate.execute(request, options);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public class HardcodedTargetCapability implements Capability {
        @Override
        public Client enrich(Client client) {
            return new HardCodedHostFirstClient(client, wireMockUrl, "service");
        }
    }
}
