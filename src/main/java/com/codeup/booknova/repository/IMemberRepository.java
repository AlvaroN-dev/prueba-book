/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.booknova.repository;

import java.util.List;
import java.util.Optional;

import com.codeup.booknova.domain.Member;
import com.codeup.booknova.exception.DatabaseException;

/**
 * Repository interface for Member entity data access operations.
 * <p>
 * This interface defines the contract for all member-related database operations
 * in the NovaBook system. It provides methods for member CRUD operations,
 * membership management, and member search functionality.
 * </p>
 * <p>Key operations:</p>
 * <ul>
 * <li>Member registration and management</li>
 * <li>Member searches by various criteria</li>
 * <li>Active member filtering</li>
 * <li>Membership role management</li>
 * </ul>
 * @version 1.0
 * @since 1.0
 * @see Member
 */
public interface IMemberRepository {
    
    /**
     * Creates a new member in the system.
     * 
     * @param member the member entity to create
     * @return the created member with generated ID
     * @throws DatabaseException if member data validation fails or database error occurs
     */
    Member create(Member member) throws DatabaseException;
    
    /**
     * Updates an existing member.
     * 
     * @param member the member object with updated information
     * @return the updated member
     * @throws DatabaseException if update fails
     */
    Member update(Member member) throws DatabaseException;
    
    /**
     * Finds a member by ID.
     * 
     * @param id the member ID to search for
     * @return an {@link Optional} containing the member if found, empty otherwise
     */
    Optional<Member> findById(Integer id);
    
    /**
     * Finds members by name (case-insensitive partial match).
     * 
     * @param name the name to search for
     * @return a list of members matching the name, may be empty but never null
     */
    List<Member> findByName(String name);
    
    /**
     * Finds an active member by ID.
     * 
     * @param id the member ID to search for
     * @return an {@link Optional} containing the active member if found, empty otherwise
     */
    Optional<Member> findActiveById(Integer id);
    
    /**
     * Retrieves all members in the system.
     * 
     * @return a list of all members, may be empty but never null
     */
    List<Member> findAll();
    
    /**
     * Retrieves all active members in the system.
     * 
     * @return a list of all active members, may be empty but never null
     */
    List<Member> findAllActive();
    
    /**
     * Checks if a member can borrow books (active and not deleted).
     * 
     * @param memberId the ID of the member to check
     * @return {@code true} if the member can borrow, {@code false} otherwise
     */
    boolean canMemberBorrow(Integer memberId);
    
    /**
     * Soft deletes a member by marking them as deleted.
     * 
     * @param id the ID of the member to delete
     * @throws DatabaseException if deletion fails
     */
    void softDelete(Integer id) throws DatabaseException;
    
    /**
     * Hard deletes a member from the database.
     * 
     * @param id the ID of the member to delete
     * @throws DatabaseException if deletion fails
     */
    void delete(Integer id) throws DatabaseException;
    
    /**
     * Activates a member account.
     * 
     * @param id the ID of the member to activate
     * @throws DatabaseException if activation fails
     */
    void activateMember(Integer id) throws DatabaseException;
    
    /**
     * Deactivates a member account.
     * 
     * @param id the ID of the member to deactivate
     * @throws DatabaseException if deactivation fails
     */
    void deactivateMember(Integer id) throws DatabaseException;
}