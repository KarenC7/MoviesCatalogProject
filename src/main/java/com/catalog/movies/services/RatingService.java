package com.catalog.movies.services;

import com.catalog.movies.model.Movie;
import com.catalog.movies.model.Rating;
import com.catalog.movies.model.User;
import com.catalog.movies.exception.ResourceNotFoundException;
import com.catalog.movies.repository.MovieRepository;
import com.catalog.movies.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MovieRepository movieRepository;

    public Rating rateMovie(Long movieId, Long userId, Integer ratingValue) throws ResourceNotFoundException {
        // Ensure the user hasn't already rated the movie.
        if (ratingRepository.findByUserIdAndMovieId(userId, movieId).isPresent()) {
            throw new RuntimeException("Movie already rated by user");
        }
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        Rating rating = new Rating();
        rating.setMovie(movie);
        // In production, you should retrieve User entity from the security context.
        User user = new User();
        user.setId(userId);
        rating.setUser(user);
        rating.setRating(ratingValue);
        return ratingRepository.save(rating);
    }

    public void removeRating(Long movieId, Long userId) throws ResourceNotFoundException {
        Rating rating = ratingRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found"));
        ratingRepository.delete(rating);
    }

    public List<Rating> getUserRatings(Long userId) {
        return ratingRepository.findByUserId(userId);
    }
}