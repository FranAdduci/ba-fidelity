package com.challenge.fidelity.webapp.challenge_fidelity.controller;

import java.util.HashMap;
import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.fidelity.webapp.challenge_fidelity.service.AuthService;

@RestController
@RequestMapping("/api/soap")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/synchroAndLogin")    
    public ResponseEntity<Map<String, String>> synchroAndLogin(@RequestBody Map<String, String> request) {
        String serialNumber = request.get("serialNumber");
        String username = request.get("username");
        String password = request.get("password");
        String foreignID = request.getOrDefault("foreignID", "");

        Optional<String> sessionID = authService.synchroAndLogin(serialNumber, username, password, foreignID);

        Map<String, String> response = new HashMap<>();
        return sessionID.map(id -> {
            response.put("sessionID", id);
            return ResponseEntity.ok(response);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "No se pudo obtener el SessionID")));
    }
}
