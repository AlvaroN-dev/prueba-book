package com.codeup.booknova.repository;

import com.codeup.booknova.domain.MembershipRequest;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MembershipRequest data access operations.
 */
public interface IMembershipRequestRepository {
    
    /**
     * Creates a new membership request
     * 
     * @param request the membership request to create
     * @return the created request with generated ID
     */
    MembershipRequest create(MembershipRequest request);
    
    /**
     * Finds a membership request by ID
     * 
     * @param id the request ID
     * @return an Optional containing the request if found
     */
    Optional<MembershipRequest> findById(Integer id);
    
    /**
     * Finds a pending membership request for a specific user
     * 
     * @param userId the user ID
     * @return an Optional containing the pending request if found
     */
    Optional<MembershipRequest> findPendingByUserId(Integer userId);
    
    /**
     * Retrieves all membership requests
     * 
     * @return list of all requests
     */
    List<MembershipRequest> findAll();
    
    /**
     * Retrieves all pending membership requests
     * 
     * @return list of pending requests
     */
    List<MembershipRequest> findAllPending();
    
    /**
     * Updates an existing membership request
     * 
     * @param request the request to update
     * @return the updated request
     */
    MembershipRequest update(MembershipRequest request);
    
    /**
     * Deletes a membership request
     * 
     * @param id the ID of the request to delete
     */
    void delete(Integer id);
    
    /**
     * Checks if a user has a pending membership request
     * 
     * @param userId the user ID
     * @return true if user has a pending request
     */
    boolean hasPendingRequest(Integer userId);
}
