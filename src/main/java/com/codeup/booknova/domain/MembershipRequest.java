package com.codeup.booknova.domain;

import java.time.Instant;

/**
 * MembershipRequest entity representing a pending membership application.
 * Users can request membership which must be approved by administrators.
 */
public class MembershipRequest {
    private Integer id;
    private Integer userId;
    private String userName;
    private String userEmail;
    private String status; // PENDING, APPROVED, REJECTED
    private String requestReason;
    private Integer approvedByUserId;
    private Instant requestedAt;
    private Instant processedAt;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Default constructor
     */
    public MembershipRequest() {
        this.status = "PENDING";
        this.requestedAt = Instant.now();
    }

    /**
     * Constructor with user information
     * 
     * @param userId the ID of the user requesting membership
     * @param userName the name of the user
     * @param userEmail the email of the user
     */
    public MembershipRequest(Integer userId, String userName, String userEmail) {
        this();
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    /**
     * Checks if the request is pending approval
     * 
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return "PENDING".equals(status);
    }

    /**
     * Checks if the request has been approved
     * 
     * @return true if status is APPROVED
     */
    public boolean isApproved() {
        return "APPROVED".equals(status);
    }

    /**
     * Checks if the request has been rejected
     * 
     * @return true if status is REJECTED
     */
    public boolean isRejected() {
        return "REJECTED".equals(status);
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public Integer getApprovedByUserId() {
        return approvedByUserId;
    }

    public void setApprovedByUserId(Integer approvedByUserId) {
        this.approvedByUserId = approvedByUserId;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "MembershipRequest{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", status='" + status + '\'' +
                ", requestedAt=" + requestedAt +
                '}';
    }
}
