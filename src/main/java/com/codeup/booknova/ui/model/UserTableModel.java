package com.codeup.booknova.ui.model;

import javafx.beans.property.*;

/**
 * JavaFX Property wrapper for User entity to use in TableViews
 */
public class UserTableModel {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phone;
    private final StringProperty role;
    private final StringProperty accessLevel;
    private final BooleanProperty active;
    
    public UserTableModel() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
        this.accessLevel = new SimpleStringProperty();
        this.active = new SimpleBooleanProperty();
    }
    
    public UserTableModel(Integer id, String name, String email, String phone, 
                         String role, String accessLevel, Boolean active) {
        this();
        setId(id);
        setName(name);
        setEmail(email);
        setPhone(phone);
        setRole(role);
        setAccessLevel(accessLevel);
        setActive(active != null ? active : false);
    }
    
    // Property getters
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty emailProperty() { return email; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty roleProperty() { return role; }
    public StringProperty accessLevelProperty() { return accessLevel; }
    public BooleanProperty activeProperty() { return active; }
    
    // Value getters
    public Integer getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getEmail() { return email.get(); }
    public String getPhone() { return phone.get(); }
    public String getRole() { return role.get(); }
    public String getAccessLevel() { return accessLevel.get(); }
    public Boolean getActive() { return active.get(); }
    
    // Value setters
    public void setId(Integer id) { this.id.set(id != null ? id : 0); }
    public void setName(String name) { this.name.set(name != null ? name : ""); }
    public void setEmail(String email) { this.email.set(email != null ? email : ""); }
    public void setPhone(String phone) { this.phone.set(phone != null ? phone : ""); }
    public void setRole(String role) { this.role.set(role != null ? role : ""); }
    public void setAccessLevel(String accessLevel) { this.accessLevel.set(accessLevel != null ? accessLevel : ""); }
    public void setActive(Boolean active) { this.active.set(active != null ? active : false); }
}