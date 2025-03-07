package com.catalog.movies.controller;

import com.catalog.movies.dto.MovieResponseDTO;
import com.catalog.movies.model.Movie;
import com.catalog.movies.model.User;
import com.catalog.movies.services.MovieService;
import com.catalog.movies.services.UserService;
import com.catalog.movies.specification.MovieSpecifications;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final UserService userService;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    public MovieController(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    @Operation(summary = "Get movie catalog", description = "Return movie catalog with pages and filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "No authorized")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MovieResponseDTO> createMovie(@Valid @RequestBody Movie movie) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
        movie.setCreatedBy(user);
        MovieResponseDTO savedMovie = movieService.createMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @Operation(summary = "Update a movie from catalog", description = "Return movie updated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "No authorized")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MovieResponseDTO> updateMovie(@PathVariable Long id, @Valid @RequestBody Movie movie) {
        MovieResponseDTO updatedMovie = movieService.updateMovie(id, movie);
        return ResponseEntity.ok(updatedMovie);
    }

    @Operation(summary = "Delete a movie from catalog", description = "Return response")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "No authorized")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get movie catalog", description = "Return movie catalog with pages and filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "No authorized")
    })
    @GetMapping
    public ResponseEntity<Page<MovieResponseDTO>> listMovies(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<String> category,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        // Construir Specification dinámica
        Specification<Movie> spec = Specification.where(null);

        if (search != null && !search.isEmpty()) {
            spec = spec.and(MovieSpecifications.containsNameOrSynopsis(search));
        }

        if (category != null && !category.isEmpty()) {
            spec = spec.and(MovieSpecifications.hasCategories(category));
        }

        if (releaseYear != null) {
            spec = spec.and(MovieSpecifications.releaseYearEquals(releaseYear));
        }

        // Manejar el ordenamiento. Se asume que para 'rating' se realiza un join y se calcula el promedio.
        Sort sort;
        if (sortBy.equalsIgnoreCase("rating")) {
            // Este ejemplo asume que en el repositorio se ha definido un join para el campo 'averageRating'
            // y que dicho campo se puede ordenar.
            sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by("ratings").ascending()
                    : Sort.by("ratings").descending();
        } else {
            sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
        }

        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<MovieResponseDTO> moviesPage = movieService.getMovies(spec, pageable);
        return ResponseEntity.ok(moviesPage);
    }
}