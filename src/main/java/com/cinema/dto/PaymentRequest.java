package com.cinema.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long bookingId;
    private Double amount;
}