package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/admin/health-test")
    public ResponseEntity<String> healthTest() {
        return ResponseEntity.ok("health-test OK");
    }
}
