package com.catalog.movies.services;

import com.catalog.movies.dto.RatingResponseDTO;
import com.catalog.movies.model.Movie;
import com.catalog.movies.model.Rating;
import com.catalog.movies.model.User;
import com.catalog.movies.exception.ResourceNotFoundException;
import com.catalog.movies.repository.MovieRepository;
import com.catalog.movies.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MovieRepository movieRepository;

    public RatingResponseDTO rateMovie(Long movieId, Long userId, Integer ratingValue) throws ResourceNotFoundException {
        if (ratingRepository.findByUserIdAndMovieId(userId, movieId).isPresent()) {
            throw new RuntimeException("Movie already rated by user");
        }
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        Rating rating = new Rating();
        rating.setMovie(movie);
        User user = new User();
        user.setId(userId);
        rating.setUser(user);
        rating.setRating(ratingValue);
        return convertToDto(ratingRepository.save(rating));
    }

    public void removeRating(Long movieId, Long userId) throws ResourceNotFoundException {
        Rating rating = ratingRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found"));
        ratingRepository.delete(rating);
    }

    public List<RatingResponseDTO> getUserRatings(Long userId) {
        List<Rating> ratings = ratingRepository.findByUserId(userId);
        return ratings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public RatingResponseDTO convertToDto(Rating rating) {
        RatingResponseDTO dto = new RatingResponseDTO();
        dto.setId(rating.getId());
        dto.setRating(rating.getRating());
        if (rating.getUser() != null) {
            dto.setUserId(rating.getUser().getId());
            dto.setUserEmail(rating.getUser().getEmail());
        }
        if (rating.getMovie() != null) {
            dto.setMovieId(rating.getMovie().getId());
            dto.setMovieName(rating.getMovie().getName());
        }
        return dto;
    }
}