package com.example.barun.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
    String name = "Barun";
    @GetMapping("/")
    public String healthCheck(){
        return "Hey, " + name + "! Working good.";
    }
}
