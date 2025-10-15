/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.codeup.booknova.domain;

/**
 * Enumeration defining user roles in the NovaBook system.
 * <p>
 * This enum represents the different levels of access and permissions
 * available to users within the library management application.
 * </p>
 * <p>Example usage:</p>
 * <pre>{@code
 * User user = new User("John", "john@email.com", "password", "123456789");
 * user.setRole(UserRole.ADMIN);
 * 
 * if (user.getRole() == UserRole.ADMIN) {
 *     // Grant administrative privileges
 * }
 * }</pre>
 * @version 1.0
 * @since 1.0
 * @see User
 * @see AccessLevel
 */
public enum UserRole {
    /** Regular user with standard library privileges */
    USER,
    
    /** Administrative user with library management capabilities */
    ADMIN,
}