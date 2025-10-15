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
import com.codeup.booknova.domain.Member;
import com.codeup.booknova.domain.MemberRole;
import com.codeup.booknova.exception.DatabaseException;
import com.codeup.booknova.jdbc.JdbcTemplateLight;
import com.codeup.booknova.jdbc.RowMapper;
import com.codeup.booknova.repository.IMemberRepository;
import com.codeup.booknova.util.ValidationUtils;

/**
 * JDBC implementation of the Member repository interface.
 * 
 * <p>This repository provides data access operations for {@link Member} entities using
 * JDBC through the {@link JdbcTemplateLight} abstraction. It handles member management
 * including registration, status tracking, and search capabilities.</p>
 * 
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li><strong>Member Status Tracking</strong> - Manages active/inactive and deleted states</li>
 *   <li><strong>Role Management</strong> - Handles REGULAR and PREMIUM member types</li>
 *   <li><strong>Search Capabilities</strong> - Supports search by name and ID</li>
 *   <li><strong>Borrowing Eligibility</strong> - Tracks member borrowing permissions</li>
 * </ul>
 * @version 1.0
 * @since 1.0
 * @see IMemberRepository
 * @see Member
 * @see JdbcTemplateLight
 * @see ValidationUtils
 */
public class MemberJdbcRepository implements IMemberRepository {
    
    private final JdbcTemplateLight jdbc;
    private static final Logger logger = Logger.getLogger(MemberJdbcRepository.class.getName());

    /**
     * Constructs a new MemberJdbcRepository with the specified JDBC template.
     * 
     * @param jdbc the JDBC template for database operations
     * @throws NullPointerException if jdbc is null
     */
    public MemberJdbcRepository(JdbcTemplateLight jdbc) { 
        this.jdbc = jdbc; 
    }

    // Row MAPPER
    private static final RowMapper<Member> MEMBER_MAPPER = rs -> {
        Member member = new Member(rs.getString("name"));
        member.setId(rs.getInt("id"));
        member.setActive(rs.getBoolean("active"));
        member.setDeleted(rs.getBoolean("deleted"));
        member.setRole(MemberRole.valueOf(rs.getString("role")));
        member.setAccessLevel(AccessLevel.valueOf(rs.getString("access_level")));
        member.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        member.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
        return member;
    };

    @Override
    public Member create(Member member) throws DatabaseException {
        
        // Validations
        ValidationUtils.validateName(member.getName());

        String sql = "INSERT INTO member (name, active, deleted, role, access_level) VALUES (?, ?, ?, ?, ?)";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setString(1, member.getName());
                    ps.setBoolean(2, member.getActive() != null ? member.getActive() : true);
                    ps.setBoolean(3, member.getDeleted() != null ? member.getDeleted() : false);
                    ps.setString(4, member.getRole() != null ? member.getRole().name() : MemberRole.REGULAR.name());
                    ps.setString(5, member.getAccessLevel() != null ? member.getAccessLevel().name() : AccessLevel.READ_WRITE.name());
                } catch (SQLException e) {
                    throw new RuntimeException("Error creating member", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to create member");
            }

            // Fetch the created member by searching for the last inserted ID
            // Since we don't have name uniqueness, we'll search by name and take the most recent
            List<Member> members = findByName(member.getName());
            if (members.isEmpty()) {
                throw new DatabaseException("Failed to retrieve created member");
            }
            // Return the one with the highest ID (most recent)
            return members.stream().max((m1, m2) -> m1.getId().compareTo(m2.getId())).get();
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error creating member", e);
            throw e;
        }
    }

    @Override
    public Member update(Member member) throws DatabaseException {
        String sql = "UPDATE member SET name=?, active=?, deleted=?, role=?, access_level=? WHERE id=?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setString(1, member.getName());
                    ps.setBoolean(2, member.getActive());
                    ps.setBoolean(3, member.getDeleted());
                    ps.setString(4, member.getRole().name());
                    ps.setString(5, member.getAccessLevel().name());
                    ps.setInt(6, member.getId());
                } catch (SQLException e) {
                    throw new RuntimeException("Error updating member", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to update member");
            }
            return member;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error updating member", e);
            throw e;
        }
    }

    @Override
    public Optional<Member> findById(Integer id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            List<Member> members = jdbc.query(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error setting parameters", e);
                }
            }, MEMBER_MAPPER);
            return members.isEmpty() ? Optional.empty() : Optional.of(members.get(0));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error finding member by id", e);
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findByName(String name) {
        String sql = "SELECT * FROM member WHERE LOWER(name) LIKE LOWER(?)";
        try {
            List<Member> list = jdbc.query(sql, ps -> {
                try { ps.setString(1, "%" + name + "%"); } catch (SQLException e) { throw new RuntimeException("Error searching members by name", e); }
            }, MEMBER_MAPPER);
            logger.log(Level.INFO, "Member search by name executed: {0}", name);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search by name: {0}", e.getMessage());
            throw new RuntimeException("Database error during member search by name", e);
        }
    }

    @Override
    public Optional<Member> findActiveById(Integer id) {
        String sql = "SELECT * FROM member WHERE id = ? AND active=true AND deleted=false";
        try {
            List<Member> members = jdbc.query(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error setting parameters", e);
                }
            }, MEMBER_MAPPER);
            return members.isEmpty() ? Optional.empty() : Optional.of(members.get(0));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error finding active member by id", e);
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM member";
        try {
            logger.log(Level.INFO, "Member list executed");
            return jdbc.query(sql, null, MEMBER_MAPPER);
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing list members: {0}", e.getMessage());
            throw new RuntimeException("Database error during member list", e);
        }
    }

    @Override
    public List<Member> findAllActive() {
        String sql = "SELECT * FROM member WHERE active=true AND deleted=false";
        try {
            logger.log(Level.INFO, "Active member list executed");
            return jdbc.query(sql, null, MEMBER_MAPPER);
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing active member list: {0}", e.getMessage());
            throw new RuntimeException("Database error during active member list", e);
        }
    }

    @Override
    public boolean canMemberBorrow(Integer memberId) {
        String sql = "SELECT active, deleted FROM member WHERE id = ?";
        try {
            List<Boolean> results = jdbc.query(sql, ps -> {
                try {
                    ps.setInt(1, memberId);
                } catch (SQLException e) {
                    throw new RuntimeException("Error checking member borrowing eligibility", e);
                }
            }, rs -> rs.getBoolean("active") && !rs.getBoolean("deleted"));
            return results.stream().findFirst().orElse(false);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error checking member borrowing eligibility", e);
            return false;
        }
    }

    @Override
    public void softDelete(Integer id) throws DatabaseException {
        String sql = "UPDATE member SET deleted=true WHERE id = ?";
        try {
            jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error soft deleting member", e);
                }
            });
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error soft deleting member", e);
            throw e;
        }
    }

    @Override
    public void delete(Integer id) throws DatabaseException {
        String sql = "DELETE FROM member WHERE id = ?";
        try {
            jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error deleting member", e);
                }
            });
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error deleting member", e);
            throw e;
        }
    }

    @Override
    public void activateMember(Integer id) throws DatabaseException {
        String sql = "UPDATE member SET active=true WHERE id = ?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error activating member", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to activate member - member may not exist");
            }
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error activating member", e);
            throw e;
        }
    }

    @Override
    public void deactivateMember(Integer id) throws DatabaseException {
        String sql = "UPDATE member SET active=false WHERE id = ?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error deactivating member", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to deactivate member - member may not exist");
            }
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error deactivating member", e);
            throw e;
        }
    }
}