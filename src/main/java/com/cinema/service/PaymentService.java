package com.cinema.service;

import com.cinema.dto.PaymentRequest;
import com.cinema.model.Payment;
import com.cinema.repository.PaymentRepository;
import com.cinema.kafka.BookingProducer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final BookingProducer producer;
    private final BookingService bookingService;

    public PaymentService(PaymentRepository repo, BookingProducer producer,BookingService bookingService) {
        this.paymentRepo = repo;
        this.producer = producer;
        this.bookingService=bookingService;
    }

    public Payment initiate(PaymentRequest request) {

        Payment payment = new Payment();
        payment.setBookingId(request.getBookingId());
        payment.setAmount(request.getAmount());
        payment.setStatus("INITIATED");

        return paymentRepo.save(payment);
    }

    public Payment success(Long paymentId) {

        Payment payment = paymentRepo.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus("SUCCESS");
        payment.setTransactionId(UUID.randomUUID().toString());

        paymentRepo.save(payment);
        // Notify system (Kafka)
        producer.send("payment-success:" + payment.getBookingId());

        return payment;
    }

    public Payment failure(Long paymentId) {

        Payment payment = paymentRepo.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus("FAILED");

        paymentRepo.save(payment);

        producer.send("payment-failed:" + payment.getBookingId());

        return payment;
    }
}