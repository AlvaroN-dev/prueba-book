package com.codeup.booknova.ui.controller;

import com.codeup.booknova.domain.Book;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * Dialog for book creation and editing
 */
public class BookDialog extends Dialog<Book> {
    
    private TextField isbnField;
    private TextField titleField;
    private TextField authorField;
    private Spinner<Integer> stockSpinner;
    
    public BookDialog() {
        initializeDialog();
        createContent();
        setupResultConverter();
    }
    
    private void initializeDialog() {
        setTitle("Add Book");
        setHeaderText("Fill in the book details");
        
        // Add button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        // Enable/disable add button
        Button addButton = (Button) getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);
    }
    
    private void createContent() {
        // Create form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        isbnField = new TextField();
        isbnField.setPromptText("ISBN of the book");
        
        titleField = new TextField();
        titleField.setPromptText("Book title");
        
        authorField = new TextField();
        authorField.setPromptText("Author of the book");
        
        stockSpinner = new Spinner<>(1, 999, 1);
        stockSpinner.setEditable(true);
        
        grid.add(new Label("ISBN:"), 0, 0);
        grid.add(isbnField, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Author:"), 0, 2);
        grid.add(authorField, 1, 2);
        grid.add(new Label("Stock:"), 0, 3);
        grid.add(stockSpinner, 1, 3);
        
        getDialogPane().setContent(grid);
        
        // Enable add button when all fields are filled
        Button addButton = (Button) getDialogPane().lookupButton(
            getDialogPane().getButtonTypes().get(0));
        
        // Add listeners for validation
        isbnField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(!isFormValid());
        });
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(!isFormValid());
        });
        authorField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(!isFormValid());
        });
        
        // Request focus on ISBN field
        javafx.application.Platform.runLater(() -> isbnField.requestFocus());
    }
    
    private boolean isFormValid() {
        return !isbnField.getText().trim().isEmpty() &&
               !titleField.getText().trim().isEmpty() &&
               !authorField.getText().trim().isEmpty();
    }
    
    private void setupResultConverter() {
        setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                if (isFormValid()) {
                    return new Book(
                        isbnField.getText().trim(),
                        titleField.getText().trim(),
                        authorField.getText().trim(),
                        stockSpinner.getValue()
                    );
                }
            }
            return null;
        });
    }
}