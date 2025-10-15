package com.codeup.booknova.service;

import com.codeup.booknova.domain.MembershipRequest;
import com.codeup.booknova.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for MembershipRequest operations
 */
public interface IMembershipRequestService {
    
    /**
     * Creates a new membership request for a user
     * 
     * @param userId the ID of the user requesting membership
     * @param userName the name of the user
     * @param userEmail the email of the user
     * @param reason optional reason for requesting membership
     * @return the created request
     * @throws DatabaseException if user already has a pending request or creation fails
     */
    MembershipRequest createRequest(Integer userId, String userName, String userEmail, String reason) throws DatabaseException;
    
    /**
     * Retrieves all pending membership requests
     * 
     * @return list of pending requests
     */
    List<MembershipRequest> getAllPendingRequests();
    
    /**
     * Retrieves all membership requests
     * 
     * @return list of all requests
     */
    List<MembershipRequest> getAllRequests();
    
    /**
     * Finds a membership request by ID
     * 
     * @param id the request ID
     * @return an Optional containing the request if found
     */
    Optional<MembershipRequest> findRequestById(Integer id);
    
    /**
     * Checks if a user has a pending membership request
     * 
     * @param userId the user ID
     * @return true if user has a pending request
     */
    boolean hasPendingRequest(Integer userId);
    
    /**
     * Approves a membership request and creates a member
     * 
     * @param requestId the ID of the request to approve
     * @param approvedByUserId the ID of the admin who approved the request
     * @throws DatabaseException if approval fails or member creation fails
     */
    void approveRequest(Integer requestId, Integer approvedByUserId) throws DatabaseException;
    
    /**
     * Rejects a membership request
     * 
     * @param requestId the ID of the request to reject
     * @param rejectedByUserId the ID of the admin who rejected the request
     * @throws DatabaseException if rejection fails
     */
    void rejectRequest(Integer requestId, Integer rejectedByUserId) throws DatabaseException;
}
