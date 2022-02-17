package com.nikitaaero.cloudwiremock.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service")
public interface ServiceClient {

    @GetMapping("/sum")
    int sum(@RequestParam("a") int a, @RequestParam("b") int b);
}
