package com.cinema.service;

import com.cinema.dto.BookingRequest;
import com.cinema.exception.SeatAlreadyLockedException;
import com.cinema.exception.SeatNotAvailableException;
import com.cinema.model.Booking;
import com.cinema.model.SeatStatus;
import com.cinema.model.ShowSeat;
import com.cinema.repository.BookingRepository;
import com.cinema.repository.ShowSeatRepository;
import com.cinema.strategy.PricingStrategy;
import com.cinema.kafka.BookingProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public Booking cancelBooking(Long bookingId) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!"CONFIRMED".equals(booking.getStatus())) {
            throw new RuntimeException("Only confirmed bookings can be cancelled");
        }

        booking.setStatus("CANCELLED");

        // Release seats
        for (ShowSeat seat : booking.getSeats()) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seatRepo.save(seat);

            // also release Redis lock
            String key = "lock:" + seat.getId() + ":" + seat.getSeatNumber();
            lockService.release(seat.getShow().getId().toString());
        }

        bookingRepo.save(booking);
        return booking;
    }
}