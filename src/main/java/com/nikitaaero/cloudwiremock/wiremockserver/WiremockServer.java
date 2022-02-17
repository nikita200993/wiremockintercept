package com.nikitaaero.cloudwiremock.wiremockserver;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.github.tomakehurst.wiremock.standalone.WireMockServerRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WiremockServer {

    public static void main(String[] args) {
        WireMockServerRunner.main(args);
        var wireMock = new WireMock(8080);
        wireMock.register(
                get("/service/sum")
                        .withQueryParam("a", new EqualToPattern("1"))
                        .withQueryParam("b", new EqualToPattern("2"))
                        .willReturn(
                                okJson("3")
                        )
        );
    }
}
