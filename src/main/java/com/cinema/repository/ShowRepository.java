package com.cinema.repository;

import com.cinema.model.SeatStatus;
import com.cinema.model.Show;
import com.cinema.model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByCity(String city);
    List<ShowSeat> findByShowId(Long showId);

    List<ShowSeat> findByShowIdAndSeatNumberIn(Long showId, List<String> seats);

    List<ShowSeat> findByShowIdAndStatus(Long showId, SeatStatus status);

    @Query("SELECT s FROM ShowSeat s WHERE s.show.id = :showId AND s.status = 'AVAILABLE'")
    List<ShowSeat> findAvailableSeats(@Param("showId") Long showId);
}