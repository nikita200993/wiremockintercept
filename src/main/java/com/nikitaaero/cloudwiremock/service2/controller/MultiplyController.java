package com.nikitaaero.cloudwiremock.service2.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MultiplyController {

    @GetMapping(value = "/mult", produces = MediaType.APPLICATION_JSON_VALUE)
    public int mult(@RequestParam("a") int a, @RequestParam("b") int b) {
        return a * b;
    }
}
