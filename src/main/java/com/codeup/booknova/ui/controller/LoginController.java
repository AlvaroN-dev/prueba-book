package com.codeup.booknova.ui.controller;

import com.codeup.booknova.domain.User;
import com.codeup.booknova.ui.NovaBookApplication;
import com.codeup.booknova.ui.service.ServiceManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

/**
 * Controller for the Login view
 */
public class LoginController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label messageLabel;
    
    private ServiceManager serviceManager;
    
    @FXML
    private void initialize() {
        serviceManager = ServiceManager.getInstance();
        
        // Enter key login
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
    }
    
    @FXML
    private void handleLogin() {
        String email = usernameField.getText();
        String password = passwordField.getText();
        
        // Clean and validate
        email = (email != null) ? email.trim() : "";
        password = (password != null) ? password : "";
        
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            showMessage("Please enter your email address and password.", "error");
            return;
        }
        
        try {
            User user = serviceManager.getUserService().authenticateUser(email, password);
            if (user != null) {
                showMessage("Login successful", "success");
                
                // Determine user type based on role
                String userType = determineUserType(user);
                // Pass the complete User object instead of just the name
                NovaBookApplication.showDashboard(userType, user);
            } else {
                showMessage("Incorrect email or password", "error");
            }
        } catch (Exception e) {
            showMessage("Error Of connection: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            RegisterDialog dialog = new RegisterDialog();
            dialog.showAndWait().ifPresent(user -> {
                try {
                    // Validate user data before sending to service
                    if (user.getName() == null || user.getName().trim().isEmpty()) {
                        showMessage("The name cannot be empty", "error");
                        return;
                    }
                    if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                        showMessage("The email cannot be empty.", "error");
                        return;
                    }
                    if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                        showMessage("The password cannot be empty", "error");
                        return;
                    }
                    if (user.getPhone() == null) {
                        user.setPhone(""); // Set empty if null
                    }
                    
                    serviceManager.getUserService().adminRegister(
                        user.getName(), user.getEmail(), user.getPassword(), 
                        user.getPhone(), user.getRole().toString(), user.getAccessLevel().toString()
                    );
                    showMessage("User successfully registered", "success");
                } catch (Exception e) {
                    showMessage("Error al registrar: " + e.getMessage(), "error");
                    e.printStackTrace(); // For debugging
                }
            });
        } catch (Exception e) {
            showMessage("Error opening registry: " + e.getMessage(), "error");
            e.printStackTrace(); // For debugging
        }
    }
    
    private String determineUserType(User user) {
        return switch (user.getRole()) {
            case ADMIN -> "admin";
            case USER -> "user"; // For now, all users start as basic users
            default -> "user";
        };
    }
    
    private void showMessage(String message, String type) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add("message-" + type);
    }
}