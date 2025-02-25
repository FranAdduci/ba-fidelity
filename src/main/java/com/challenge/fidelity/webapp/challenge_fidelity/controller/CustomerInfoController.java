package com.challenge.fidelity.webapp.challenge_fidelity.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.fidelity.webapp.challenge_fidelity.service.CustomerInfoService;

@RestController
@RequestMapping("/api/soap")
@CrossOrigin(origins = "*")
public class CustomerInfoController {

    private final CustomerInfoService customerInfoService;

    @Autowired
    public CustomerInfoController(CustomerInfoService customerInfoService) {
        this.customerInfoService = customerInfoService;
    }

    @PostMapping("/getInfo")    
    public ResponseEntity<?> getCustomerInfo(@RequestBody Map<String, String> request) {
        String sessionID = request.get("sessionID");
        String card = request.get("card");

        if (sessionID == null || sessionID.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: sessionID es requerido");
        }

        if (!"100".equals(card)) {
            return ResponseEntity.badRequest().body("Error: Tarjeta no existe");
        }

        Optional<String> response = customerInfoService.getInfo(sessionID);

        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al obtener la información del cliente"));
    }
}
