package com.catalog.movies.services;

import com.catalog.movies.dto.RegisterRequest;
import com.catalog.movies.exception.ResourceNotFoundException;
import com.catalog.movies.model.Role;
import com.catalog.movies.model.User;
import com.catalog.movies.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user. Before saving, the email is verified to be unregistered.
     * The password is encrypted and the default role (USER) is assigned.
     *
     * @param registerRequest DTO object containing the information required for registration.
     * @return The user saved in the database.
     */
    public User registerUser(RegisterRequest registerRequest) {
        // Verify if a user with the same email already exists
        Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("El correo ya se encuentra registrado.");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        // Encrypt password before to save it
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        // Assign a new role
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    /**
     * Search for a user by their email.
     *
     * @param email The user's email.
     * @return An Optional with the user, or empty if not found.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Gets a user by its id.
     *
     * @param userId The user id.
     * @return The user found.
     * @throws ResourceNotFoundException If the user is not found.
     */
    public User getUserById(Long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));
    }
}
