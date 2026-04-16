package com.cinema.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
}