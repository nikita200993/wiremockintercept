package com.nikitaaero.cloudwiremock.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service2")
public interface Service2Client {

    @GetMapping("/mult")
    int mult(@RequestParam("a") int a, @RequestParam("b") int b);
}
