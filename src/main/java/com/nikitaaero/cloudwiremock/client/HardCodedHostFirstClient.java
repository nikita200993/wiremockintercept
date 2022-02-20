package com.nikitaaero.cloudwiremock.client;

import feign.Client;
import feign.Request;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

class HardCodedHostFirstClient implements Client {

    private static final Logger logger = LoggerFactory.getLogger(HardCodedHostFirstClient.class);

    private final Client delegate;
    private final String targetUrl;
    private final Client clientForTargetHost;

    HardCodedHostFirstClient(Client delegate, String targetUrl, Client clientForTargetHost) {
        this.delegate = delegate;
        this.targetUrl = targetUrl;
        this.clientForTargetHost = clientForTargetHost;
    }

    @Override
    public Response execute(Request originalRequest, Request.Options options) {
        return formNewRequest(originalRequest)
                .flatMap(newRequest -> executeTarget(newRequest, options))
                .filter(response -> response.status() >= 200 && response.status() < 300)
                .orElseGet(() -> executeRethrowingAsRuntime(originalRequest, options));
    }

    private Optional<Request> formNewRequest(Request originalRequest) {
        try {
            URI originalUri = new URI(originalRequest.url());
            URI targetHostUri = new URI(targetUrl);
            URI expandedTargetUri = createTargetUri(originalUri, targetHostUri);
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

    private URI createTargetUri(URI originalUri, URI targetUri) throws URISyntaxException {
        return new URI(
                targetUri.getScheme() == null ? originalUri.getScheme() : targetUri.getScheme(),
                originalUri.getUserInfo(),
                targetUri.getHost(),
                targetUri.getPort() == -1 ? 8080 : targetUri.getPort(),
                "/" + originalUri.getHost() + originalUri.getPath(),
                originalUri.getQuery(),
                originalUri.getFragment()
        );
    }

    private Optional<Response> executeTarget(Request request, Request.Options options) {
        try {
            return Optional.ofNullable(clientForTargetHost.execute(request, options));
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
