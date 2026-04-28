package com.cinema.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Theatre {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String city;

    @OneToMany(mappedBy = "theatre")
    private List<Show> shows;
}