package com.cinema.controller;

import com.cinema.model.Movie;
import com.cinema.repository.MovieRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieRepository repo;

    public MovieController(MovieRepository r) {
        this.repo = r;
    }

    @PostMapping
    public Movie create(@RequestBody Movie m) {
        return repo.save(m);
    }

    @GetMapping
    public List<Movie> list() {
        return repo.findAll();
    }
}