package com.cinema.strategy;

import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SmartPricingStrategy implements PricingStrategy {
    public double calculate(double price, int count, LocalTime time) {
        double total = price * count;
        if (count >= 3) total -= price * 0.5;
        if (time.isBefore(LocalTime.NOON)) total *= 0.8;
        return total;
    }
}