package com.codeup.booknova.ui.controller;

import com.codeup.booknova.domain.AccessLevel;
import com.codeup.booknova.domain.User;
import com.codeup.booknova.domain.UserRole;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * Dialog for user registration
 */
public class RegisterDialog extends Dialog<User> {
    
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<UserRole> roleComboBox;
    
    public RegisterDialog() {
        initializeDialog();
        createContent();
        setupResultConverter();
    }
    
    private void initializeDialog() {
        setTitle("Registro de Usuario");
        setHeaderText("Complete los datos para registrar un nuevo usuario");
        
        // Add button types
        ButtonType registerButtonType = new ButtonType("Registrar", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);
        
        // Enable/disable register button
        Button registerButton = (Button) getDialogPane().lookupButton(registerButtonType);
        registerButton.setDisable(true);
    }
    
    private void createContent() {
        // Create form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        nameField = new TextField();
        nameField.setPromptText("Nombre completo");
        
        emailField = new TextField();
        emailField.setPromptText("Correo electrónico");
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirmar contraseña");
        
        phoneField = new TextField();
        phoneField.setPromptText("Teléfono");
        
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(UserRole.values());
        roleComboBox.setValue(UserRole.USER);
        
        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Contraseña:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(new Label("Confirmar:"), 0, 3);
        grid.add(confirmPasswordField, 1, 3);
        grid.add(new Label("Teléfono:"), 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(new Label("Rol:"), 0, 5);
        grid.add(roleComboBox, 1, 5);
        
        getDialogPane().setContent(grid);
        
        // Enable register button when all fields are filled
        Button registerButton = (Button) getDialogPane().lookupButton(
            getDialogPane().getButtonTypes().get(0));
        
        // Add listeners for validation with visual feedback
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(nameField, !newValue.trim().isEmpty());
            registerButton.setDisable(!isFormValid());
        });
        
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean valid = newValue.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
            validateField(emailField, valid);
            registerButton.setDisable(!isFormValid());
        });
        
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean valid = newValue.length() >= 8;
            validateField(passwordField, valid);
            
            // Also validate confirm password if it has content
            if (!confirmPasswordField.getText().isEmpty()) {
                boolean confirmValid = newValue.equals(confirmPasswordField.getText());
                validateField(confirmPasswordField, confirmValid);
            }
            registerButton.setDisable(!isFormValid());
        });
        
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean valid = newValue.equals(passwordField.getText()) && !newValue.isEmpty();
            validateField(confirmPasswordField, valid);
            registerButton.setDisable(!isFormValid());
        });
        
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Phone is optional, but if provided must be valid
            boolean valid = newValue.trim().isEmpty() || newValue.matches("^\\d{10,15}$");
            validateField(phoneField, valid);
            registerButton.setDisable(!isFormValid());
        });
        
        // Request focus on name field
        javafx.application.Platform.runLater(() -> nameField.requestFocus());
    }
    
    private void validateField(Control field, boolean isValid) {
        if (isValid) {
            field.setStyle("-fx-border-color: green; -fx-border-width: 1px;");
        } else {
            field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        }
    }
    
    private boolean isFormValid() {
        // Check if all required fields are filled
        if (nameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() ||
            passwordField.getText().isEmpty() ||
            confirmPasswordField.getText().isEmpty()) {
            return false;
        }
        
        // Check email format
        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return false;
        }
        
        // Check password length
        if (passwordField.getText().length() < 8) {
            return false;
        }
        
        // Check password confirmation
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            return false;
        }
        
        // Check phone if provided (optional but if provided should be valid)
        String phone = phoneField.getText().trim();
        if (!phone.isEmpty() && !phone.matches("^\\d{10,15}$")) {
            return false;
        }
        
        return true;
    }
    
    private void setupResultConverter() {
        setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                if (isFormValid()) {
                    User user = new User(
                        nameField.getText().trim(),
                        emailField.getText().trim(),
                        passwordField.getText(),
                        phoneField.getText().trim()
                    );
                    user.setRole(roleComboBox.getValue());
                    user.setAccessLevel(AccessLevel.READ_WRITE);
                    user.setActive(true);
                    user.setDeleted(false);
                    return user;
                }
            }
            return null;
        });
    }
}