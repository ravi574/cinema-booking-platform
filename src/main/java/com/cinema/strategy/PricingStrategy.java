package com.cinema.strategy;

import java.time.LocalTime;

public interface PricingStrategy {
    double calculate(double basePrice, int seats, LocalTime showTime);
}