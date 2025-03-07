package com.catalog.movies.controller;

import com.catalog.movies.dto.RatingResponseDTO;
import com.catalog.movies.model.User;
import com.catalog.movies.services.RatingService;
import com.catalog.movies.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final UserService userService;

    @Autowired
    public RatingController(RatingService ratingService, UserService userService) {
        this.ratingService = ratingService;
        this.userService = userService;
    }

    @Operation(summary = "Save a rate for a movie", description = "Post a new rate for a movie id and pass the rate value 1 to 5")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "No authorized")
    })
    @PostMapping("/{movieId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RatingResponseDTO> rateMovie(@PathVariable Long movieId, @RequestParam Integer rating) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // After extract the email, search user in bd
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        // Create rating throght the service
        RatingResponseDTO createdRating = ratingService.rateMovie(movieId, user.getId(), rating);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRating);
    }

    @Operation(summary = "Delete a movie from catalog", description = "Return empty when it is deleted")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "No authorized")
    })
    @DeleteMapping("/{movieId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeRating(@PathVariable Long movieId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        ratingService.removeRating(movieId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all rates for all movies by current user", description = "Return all rates created by the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "No authorized")
    })
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RatingResponseDTO>> getMyRatings() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        List<RatingResponseDTO> ratings = ratingService.getUserRatings(user.getId());
        return ResponseEntity.ok(ratings);
    }
}