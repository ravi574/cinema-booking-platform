package com.cinema.service;

import com.cinema.dto.BookingRequest;
import com.cinema.exception.SeatAlreadyLockedException;
import com.cinema.exception.SeatNotAvailableException;
import com.cinema.kafka.EventPublisher;
import com.cinema.model.*;
import com.cinema.repository.*;
import com.cinema.strategy.PricingStrategy;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class BookingService {
    private final ShowSeatRepository seatRepo;
    private final BookingRepository bookingRepo;
    private final SeatLockService lockService;
    private final PricingStrategy pricingStrategy;
    private final EventPublisher publisher;

    public BookingService(ShowSeatRepository s, BookingRepository b, SeatLockService l, PricingStrategy p, EventPublisher pub) {
        this.seatRepo = s;
        this.bookingRepo = b;
        this.lockService = l;
        this.pricingStrategy = p;
        this.publisher = pub;
    }

    @Transactional
    public Booking book(BookingRequest req) {
        for (String seat : req.getSeats()) {
            if (!lockService.lockSeat(req.getShowId(), seat))
                throw new SeatAlreadyLockedException("Seat already locked: " + seat);
        }

        List<ShowSeat> seats = seatRepo.findByShowIdAndSeatNumberIn(req.getShowId(), req.getSeats());

        for (ShowSeat s : seats) {
            if (s.getStatus() != SeatStatus.AVAILABLE)
                throw new SeatNotAvailableException("Seat not available: " + s.getSeatNumber());
            s.setStatus(SeatStatus.LOCKED);
        }

        seatRepo.saveAll(seats);

        double total = pricingStrategy.calculate(200, seats.size(), LocalTime.now());

        Booking booking = new Booking();
        booking.setUserId(req.getUserId());
        booking.setShowId(req.getShowId());
        booking.setStatus("CONFIRMED");
        booking.setTotalAmount(total);
        bookingRepo.save(booking);

        for (ShowSeat s : seats) {
            s.setStatus(SeatStatus.BOOKED);
        }
        seatRepo.saveAll(seats);

        publisher.publish("booking-confirmed", booking.getId().toString());
        return booking;
    }
}