package com.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/login")
    public String login() {

        return "Response from Backend Port : " + port;
    }
}