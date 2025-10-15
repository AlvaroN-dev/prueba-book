/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.booknova.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for secure password operations using BCrypt hashing.
 * <p>
 * This class provides static methods for password hashing and verification
 * using the BCrypt algorithm for secure password storage.
 * </p>
 * 
 * @author tonys-dev
 * @version 1.0
 * @since 1.0
 */
public class PasswordUtils {
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private PasswordUtils() {}
    
    /**
     * Hashes a plain text password using BCrypt.
     * 
     * @param plainPassword the plain text password to hash
     * @return the BCrypt hashed password
     * @throws IllegalArgumentException if plainPassword is null or empty
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        // Use BCrypt for secure password hashing
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Verifies a plain text password against a BCrypt hash.
     * 
     * @param plainPassword the plain text password to verify
     * @param hashedPassword the BCrypt hash to verify against
     * @return {@code true} if the password matches the hash, {@code false} otherwise
     * @throws IllegalArgumentException if either parameter is null or empty
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Hash cannot be null or empty");
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            // If the hash is invalid or there's an error, return false
            return false;
        }
    }
}