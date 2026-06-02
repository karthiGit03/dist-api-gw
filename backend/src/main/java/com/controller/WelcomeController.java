package com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WelcomeController {

    @GetMapping("/login")
    public Map<String, Object> login() throws Exception {

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Response from backend");
        response.put("instance", InetAddress.getLocalHost().getHostName());
        response.put("timestamp", System.currentTimeMillis());

        return response;
    }
}