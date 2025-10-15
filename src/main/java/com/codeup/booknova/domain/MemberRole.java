/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.codeup.booknova.domain;

/**
 * Enumeration defining member roles in the NovaBook library system.
 * <p>
 * This enum represents the different membership tiers available to
 * library patrons, affecting their borrowing privileges and benefits.
 * </p>
 * @version 1.0
 * @since 1.0
 * @see Member
 */
public enum MemberRole {
    /** Regular member with standard borrowing privileges */
    REGULAR,
    
    /** Premium member with extended borrowing capabilities and benefits */
    PREMIUM
}