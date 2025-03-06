package com.catalog.movies.repository;

import com.catalog.movies.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserIdAndMovieId(Long userId, Long movieId);
    List<Rating> findByUserId(Long userId);
}
