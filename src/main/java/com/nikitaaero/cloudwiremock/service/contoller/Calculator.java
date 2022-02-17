package com.nikitaaero.cloudwiremock.service.contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Calculator {

    @GetMapping(value = "/sum", produces = "application/json")
    Integer sum(@RequestParam("a") int a, @RequestParam("b") int b) {
        return a + b;
    }
}
