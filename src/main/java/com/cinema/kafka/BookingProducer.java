package com.cinema.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingProducer {
    private final KafkaTemplate<String, String> kafka;

    public BookingProducer(KafkaTemplate<String, String> k) {
        this.kafka = k;
    }

    public void send(String msg) {
        kafka.send("booking-confirmed", msg);
    }
}