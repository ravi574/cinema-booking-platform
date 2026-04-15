package com.cinema.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ShowSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long showId;
    private String seatNumber;
    @Enumerated(EnumType.STRING)
    private SeatStatus status;
}