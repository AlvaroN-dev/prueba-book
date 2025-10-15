/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.booknova.service;

import java.util.List;
import java.util.Optional;

import com.codeup.booknova.domain.User;
import com.codeup.booknova.exception.DatabaseException;

/**
 * Service interface for User management operations.
 * <p>
 * This interface defines the contract for all user-related business operations
 * in the NovaBook system. It provides methods for user registration, authentication,
 * profile management, and administrative operations.
 * </p>
 * <p>Key responsibilities:</p>
 * <ul>
 * <li>User registration with validation and password hashing</li>
 * <li>Secure authentication with credential verification</li>
 * <li>Profile management and updates</li>
 * <li>Administrative user creation with roles and permissions</li>
 * <li>User existence and duplicate detection</li>
 * </ul>
 * 
 * <p><strong>Usage Example:</strong></p>
 * <pre>{@code
 * IUserService userService = new UserService(userRepository);
 * 
 * // Register a new user
 * userService.register("John Doe", "john@email.com", "SecurePass123!", "1234567890");
 * 
 * // Authenticate user
 * Optional<User> user = userService.authenticate("john@email.com", "SecurePass123!");
 * }</pre>
 * @version 1.0
 * @since 1.0
 * @see User
 * @see com.codeup.booknova.repository.IUserRepository
 */
public interface IUserService {
    
    /**
     * Registers a new user in the system.
     * <p>
     * This method validates the input data, ensures the email is unique,
     * and creates a new user account with default permissions.
     * The password is automatically hashed before storage.
     * </p>
     * 
     * @param name the full name of the user, must not be empty or contain only digits
     * @param email the email address, must be valid format and unique
     * @param password the plain text password, must meet complexity requirements
     * @param phone the phone number, must contain only digits
     * @throws DatabaseException if any input validation fails or user already exists
     */
    void register(String name, String email, String password, String phone) throws DatabaseException;
    
    /**
     * Registers a new administrative user in the system.
     * <p>
     * This method creates a user with administrative privileges and custom role/access level.
     * Only existing administrators should be able to call this method.
     * </p>
     * 
     * @param name the full name of the user
     * @param email the email address, must be unique
     * @param password the plain text password
     * @param phone the phone number
     * @param role the user role (USER or ADMIN)
     * @param accessLevel the access level (READ_ONLY, READ_WRITE, MANAGE)
     * @throws DatabaseException if validation fails or user already exists
     */
    void adminRegister(String name, String email, String password, String phone, String role, String accessLevel) throws DatabaseException;
    
    /**
     * Updates an existing user's profile information.
     * 
     * @param user the user with updated information
     * @return the updated user
     * @throws DatabaseException if update fails
     */
    User updateUser(User user) throws DatabaseException;
    
    /**
     * Updates a user identified by email.
     * 
     * @param user the user with updated information
     * @param email the email to identify the user
     * @return the number of affected rows
     * @throws DatabaseException if update fails
     */
    int updateUserByEmail(User user, String email) throws DatabaseException;
    
    /**
     * Finds a user by their ID.
     * 
     * @param id the user ID
     * @return an Optional containing the user if found
     */
    Optional<User> findUserById(Integer id);
    
    /**
     * Finds a user by their email address.
     * 
     * @param email the email address
     * @return an Optional containing the user if found
     */
    Optional<User> findUserByEmail(String email);
    
    /**
     * Retrieves all users in the system.
     * 
     * @return a list of all users, may be empty but never null
     */
    List<User> getAllUsers();
    
    /**
     * Retrieves all active users in the system.
     * 
     * @return a list of all active users, may be empty but never null
     */
    List<User> getAllActiveUsers();
    
    /**
     * Checks if a user exists with the given email address.
     * 
     * @param email the email address to check
     * @return {@code true} if a user exists with the email, {@code false} otherwise
     */
    boolean userExists(String email);
    
    /**
     * Authenticates a user with email and password credentials.
     * <p>
     * This method verifies the provided credentials against stored user data.
     * It only considers active, non-deleted users for authentication.
     * Password verification uses secure hashing for security.
     * </p>
     * 
     * @param email the user's email address
     * @param password the plain text password to verify
     * @return an {@link Optional} containing the authenticated user if successful
     * @throws DatabaseException if authentication fails or database error occurs
     */
    Optional<User> authenticate(String email, String password) throws DatabaseException;
    
    /**
     * Deactivates a user account (soft delete).
     * 
     * @param userId the ID of the user to deactivate
     * @throws DatabaseException if deactivation fails
     */
    void deactivateUser(Integer userId) throws DatabaseException;
    
    /**
     * Activates a user account.
     * 
     * @param userId the ID of the user to activate
     * @throws DatabaseException if activation fails
     */
    void activateUser(Integer userId) throws DatabaseException;
    
    /**
     * Permanently deletes a user from the system.
     * 
     * @param userId the ID of the user to delete
     * @throws DatabaseException if deletion fails
     */
    void deleteUser(Integer userId) throws DatabaseException;
}