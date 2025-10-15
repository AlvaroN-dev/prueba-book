/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.booknova.repository;

import java.util.List;
import java.util.Optional;

import com.codeup.booknova.domain.User;
import com.codeup.booknova.exception.DatabaseException;

/**
 * Repository interface for User entity data access operations.
 * <p>
 * This interface defines the contract for all user-related database operations
 * in the NovaBook system. It provides methods for user CRUD operations,
 * authentication support, and user management functionality.
 * </p>
 * <p>Key operations:</p>
 * <ul>
 * <li>User creation with validation</li>
 * <li>User updates by email identifier</li>
 * <li>User searches with active/inactive filtering</li>
 * <li>User existence checks for duplicate prevention</li>
 * <li>User listing for administrative purposes</li>
 * </ul>
 * @version 1.0
 * @since 1.0
 * @see User
 */
public interface IUserRepository {
    
    /**
     * Creates a new user in the system.
     * 
     * @param user the user entity to create
     * @return the created user with generated ID
     * @throws DatabaseException if user data validation fails or database error occurs
     */
    User create(User user) throws DatabaseException;
    
    /**
     * Updates an existing user identified by ID.
     * 
     * @param user the user object with updated information
     * @return the updated user
     * @throws DatabaseException if update fails
     */
    User update(User user) throws DatabaseException;
    
    /**
     * Updates an existing user identified by email.
     * 
     * @param user the user object with updated information
     * @param email the email address to identify the user to update
     * @return the number of affected rows (typically 1)
     * @throws DatabaseException if update fails
     */
    int updateByEmail(User user, String email) throws DatabaseException;
    
    /**
     * Finds a user by ID.
     * 
     * @param id the user ID to search for
     * @return an {@link Optional} containing the user if found, empty otherwise
     */
    Optional<User> findById(Integer id);
    
    /**
     * Finds a user by email address (including inactive users).
     * 
     * @param email the email address to search for
     * @return an {@link Optional} containing the user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Finds an active user by email address for authentication purposes.
     * 
     * @param email the email address to search for
     * @return an {@link Optional} containing the active user if found, empty otherwise
     */
    Optional<User> findActiveByEmail(String email);
    
    /**
     * Retrieves all users in the system.
     * 
     * @return a list of all users, may be empty but never null
     */
    List<User> findAll();
    
    /**
     * Retrieves all active users in the system.
     * 
     * @return a list of all active users, may be empty but never null
     */
    List<User> findAllActive();
    
    /**
     * Checks if a user exists with the given email address.
     * 
     * @param email the email address to check
     * @return {@code true} if a user exists with the email, {@code false} otherwise
     */
    boolean userExists(String email);
    
    /**
     * Soft deletes a user by marking them as deleted.
     * 
     * @param id the ID of the user to delete
     * @throws DatabaseException if deletion fails
     */
    void softDelete(Integer id) throws DatabaseException;
    
    /**
     * Hard deletes a user from the database.
     * 
     * @param id the ID of the user to delete
     * @throws DatabaseException if deletion fails
     */
    void delete(Integer id) throws DatabaseException;
}