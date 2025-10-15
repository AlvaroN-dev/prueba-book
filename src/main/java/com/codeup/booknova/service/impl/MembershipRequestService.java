package com.codeup.booknova.service.impl;

import com.codeup.booknova.domain.Member;
import com.codeup.booknova.domain.MembershipRequest;
import com.codeup.booknova.exception.DatabaseException;
import com.codeup.booknova.repository.IMemberRepository;
import com.codeup.booknova.repository.IMembershipRequestRepository;
import com.codeup.booknova.service.IMembershipRequestService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of IMembershipRequestService
 */
public class MembershipRequestService implements IMembershipRequestService {
    
    private final IMembershipRequestRepository requestRepository;
    private final IMemberRepository memberRepository;
    
    public MembershipRequestService(IMembershipRequestRepository requestRepository, 
                                   IMemberRepository memberRepository) {
        this.requestRepository = requestRepository;
        this.memberRepository = memberRepository;
    }
    
    @Override
    public MembershipRequest createRequest(Integer userId, String userName, String userEmail, String reason) 
            throws DatabaseException {
        
        // Validation: Check if user already has a pending request
        if (requestRepository.hasPendingRequest(userId)) {
            throw new DatabaseException("User already has a pending membership request");
        }
        
        // Create new request
        MembershipRequest request = new MembershipRequest(userId, userName, userEmail);
        request.setRequestReason(reason);
        request.setStatus("PENDING");
        
        return requestRepository.create(request);
    }
    
    @Override
    public List<MembershipRequest> getAllPendingRequests() {
        return requestRepository.findAllPending();
    }
    
    @Override
    public List<MembershipRequest> getAllRequests() {
        return requestRepository.findAll();
    }
    
    @Override
    public Optional<MembershipRequest> findRequestById(Integer id) {
        return requestRepository.findById(id);
    }
    
    @Override
    public boolean hasPendingRequest(Integer userId) {
        return requestRepository.hasPendingRequest(userId);
    }
    
    @Override
    public void approveRequest(Integer requestId, Integer approvedByUserId) throws DatabaseException {
        // Find the request
        MembershipRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new DatabaseException("Membership request not found"));
        
        // Validate request is pending
        if (!"PENDING".equals(request.getStatus())) {
            throw new DatabaseException("Request has already been processed");
        }
        
        // Create the member with user ID link
        Member member = new Member(request.getUserName());
        member.setUserId(request.getUserId()); // Link member to user
        member.setActive(true);
        member = memberRepository.create(member);
        
        // Update request status
        request.setStatus("APPROVED");
        request.setApprovedByUserId(approvedByUserId);
        request.setProcessedAt(Instant.now());
        
        requestRepository.update(request);
    }
    
    @Override
    public void rejectRequest(Integer requestId, Integer rejectedByUserId) throws DatabaseException {
        // Find the request
        MembershipRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new DatabaseException("Membership request not found"));
        
        // Validate request is pending
        if (!"PENDING".equals(request.getStatus())) {
            throw new DatabaseException("Request has already been processed");
        }
        
        // Update request status
        request.setStatus("REJECTED");
        request.setApprovedByUserId(rejectedByUserId);
        request.setProcessedAt(Instant.now());
        
        requestRepository.update(request);
    }
}
