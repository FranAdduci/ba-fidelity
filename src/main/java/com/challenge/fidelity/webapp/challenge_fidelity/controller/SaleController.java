package com.challenge.fidelity.webapp.challenge_fidelity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.fidelity.webapp.challenge_fidelity.service.SaleService;

@RestController
@RequestMapping("/api/soap")
@CrossOrigin(origins = "*")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping("/venta")    
    public String Venta(
            @RequestParam String sessionID,
            @RequestParam long customerID,
            @RequestParam double totalMoney,
            @RequestParam String notes) {

        // Llamar al servicio para realizar la venta
        String result = saleService.saleByCard(sessionID, customerID, totalMoney, notes);
        return result;
    }
}
