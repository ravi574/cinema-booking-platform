package com.cinema.repository;
import com.cinema.model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
 List<ShowSeat> findByShowIdAndSeatNumberIn(Long showId, List<String> seats);
}