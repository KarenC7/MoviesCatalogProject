package com.catalog.movies.services;

import com.catalog.movies.model.Movie;
import com.catalog.movies.exception.ResourceNotFoundException;
import com.catalog.movies.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public Movie createMovie(Movie movie) {
        // Validate and save movie
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie updatedMovie) throws ResourceNotFoundException {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        // update fields and save
        movie.setName(updatedMovie.getName());
        movie.setSynopsis(updatedMovie.getSynopsis());
        movie.setReleaseYear(updatedMovie.getReleaseYear());
        movie.setCategories(updatedMovie.getCategories());
        movie.setPosterUrl(updatedMovie.getPosterUrl());
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public Page<Movie> getMovies(Specification<Movie> spec, Pageable pageable) {
        return movieRepository.findAll(spec, pageable);
    }
}