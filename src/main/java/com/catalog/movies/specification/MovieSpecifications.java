package com.catalog.movies.specification;

import com.catalog.movies.model.Movie;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.List;

public class MovieSpecifications {

    // Search name or synopsis matches (case-insensitive)
    public static Specification<Movie> containsNameOrSynopsis(String search) {
        return (root, query, cb) -> {
            String likePattern = "%" + search.toLowerCase() + "%";
            Predicate namePredicate = cb.like(cb.lower(root.get("name")), likePattern);
            Predicate synopsisPredicate = cb.like(cb.lower(root.get("synopsis")), likePattern);
            return cb.or(namePredicate, synopsisPredicate);
        };
    }

    // Filter by category
    public static Specification<Movie> hasCategories(List<String> categories) {
        return (root, query, cb) -> {
            Join<Movie, String> joinCategories = root.join("categories");
            return joinCategories.in(categories);
        };
    }

    // Filter by year
    public static Specification<Movie> releaseYearEquals(Integer releaseYear) {
        return (root, query, cb) -> cb.equal(root.get("releaseYear"), releaseYear);
    }
}