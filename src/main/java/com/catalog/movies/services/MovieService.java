package com.catalog.movies.services;

import com.catalog.movies.dto.MovieResponseDTO;
import com.catalog.movies.model.Movie;
import com.catalog.movies.exception.ResourceNotFoundException;
import com.catalog.movies.repository.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MovieResponseDTO createMovie(Movie movie) {
        return convertToDto(movieRepository.save(movie));
    }


    public MovieResponseDTO updateMovie(Long id, Movie updatedMovie) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Película no encontrada con id: " + id));
        movie.setName(updatedMovie.getName());
        movie.setSynopsis(updatedMovie.getSynopsis());
        movie.setReleaseYear(updatedMovie.getReleaseYear());
        movie.setCategories(updatedMovie.getCategories());
        movie.setPosterUrl(updatedMovie.getPosterUrl());
        // CreatedBy not updated
        return convertToDto(movieRepository.save(movie));
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found: id " + id);
        }
        movieRepository.deleteById(id);
    }

    public Page<MovieResponseDTO> getMovies(Specification<Movie> spec, Pageable pageable) {
        Page<Movie> movies = movieRepository.findAll(pageable);
        return movies.map(this::convertToDto);
    }

    public MovieResponseDTO convertToDto(Movie movie) {
        return modelMapper.map(movie, MovieResponseDTO.class);
    }
}