/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.booknova.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.codeup.booknova.domain.AccessLevel;
import com.codeup.booknova.domain.User;
import com.codeup.booknova.domain.UserRole;
import com.codeup.booknova.exception.DatabaseException;
import com.codeup.booknova.repository.IUserRepository;
import com.codeup.booknova.service.IUserService;
import com.codeup.booknova.util.PasswordUtils;
import com.codeup.booknova.util.ValidationUtils;

/**
 * Service class for managing user operations in the NovaBook system.
 * <p>
 * This service provides comprehensive user management functionality including
 * registration, authentication, profile updates, and administrative operations.
 * It enforces business rules and validates data before delegating to the repository layer.
 * </p>
 * <p>Key responsibilities:</p>
 * <ul>
 * <li>User registration with validation and password hashing</li>
 * <li>Secure authentication with credential verification</li>
 * <li>Profile management and updates</li>
 * <li>Administrative user creation with roles and permissions</li>
 * <li>User existence and duplicate detection</li>
 * </ul>
 * @version 1.0
 * @since 1.0
 * @see IUserRepository
 * @see User
 */
public class UserService implements IUserService {
    private final IUserRepository repo;
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    /**
     * Constructs a UserService with the specified repository.
     * 
     * @param repo the user repository implementation for data access
     * @throws IllegalArgumentException if repo is null
     */
    public UserService(IUserRepository repo) { 
        this.repo = repo; 
    }

    @Override
    public void register(String name, String email, String password, String phone) throws DatabaseException {
        // Validate input data
        ValidationUtils.validateUser(name, email, password, phone);
        
        // Check if user already exists
        if (userExists(email)) {
            throw new DatabaseException("User already exists with email: " + email);
        }
        
        // Hash the password before creating user
        String hashedPassword = PasswordUtils.hashPassword(password);
        User user = new User(name, email, hashedPassword, phone);
        repo.create(user);
    }

    @Override
    public void adminRegister(String name, String email, String password, String phone, String role, String accessLevel) 
            throws DatabaseException {
        // Validate input data
        ValidationUtils.validateUser(name, email, password, phone);
        
        // Check if user already exists
        if (userExists(email)) {
            throw new DatabaseException("User already exists with email: " + email);
        }

        User user = new User(name, email, password, phone);
        
        // Set role
        try {
            user.setRole(UserRole.valueOf(role.toUpperCase()));
            logger.info("Usuario registrado exitosamente: " + email);
        } catch (IllegalArgumentException e) {
            logger.severe("Error al registrar usuario: " + e.getMessage());
            throw new DatabaseException("Invalid role: " + role);
        }
        
        // Set access level
        try {
            user.setAccessLevel(AccessLevel.valueOf(accessLevel.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new DatabaseException("Invalid access level: " + accessLevel);
        }
        
        repo.create(user);
    }

    @Override
    public User updateUser(User user) throws DatabaseException {
        if (user == null || user.getId() == null) {
            throw new DatabaseException("User and user ID cannot be null");
        }
        return repo.update(user);
    }

    @Override
    public int updateUserByEmail(User user, String email) throws DatabaseException {
        if (user == null || email == null || email.trim().isEmpty()) {
            throw new DatabaseException("User and email cannot be null or empty");
        }
        ValidationUtils.validateEmail(email);
        return repo.updateByEmail(user, email);
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return repo.findById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        return repo.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    @Override
    public List<User> getAllActiveUsers() {
        return repo.findAllActive();
    }

    @Override
    public boolean userExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return repo.userExists(email);
    }

    @Override
    public Optional<User> authenticate(String email, String password) throws DatabaseException {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new DatabaseException("Email and password cannot be null or empty");
        }
        
        Optional<User> userOpt = repo.findActiveByEmail(email);
        if (userOpt.isEmpty()) {
            throw new DatabaseException("Authentication failed - user not found or inactive");
        }
        
        User user = userOpt.get();
        if (PasswordUtils.checkPassword(password, user.getPassword())) {
            return Optional.of(user);
        } else {
            throw new DatabaseException("Authentication failed - invalid credentials");
        }
    }

    @Override
    public void deactivateUser(Integer userId) throws DatabaseException {
        ValidationUtils.validateId(userId, "User ID");
        
        Optional<User> userOpt = repo.findById(userId);
        if (userOpt.isEmpty()) {
            throw new DatabaseException("User not found with ID: " + userId);
        }
        
        User user = userOpt.get();
        user.setActive(false);
        repo.update(user);
    }

    @Override
    public void activateUser(Integer userId) throws DatabaseException {
        ValidationUtils.validateId(userId, "User ID");
        
        Optional<User> userOpt = repo.findById(userId);
        if (userOpt.isEmpty()) {
            throw new DatabaseException("User not found with ID: " + userId);
        }
        
        User user = userOpt.get();
        user.setActive(true);
        repo.update(user);
    }

    @Override
    public void deleteUser(Integer userId) throws DatabaseException {
        ValidationUtils.validateId(userId, "User ID");
        repo.delete(userId);
    }
    
    /**
     * Authenticates a user with email and password.
     * 
     * @param email the email to search for
     * @param password the password to verify
     * @return the authenticated user if credentials are valid, null otherwise
     * @throws DatabaseException if there's an error accessing the database
     */
    public User authenticateUser(String email, String password) throws DatabaseException {
        if (email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return null;
        }
        
        // Find user by email
        Optional<User> userOpt = repo.findByEmail(email.trim());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Check if user is active and verify password using BCrypt
            if (user.getActive() && !user.getDeleted() && 
                PasswordUtils.checkPassword(password, user.getPassword())) {
                logger.info("Usuario autenticado: " + email);
                return user;
            }
        }
        
        return null;
    }
}