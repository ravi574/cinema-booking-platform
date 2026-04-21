package com.cinema.service;

import com.cinema.dto.BookingRequest;
import com.cinema.exception.SeatAlreadyLockedException;
import com.cinema.exception.SeatNotAvailableException;
import com.cinema.model.*;
import com.cinema.repository.*;
import com.cinema.strategy.PricingStrategy;
import com.cinema.kafka.BookingProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BookingService {
    private final ShowSeatRepository seatRepo;
    private final BookingRepository bookingRepo;
    private final SeatLockService lockService;
    private final PricingStrategy pricing;
    private final BookingProducer producer;

    public BookingService(ShowSeatRepository s, BookingRepository b, SeatLockService l,
                          PricingStrategy p, BookingProducer pr) {
        this.seatRepo = s;
        this.bookingRepo = b;
        this.lockService = l;
        this.pricing = p;
        this.producer = pr;
    }

    @Transactional
    public Booking book(BookingRequest req) {
        for (String seat : req.getSeats()) {
            if (!lockService.lock("lock:" + req.getShowId() + ":" + seat))
                throw new SeatAlreadyLockedException("Seat locked " + seat);
        }
        List<ShowSeat> seats = seatRepo.findByShowIdAndSeatNumberIn(req.getShowId(), req.getSeats());
        for (ShowSeat s : seats) {
            if (s.getStatus() != SeatStatus.AVAILABLE)
                throw new SeatNotAvailableException("Seat not available " + s.getSeatNumber());
            s.setStatus(SeatStatus.BOOKED);
        }
        seatRepo.saveAll(seats);
        Booking b = new Booking();
        b.setUserId(req.getUserId());
        b.setShowId(req.getShowId());
        b.setTotalAmount(pricing.calculate(200, seats.size()));
        b.setStatus("PENDING_PAYMENT");
        bookingRepo.save(b);
        producer.send(b.getId().toString());
        return b;
    }
}