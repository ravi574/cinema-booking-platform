package com.cinema.controller;

import com.cinema.dto.PaymentRequest;
import com.cinema.model.Payment;
import com.cinema.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    // Step 1: Initiate payment
    @PostMapping("/initiate")
    public Payment initiate(@RequestBody PaymentRequest request) {
        return service.initiate(request);
    }

    // Step 2: Payment success callback
    @PostMapping("/{paymentId}/success")
    public Payment success(@PathVariable Long paymentId) {
        return service.success(paymentId);
    }

    // Step 3: Payment failure callback
    @PostMapping("/{paymentId}/failure")
    public Payment failure(@PathVariable Long paymentId) {
        return service.failure(paymentId);
    }
}