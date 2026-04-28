package com.cinema.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private Long showId;

    @ManyToOne
    private Show show;

    @ManyToMany
    @JoinTable(
            name = "booking_seats",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id")
    )
    private List<ShowSeat> seats;

    private Double totalAmount;

    private String status;
}