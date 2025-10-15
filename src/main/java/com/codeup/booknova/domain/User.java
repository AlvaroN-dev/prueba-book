/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.booknova.domain;

import java.time.Instant;

/**
 * User entity representing a system user in the NovaBook application.
 * <p>
 * This class encapsulates all user-related information including personal details,
 * authentication credentials, role-based permissions, and account status.
 * Passwords are stored in hashed format for security.
 * </p>
 * <p>Example usage:</p>
 * <pre>{@code
 * User user = new User("John Doe", "john@email.com", "hashedPassword", "1234567890");
 * user.setRole(UserRole.USER);
 * user.setAccessLevel(AccessLevel.READ_WRITE);
 * user.setActive(true);
 * }</pre>
 * @version 1.0
 * @since 1.0
 * @see UserRole
 * @see AccessLevel
 */
public class User {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private UserRole role;
    private AccessLevel accessLevel;
    private Boolean active;
    private Boolean deleted;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Constructs a new User with the specified basic information.
     * <p>
     * Creates a user instance with name, email, password, and phone.
     * The user is initially set with default role (USER) and access level (READ_WRITE),
     * and is marked as active and not deleted.
     * </p>
     * 
     * @param name the full name of the user
     * @param email the email address, must be unique
     * @param password the password (will be hashed before storage)
     * @param phone the phone number
     * @throws IllegalArgumentException if any parameter is null or empty
     */
    public User(String name, String email, String password, String phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        // Phone can be empty but not null
        if (phone == null) {
            phone = "";
        }
        
        // Additional validations
        if (name.length() > 100) {
            throw new IllegalArgumentException("Name cannot exceed 100 characters");
        }
        if (email.length() > 255) {
            throw new IllegalArgumentException("Email cannot exceed 255 characters");
        }
        if (password.length() > 255) {
            throw new IllegalArgumentException("Password cannot exceed 255 characters");
        }
        if (phone.length() > 30) {
            throw new IllegalArgumentException("Phone cannot exceed 30 characters");
        }
        
        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.password = password;
        this.phone = phone.trim();
        this.role = UserRole.USER;
        this.accessLevel = AccessLevel.READ_WRITE;
        this.active = true;
        this.deleted = false;
    }

    /**
     * Default constructor for User.
     */
    public User() {
        this.role = UserRole.USER;
        this.accessLevel = AccessLevel.READ_WRITE;
        this.active = true;
        this.deleted = false;
    }

    @Override
    public String toString() {
        return "User{id=%s, name='%s', email='%s', phone='%s', role=%s, accessLevel=%s, active=%s, deleted=%s, createdAt=%s, updatedAt=%s}"
            .formatted(id, name, email, phone, role, accessLevel, active, deleted, createdAt, updatedAt);
    }

    /**
     * Gets the unique identifier of the user.
     * @return the user ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     * @param id the user ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the full name of the user.
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the full name of the user.
     * @param name the user's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the user.
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     * @param email the user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password hash of the user.
     * @return the hashed password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password hash of the user.
     * @param password the hashed password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the phone number of the user.
     * @return the user's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the user.
     * @param phone the user's phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the role of the user.
     * @return the user's role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     * @param role the user's role
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * Gets the access level of the user.
     * @return the user's access level
     */
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * Sets the access level of the user.
     * @param accessLevel the user's access level
     */
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * Checks if the user account is active.
     * @return true if active, false otherwise
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets the active status of the user account.
     * @param active true if active, false otherwise
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Checks if the user account is deleted.
     * @return true if deleted, false otherwise
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * Sets the deleted status of the user account.
     * @param deleted true if deleted, false otherwise
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Gets the creation timestamp.
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last update timestamp.
     * @return the last update timestamp
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp.
     * @param updatedAt the last update timestamp
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}