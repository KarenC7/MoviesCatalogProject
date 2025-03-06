package com.catalog.movies.controller;

import com.catalog.movies.model.Rating;
import com.catalog.movies.model.User;
import com.catalog.movies.services.RatingService;
import com.catalog.movies.services.UserService;
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

    /**
     * Endpoint to allow user rating the movie
     * URL: POST /api/ratings/{movieId}?rating=valor
     *
     * @param movieId movie id
     * @param rating  rating (ej. 1 to 5).
     * @return Created rating
     */
    @PostMapping("/{movieId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Rating> rateMovie(@PathVariable Long movieId, @RequestParam Integer rating) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // After extract the email, search user in bd
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        // Create rating throght the service
        Rating createdRating = ratingService.rateMovie(movieId, user.getId(), rating);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRating);
    }

    /**
     * Endpoint to allow user remove the rating
     * URL: DELETE /api/ratings/{movieId}
     *
     * @param movieId movie id to remove rating
     * @return HTTP 204 (No Content) it it was succesfuly deleted.
     */
    @DeleteMapping("/{movieId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeRating(@PathVariable Long movieId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        ratingService.removeRating(movieId, user.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to get all ratings from the authenticated user
     * URL: GET /api/ratings/my
     *
     * @return List of ratings.
     */
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Rating>> getMyRatings() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        List<Rating> ratings = ratingService.getUserRatings(user.getId());
        return ResponseEntity.ok(ratings);
    }
}