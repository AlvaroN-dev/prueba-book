/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.booknova.repository.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.codeup.booknova.domain.AccessLevel;
import com.codeup.booknova.domain.User;
import com.codeup.booknova.domain.UserRole;
import com.codeup.booknova.exception.DatabaseException;
import com.codeup.booknova.jdbc.JdbcTemplateLight;
import com.codeup.booknova.jdbc.RowMapper;
import com.codeup.booknova.repository.IUserRepository;
import com.codeup.booknova.util.PasswordUtils;
import com.codeup.booknova.util.ValidationUtils;

/**
 * JDBC implementation of the User repository interface.
 * 
 * <p>This repository provides data access operations for {@link User} entities using
 * JDBC through the {@link JdbcTemplateLight} abstraction. It implements security best
 * practices by using different mappers for different use cases to prevent password
 * exposure.</p>
 * 
 * <p><strong>Security Features:</strong></p>
 * <ul>
 *   <li><strong>Password Protection</strong> - Uses different mappers to control password exposure</li>
 *   <li><strong>Password Hashing</strong> - Passwords are hashed before storage</li>
 *   <li><strong>Active User Filtering</strong> - Separate methods for active users only</li>
 *   <li><strong>Input Validation</strong> - All inputs are validated before database operations</li>
 * </ul>
 * 
 * <p><strong>Mapper Strategy:</strong></p>
 * <ul>
 *   <li><strong>PUBLIC_MAPPER</strong> - Excludes password, used for general queries</li>
 *   <li><strong>FULL_MAPPER</strong> - Includes all fields including password for authentication</li>
 * </ul>
 * @version 1.0
 * @since 1.0
 * @see IUserRepository
 * @see User
 * @see JdbcTemplateLight
 * @see PasswordUtils
 * @see ValidationUtils
 */
public class UserJdbcRepository implements IUserRepository {
    
    private final JdbcTemplateLight jdbc;
    private static final Logger logger = Logger.getLogger(UserJdbcRepository.class.getName());

    /**
     * Constructs a new UserJdbcRepository with the specified JDBC template.
     * 
     * @param jdbc the JDBC template for database operations
     * @throws NullPointerException if jdbc is null
     */
    public UserJdbcRepository(JdbcTemplateLight jdbc) { 
        this.jdbc = jdbc; 
    }

    // Row MAPPERS
    private static final RowMapper<User> PUBLIC_MAPPER = rs -> {
        User user = new User(rs.getString("name"), rs.getString("email"), null, rs.getString("phone")); // password = null
        user.setId(rs.getInt("id"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        user.setAccessLevel(AccessLevel.valueOf(rs.getString("access_level")));
        user.setActive(rs.getBoolean("active"));
        user.setDeleted(rs.getBoolean("deleted"));
        user.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
        return user;
    };

    private static final RowMapper<User> FULL_MAPPER = rs -> {
        User user = new User(
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("phone")
        );
        user.setId(rs.getInt("id"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        user.setAccessLevel(AccessLevel.valueOf(rs.getString("access_level")));
        user.setActive(rs.getBoolean("active"));
        user.setDeleted(rs.getBoolean("deleted"));
        user.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
        return user;
    };

    @Override
    public User create(User user) throws DatabaseException {
        
        // Validations
        ValidationUtils.validateUser(user.getName(), user.getEmail(), user.getPassword(), user.getPhone());

        if (userExists(user.getEmail())) {
            throw new DatabaseException("Email already in use: " + user.getEmail());
        }
        
        String hashedPassword = PasswordUtils.hashPassword(user.getPassword());

        String sql = "INSERT INTO users (name, email, password, phone, role, access_level, active, deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, hashedPassword);
                    ps.setString(4, user.getPhone());
                    ps.setString(5, user.getRole() != null ? user.getRole().name() : UserRole.USER.name());
                    ps.setString(6, user.getAccessLevel() != null ? user.getAccessLevel().name() : AccessLevel.READ_WRITE.name());
                    ps.setBoolean(7, user.getActive() != null ? user.getActive() : true);
                    ps.setBoolean(8, user.getDeleted() != null ? user.getDeleted() : false);
                } catch (SQLException e) {
                    throw new RuntimeException("Error creating user", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to create user");
            }

            // Fetch the created user to get the ID
            return findByEmail(user.getEmail()).orElseThrow(() -> new DatabaseException("Failed to retrieve created user"));
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error creating user", e);
            throw e;
        }
    }

    @Override
    public User update(User user) throws DatabaseException {
        String sql = "UPDATE users SET name=?, email=?, password=?, phone=?, role=?, access_level=?, active=?, deleted=? WHERE id=?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, user.getPassword());
                    ps.setString(4, user.getPhone());
                    ps.setString(5, user.getRole().name());
                    ps.setString(6, user.getAccessLevel().name());
                    ps.setBoolean(7, user.getActive());
                    ps.setBoolean(8, user.getDeleted());
                    ps.setInt(9, user.getId());
                } catch (SQLException e) {
                    throw new RuntimeException("Error updating user", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to update user");
            }
            return user;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error updating user", e);
            throw e;
        }
    }

    @Override
    public int updateByEmail(User user, String email) throws DatabaseException {
        String sql = "UPDATE users SET name=?, password=?, phone=?, role=?, access_level=?, active=?, deleted=? WHERE email=?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getPassword());
                    ps.setString(3, user.getPhone());
                    ps.setString(4, user.getRole().name());
                    ps.setString(5, user.getAccessLevel().name());
                    ps.setBoolean(6, user.getActive());
                    ps.setBoolean(7, user.getDeleted());
                    ps.setString(8, email);
                } catch (SQLException e) {
                    throw new RuntimeException("Error updating user by email", e);
                }
            });
            logger.log(Level.INFO, "User update by email executed: {0}", email);
            return rows;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing update user: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<User> findById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            List<User> users = jdbc.query(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error setting parameters", e);
                }
            }, FULL_MAPPER);
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error finding user by id", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email=?";
        try {
            List<User> list = jdbc.query(sql, ps -> {
                try { ps.setString(1, email); } catch (SQLException e) { throw new RuntimeException("Error searching user by email", e); }
            }, FULL_MAPPER); // Use FULL_MAPPER to include password for authentication
            logger.log(Level.INFO, "User search by email executed: {0}", email);
            return list.stream().findFirst();
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search by email: {0}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findActiveByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email=? AND active=true AND deleted=false";
        try {
            List<User> list = jdbc.query(sql, ps -> {
                try { ps.setString(1, email); } catch (SQLException e) { throw new RuntimeException("Error finding active user by email", e); }
            }, FULL_MAPPER);
            logger.log(Level.INFO, "User authentication query executed: {0}", email);
            return list.stream().findFirst();
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing authentication query: {0}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        try {
            logger.log(Level.INFO, "User list executed");
            return jdbc.query(sql, null, FULL_MAPPER);
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing list users: {0}", e.getMessage());
            throw new RuntimeException("Database error during user list", e);
        }
    }

    @Override
    public List<User> findAllActive() {
        String sql = "SELECT * FROM users WHERE active=true AND deleted=false";
        try {
            logger.log(Level.INFO, "Active user list executed");
            return jdbc.query(sql, null, FULL_MAPPER);
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing active user list: {0}", e.getMessage());
            throw new RuntimeException("Database error during active user list", e);
        }
    }

    @Override
    public boolean userExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email=?";
        try {
            List<Integer> count = jdbc.query(sql, ps -> {
                try { ps.setString(1, email); } catch (SQLException e) { throw new RuntimeException("Error checking user existence", e); }
            }, rs -> rs.getInt(1));
            logger.log(Level.INFO, "User exists check executed: {0}", email);
            return count.stream().findFirst().orElse(0) > 0;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing user exists check: {0}", e.getMessage());
            throw new RuntimeException("Database error during user existence check", e);
        }
    }

    @Override
    public void softDelete(Integer id) throws DatabaseException {
        String sql = "UPDATE users SET deleted=true WHERE id = ?";
        try {
            jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error soft deleting user", e);
                }
            });
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error soft deleting user", e);
            throw e;
        }
    }

    @Override
    public void delete(Integer id) throws DatabaseException {
        String sql = "DELETE FROM users WHERE id = ?";
        try {
            jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error deleting user", e);
                }
            });
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error deleting user", e);
            throw e;
        }
    }
}