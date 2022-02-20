package com.nikitaaero.cloudwiremock.client;

import feign.Capability;
import feign.Client;

public class RequestToWiremockCapability implements Capability {

    private final String targetUrl;
    private final Client targetClient;

    public RequestToWiremockCapability(String targetUrl, Client targetClient) {
        this.targetUrl = targetUrl;
        this.targetClient = targetClient;
    }

    @Override
    public Client enrich(Client client) {
        return new HardCodedHostFirstClient(client, targetUrl, targetClient);
    }
}
