package com.cinema.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookingConsumer {
    @KafkaListener(topics = "booking-confirmed", groupId = "grp")
    public void consume(String msg) {
        System.out.println("Notification sent for booking: " + msg);
    }
}