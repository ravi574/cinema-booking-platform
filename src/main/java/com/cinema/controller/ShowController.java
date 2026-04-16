package com.cinema.controller;

import com.cinema.model.*;
import com.cinema.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {
    private final ShowRepository repo;
    private final ShowSeatRepository seatRepo;

    public ShowController(ShowRepository r, ShowSeatRepository s) {
        this.repo = r;
        this.seatRepo = s;
    }

    @PostMapping
    public Show create(@RequestBody Show s) {
        return repo.save(s);
    }

    @GetMapping
    public List<Show> list() {
        return repo.findAll();
    }

    @GetMapping("/{id}/seats")
    public List<ShowSeat> seats(@PathVariable Long id) {
        return seatRepo.findByShowId(id);
    }
}