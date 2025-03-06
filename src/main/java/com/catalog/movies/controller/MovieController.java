package com.catalog.movies.controller;

import com.catalog.movies.exception.ResourceNotFoundException;
import com.catalog.movies.model.Movie;
import com.catalog.movies.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // ADMIN only endpoint: create movie
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        Movie savedMovie = movieService.createMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    // ADMIN only endpoint: update movie
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @Valid @RequestBody Movie movie) throws ResourceNotFoundException {
        Movie updatedMovie = movieService.updateMovie(id, movie);
        return ResponseEntity.ok(updatedMovie);
    }

    // ADMIN only endpoint: delete movie
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    // List movies with filtering, searching, pagination and ordering
    @GetMapping
    public ResponseEntity<Page<Movie>> listMovies(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<String> category,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        // Validate how to build specification dynamically for search and filtering
        Specification<Movie> spec = Specification.where(null);
     /*
        Object MovieSpecifications;
        if (search != null && !search.isEmpty()) {
            spec = spec.and(MovieSpecifications.nameOrSynopsisContains(search));
        }
        if (category != null && !category.isEmpty()) {
            spec = spec.and(MovieSpecifications.hasCategories(category));
        }
        if (releaseYear != null) {
            spec = spec.and(MovieSpecifications.releaseYearEquals(releaseYear));
        }*/
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Movie> moviesPage = movieService.getMovies(spec, pageable);
        return ResponseEntity.ok(moviesPage);
    }
}