package com.codeup.booknova.ui.model;

/**
 * Table model for displaying membership requests in the admin dashboard
 */
public class MembershipRequestTableModel {
    private Integer id;
    private String userName;
    private String userEmail;
    private String status;
    private String requestDate;
    
    public MembershipRequestTableModel() {
    }
    
    public MembershipRequestTableModel(Integer id, String userName, String userEmail, 
                                      String status, String requestDate) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.status = status;
        this.requestDate = requestDate;
    }
    
    // Getters and setters
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
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
    
    public String getRequestDate() {
        return requestDate;
    }
    
    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}
