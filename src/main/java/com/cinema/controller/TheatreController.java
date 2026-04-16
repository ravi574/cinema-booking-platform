package com.cinema.controller;

import com.cinema.model.Theatre;
import com.cinema.repository.TheatreRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
public class TheatreController {
    private final TheatreRepository repo;

    public TheatreController(TheatreRepository r) {
        this.repo = r;
    }

    @PostMapping
    public Theatre create(@RequestBody Theatre t) {
        return repo.save(t);
    }

    @GetMapping
    public List<Theatre> list() {
        return repo.findAll();
    }
}