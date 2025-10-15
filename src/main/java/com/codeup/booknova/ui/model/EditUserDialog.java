package com.codeup.booknova.ui.model;

import com.codeup.booknova.domain.AccessLevel;
import com.codeup.booknova.domain.User;
import com.codeup.booknova.domain.UserRole;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

/**
 * Dialog for editing an existing user's information.
 * Provides a form to modify user details including name, email, phone, role, and access level.
 */
public class EditUserDialog extends Dialog<User> {
    
    private final TextField nameField;
    private final TextField emailField;
    private final TextField phoneField;
    private final ComboBox<UserRole> roleComboBox;
    private final ComboBox<AccessLevel> accessLevelComboBox;
    private final CheckBox activeCheckBox;
    private final PasswordField passwordField;
    private final CheckBox changePasswordCheckBox;
    private final User originalUser;
    
    /**
     * Creates a new EditUserDialog with the given user's information.
     * 
     * @param user the user to edit
     */
    public EditUserDialog(User user) {
        this.originalUser = user;
        
        setTitle("Edit User");
        setHeaderText("Modify user information");
        
        // Create the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        nameField = new TextField(user.getName());
        nameField.setPromptText("Full Name");
        
        emailField = new TextField(user.getEmail());
        emailField.setPromptText("Email");
        
        phoneField = new TextField(user.getPhone());
        phoneField.setPromptText("Phone");
        
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(UserRole.values());
        roleComboBox.setValue(user.getRole() != null ? user.getRole() : UserRole.USER);
        
        accessLevelComboBox = new ComboBox<>();
        accessLevelComboBox.getItems().addAll(AccessLevel.values());
        accessLevelComboBox.setValue(user.getAccessLevel() != null ? user.getAccessLevel() : AccessLevel.READ_WRITE);
        
        activeCheckBox = new CheckBox("Active");
        activeCheckBox.setSelected(user.getActive());
        
        changePasswordCheckBox = new CheckBox("Change Password");
        changePasswordCheckBox.setSelected(false);
        
        passwordField = new PasswordField();
        passwordField.setPromptText("New Password (optional)");
        passwordField.setDisable(true);
        
        // Enable/disable password field based on checkbox
        changePasswordCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            passwordField.setDisable(!newVal);
            if (!newVal) {
                passwordField.clear();
            }
        });
        
        // Add fields to grid
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Role:"), 0, 3);
        grid.add(roleComboBox, 1, 3);
        grid.add(new Label("Access Level:"), 0, 4);
        grid.add(accessLevelComboBox, 1, 4);
        grid.add(activeCheckBox, 1, 5);
        grid.add(changePasswordCheckBox, 1, 6);
        grid.add(new Label("Password:"), 0, 7);
        grid.add(passwordField, 1, 7);
        
        getDialogPane().setContent(grid);
        
        // Add buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Enable/Disable save button based on validation
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(false);
        
        // Validate input
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });
        
        // Convert result to User when save is clicked
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                User updatedUser = new User();
                updatedUser.setId(originalUser.getId());
                updatedUser.setName(nameField.getText().trim());
                updatedUser.setEmail(emailField.getText().trim());
                updatedUser.setPhone(phoneField.getText().trim());
                updatedUser.setRole(roleComboBox.getValue());
                updatedUser.setAccessLevel(accessLevelComboBox.getValue());
                updatedUser.setActive(activeCheckBox.isSelected());
                
                // Keep original password if not changed
                if (changePasswordCheckBox.isSelected() && !passwordField.getText().isEmpty()) {
                    updatedUser.setPassword(passwordField.getText());
                } else {
                    updatedUser.setPassword(originalUser.getPassword());
                }
                
                updatedUser.setCreatedAt(originalUser.getCreatedAt());
                updatedUser.setUpdatedAt(originalUser.getUpdatedAt());
                
                return updatedUser;
            }
            return null;
        });
    }
    
    /**
     * Shows the dialog and waits for user input.
     * 
     * @return an Optional containing the modified user if saved, empty otherwise
     */
    public Optional<User> showDialog() {
        return showAndWait();
    }
}
