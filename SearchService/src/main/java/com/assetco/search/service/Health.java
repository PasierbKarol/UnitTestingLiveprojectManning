package com.assetco.search.service;

import org.springframework.web.bind.annotation.*;

@RestController()
public class Health {
    @GetMapping(value = "/health/status", produces = { "application/json" })
    public String check() {
        System.out.println("Checking health");
        return "{\"up\":true}";
    }
}
