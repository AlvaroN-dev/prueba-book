/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.booknova.service.impl;

import java.util.List;
import java.util.Optional;

import com.codeup.booknova.domain.AccessLevel;
import com.codeup.booknova.domain.Member;
import com.codeup.booknova.domain.MemberRole;
import com.codeup.booknova.exception.DatabaseException;
import com.codeup.booknova.repository.IMemberRepository;
import com.codeup.booknova.service.IMemberService;
import com.codeup.booknova.util.ValidationUtils;

/**
 * Service class for managing member operations in the NovaBook system.
 * <p>
 * This service provides comprehensive member management functionality including
 * registration, profile updates, and membership administration.
 * It enforces business rules and validates data before delegating to the repository layer.
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
 * @see IMemberRepository
 * @see Member
 */
public class MemberService implements IMemberService {
    private final IMemberRepository repo;
    
    /**
     * Constructs a MemberService with the specified repository.
     * 
     * @param repo the member repository implementation for data access
     * @throws IllegalArgumentException if repo is null
     */
    public MemberService(IMemberRepository repo) { 
        this.repo = repo; 
    }

    @Override
    public Member registerMember(String name) throws DatabaseException {
        // Validate input data
        ValidationUtils.validateName(name);
        
        Member member = new Member(name);
        return repo.create(member);
    }

    @Override
    public Member registerMemberWithRole(String name, String role, String accessLevel) throws DatabaseException {
        // Validate input data
        ValidationUtils.validateName(name);
        
        Member member = new Member(name);
        
        // Set role
        try {
            member.setRole(MemberRole.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new DatabaseException("Invalid role: " + role);
        }
        
        // Set access level
        try {
            member.setAccessLevel(AccessLevel.valueOf(accessLevel.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new DatabaseException("Invalid access level: " + accessLevel);
        }
        
        return repo.create(member);
    }

    @Override
    public Member updateMember(Member member) throws DatabaseException {
        if (member == null || member.getId() == null) {
            throw new DatabaseException("Member and member ID cannot be null");
        }
        
        // Validate member data
        ValidationUtils.validateName(member.getName());
        
        return repo.update(member);
    }

    @Override
    public Optional<Member> findMemberById(Integer id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return repo.findById(id);
    }

    @Override
    public List<Member> searchMembersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }
        return repo.findByName(name);
    }

    @Override
    public List<Member> getAllMembers() {
        return repo.findAll();
    }

    @Override
    public List<Member> getAllActiveMembers() {
        return repo.findAllActive();
    }

    @Override
    public boolean canMemberBorrow(Integer memberId) {
        if (memberId == null || memberId <= 0) {
            return false;
        }
        return repo.canMemberBorrow(memberId);
    }

    @Override
    public void activateMember(Integer memberId) throws DatabaseException {
        ValidationUtils.validateId(memberId, "Member ID");
        
        // Verify member exists
        if (!repo.findById(memberId).isPresent()) {
            throw new DatabaseException("Member not found with ID: " + memberId);
        }
        
        repo.activateMember(memberId);
    }

    @Override
    public void deactivateMember(Integer memberId) throws DatabaseException {
        ValidationUtils.validateId(memberId, "Member ID");
        
        // Verify member exists
        if (!repo.findById(memberId).isPresent()) {
            throw new DatabaseException("Member not found with ID: " + memberId);
        }
        
        // In a more complete implementation, we would check for active loans here
        // For now, we'll allow deactivation
        repo.deactivateMember(memberId);
    }

    @Override
    public void upgradeToPremium(Integer memberId) throws DatabaseException {
        ValidationUtils.validateId(memberId, "Member ID");
        
        Optional<Member> memberOpt = repo.findById(memberId);
        if (memberOpt.isEmpty()) {
            throw new DatabaseException("Member not found with ID: " + memberId);
        }
        
        Member member = memberOpt.get();
        member.setRole(MemberRole.PREMIUM);
        repo.update(member);
    }

    @Override
    public void downgradeToRegular(Integer memberId) throws DatabaseException {
        ValidationUtils.validateId(memberId, "Member ID");
        
        Optional<Member> memberOpt = repo.findById(memberId);
        if (memberOpt.isEmpty()) {
            throw new DatabaseException("Member not found with ID: " + memberId);
        }
        
        Member member = memberOpt.get();
        member.setRole(MemberRole.REGULAR);
        repo.update(member);
    }

    @Override
    public void removeMember(Integer memberId) throws DatabaseException {
        ValidationUtils.validateId(memberId, "Member ID");
        
        // Check if member exists
        Optional<Member> memberOpt = repo.findById(memberId);
        if (memberOpt.isEmpty()) {
            throw new DatabaseException("Member not found with ID: " + memberId);
        }
        
        // In a more complete implementation, we would check for loan history here
        // For now, we'll allow deletion
        repo.delete(memberId);
    }
}