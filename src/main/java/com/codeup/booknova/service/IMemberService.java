/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.booknova.service;

import java.util.List;
import java.util.Optional;

import com.codeup.booknova.domain.Member;
import com.codeup.booknova.exception.DatabaseException;

/**
 * Service interface for Member management operations.
 * <p>
 * This interface defines the contract for all member-related business operations
 * in the NovaBook system. It provides methods for member registration, profile
 * management, and membership administration.
 * </p>
 * <p>Key responsibilities:</p>
 * <ul>
 * <li>Member registration and profile management</li>
 * <li>Membership status and role management</li>
 * <li>Member search and discovery</li>
 * <li>Borrowing eligibility validation</li>
 * </ul>
 * @version 1.0
 * @since 1.0
 * @see Member
 * @see com.codeup.booknova.repository.IMemberRepository
 */
public interface IMemberService {
    
    /**
     * Registers a new member in the library system.
     * <p>
     * This method validates the member data and creates a new member
     * with default settings (REGULAR role, active status).
     * </p>
     * 
     * @param name the full name of the member
     * @return the created member with generated ID
     * @throws DatabaseException if validation fails or database error occurs
     */
    Member registerMember(String name) throws DatabaseException;
    
    /**
     * Registers a new member with specific role and access level.
     * 
     * @param name the full name of the member
     * @param role the member role (REGULAR or PREMIUM)
     * @param accessLevel the access level (READ_ONLY, READ_WRITE, MANAGE)
     * @return the created member with generated ID
     * @throws DatabaseException if validation fails or database error occurs
     */
    Member registerMemberWithRole(String name, String role, String accessLevel) throws DatabaseException;
    
    /**
     * Updates an existing member's information.
     * 
     * @param member the member with updated information
     * @return the updated member
     * @throws DatabaseException if update fails
     */
    Member updateMember(Member member) throws DatabaseException;
    
    /**
     * Finds a member by their ID.
     * 
     * @param id the member ID
     * @return an Optional containing the member if found
     */
    Optional<Member> findMemberById(Integer id);
    
    /**
     * Searches for members by name (partial match, case-insensitive).
     * 
     * @param name the name to search for
     * @return a list of members matching the name search
     */
    List<Member> searchMembersByName(String name);
    
    /**
     * Retrieves all members in the system.
     * 
     * @return a list of all members, may be empty but never null
     */
    List<Member> getAllMembers();
    
    /**
     * Retrieves all active members in the system.
     * 
     * @return a list of all active members, may be empty but never null
     */
    List<Member> getAllActiveMembers();
    
    /**
     * Checks if a member is eligible to borrow books.
     * <p>
     * A member is eligible if they are active and not deleted.
     * Additional business rules may apply based on loan history.
     * </p>
     * 
     * @param memberId the ID of the member to check
     * @return {@code true} if the member can borrow, {@code false} otherwise
     */
    boolean canMemberBorrow(Integer memberId);
    
    /**
     * Activates a member account.
     * 
     * @param memberId the ID of the member to activate
     * @throws DatabaseException if activation fails
     */
    void activateMember(Integer memberId) throws DatabaseException;
    
    /**
     * Deactivates a member account.
     * <p>
     * This should check if the member has any active loans
     * before allowing deactivation.
     * </p>
     * 
     * @param memberId the ID of the member to deactivate
     * @throws DatabaseException if deactivation fails or member has active loans
     */
    void deactivateMember(Integer memberId) throws DatabaseException;
    
    /**
     * Upgrades a member to premium status.
     * 
     * @param memberId the ID of the member to upgrade
     * @throws DatabaseException if upgrade fails
     */
    void upgradeToPremium(Integer memberId) throws DatabaseException;
    
    /**
     * Downgrades a member to regular status.
     * 
     * @param memberId the ID of the member to downgrade
     * @throws DatabaseException if downgrade fails
     */
    void downgradeToRegular(Integer memberId) throws DatabaseException;
    
    /**
     * Permanently removes a member from the system.
     * <p>
     * This should check if the member has any loan history
     * before allowing deletion.
     * </p>
     * 
     * @param memberId the ID of the member to remove
     * @throws DatabaseException if deletion fails or member has loan history
     */
    void removeMember(Integer memberId) throws DatabaseException;
}