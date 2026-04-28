package com.cinema.controller;

import com.cinema.dto.BookingRequest;
import com.cinema.model.Booking;
import com.cinema.service.BookingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService service;

    public BookingController(BookingService s) {
        this.service = s;
    }

    @PostMapping
    public Booking book(@RequestBody BookingRequest req) {
        return service.book(req);
    }

    @PostMapping("/{bookingId}/cancel")
    public Booking cancelBooking(@PathVariable Long bookingId) {
        return service.cancelBooking(bookingId);
    }
}