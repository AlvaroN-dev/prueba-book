/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.booknova.domain;

import java.time.Instant;

/**
 * Member entity representing a library member in the NovaBook system.
 * <p>
 * This class encapsulates all member-related information including
 * personal details, membership status, role, and access permissions.
 * </p>
 * <p>Example usage:</p>
 * <pre>{@code
 * Member member = new Member("Jane Doe");
 * member.setRole(MemberRole.PREMIUM);
 * member.setActive(true);
 * }</pre>
 * @version 1.0
 * @since 1.0
 * @see MemberRole
 * @see AccessLevel
 */
public class Member {
    private Integer id;
    private String name;
    private Boolean active;
    private Boolean deleted;
    private MemberRole role;
    private AccessLevel accessLevel;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Constructs a new Member with the specified name.
     * <p>
     * Creates a member instance with default settings: REGULAR role,
     * READ_WRITE access level, active status, and not deleted.
     * </p>
     * 
     * @param name the full name of the member
     * @throws IllegalArgumentException if name is null or empty
     */
    public Member(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        
        this.name = name;
        this.role = MemberRole.REGULAR;
        this.accessLevel = AccessLevel.READ_WRITE;
        this.active = true;
        this.deleted = false;
    }

    /**
     * Default constructor for Member.
     */
    public Member() {
        this.role = MemberRole.REGULAR;
        this.accessLevel = AccessLevel.READ_WRITE;
        this.active = true;
        this.deleted = false;
    }

    @Override
    public String toString() {
        return "Member{id=%s, name='%s', active=%s, deleted=%s, role=%s, accessLevel=%s, createdAt=%s, updatedAt=%s}"
            .formatted(id, name, active, deleted, role, accessLevel, createdAt, updatedAt);
    }

    /**
     * Checks if the member is active and can borrow books.
     * @return true if member is active and not deleted, false otherwise
     */
    public boolean canBorrow() {
        return active != null && active && (deleted == null || !deleted);
    }

    /**
     * Gets the unique identifier of the member.
     * @return the member ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the member.
     * @param id the member ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the name of the member.
     * @return the member's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the member.
     * @param name the member's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks if the member account is active.
     * @return true if active, false otherwise
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets the active status of the member account.
     * @param active true if active, false otherwise
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Checks if the member account is deleted.
     * @return true if deleted, false otherwise
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * Sets the deleted status of the member account.
     * @param deleted true if deleted, false otherwise
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Gets the role of the member.
     * @return the member's role
     */
    public MemberRole getRole() {
        return role;
    }

    /**
     * Sets the role of the member.
     * @param role the member's role
     */
    public void setRole(MemberRole role) {
        this.role = role;
    }

    /**
     * Gets the access level of the member.
     * @return the member's access level
     */
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * Sets the access level of the member.
     * @param accessLevel the member's access level
     */
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
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