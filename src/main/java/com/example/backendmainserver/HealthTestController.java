package com.example.backendmainserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/health")
public class HealthTestController {
    @GetMapping("")
    public String apiHealthTest() {
        return "energiology!";
    }
}
