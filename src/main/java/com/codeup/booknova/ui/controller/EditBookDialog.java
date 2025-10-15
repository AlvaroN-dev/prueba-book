package com.codeup.booknova.ui.controller;

import com.codeup.booknova.domain.Book;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * Dialog for editing existing books.
 * Allows modification of book details including title, author, ISBN, and stock.
 */
public class EditBookDialog extends Dialog<Book> {
    
    private TextField isbnField;
    private TextField titleField;
    private TextField authorField;
    private Spinner<Integer> stockSpinner;
    private Book originalBook;
    
    /**
     * Creates a new EditBookDialog with the existing book data.
     * 
     * @param book The book to edit
     */
    public EditBookDialog(Book book) {
        this.originalBook = book;
        initializeDialog();
        createContent();
        loadBookData();
        setupResultConverter();
    }
    
    /**
     * Initializes the dialog properties.
     */
    private void initializeDialog() {
        setTitle("Edit Book");
        setHeaderText("Modify the book details");
        
        // Add button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Enable/disable save button
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);
    }
    
    /**
     * Creates the form content with all input fields.
     */
    private void createContent() {
        // Create form grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        // ISBN field (disabled - cannot be edited)
        isbnField = new TextField();
        isbnField.setPromptText("ISBN of the book");
        isbnField.setDisable(true); // Disable ISBN editing
        isbnField.setStyle("-fx-opacity: 1.0;"); // Keep text fully visible even when disabled
        
        // Title field
        titleField = new TextField();
        titleField.setPromptText("Book title");
        
        // Author field
        authorField = new TextField();
        authorField.setPromptText("Author name");
        
        // Stock spinner
        stockSpinner = new Spinner<>(0, 9999, 1);
        stockSpinner.setEditable(true);
        
        // Add fields to grid
        grid.add(new Label("ISBN:"), 0, 0);
        grid.add(isbnField, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Author:"), 0, 2);
        grid.add(authorField, 1, 2);
        grid.add(new Label("Stock:"), 0, 3);
        grid.add(stockSpinner, 1, 3);
        
        getDialogPane().setContent(grid);
        
        // Enable save button when all fields are filled
        Button saveButton = (Button) getDialogPane().lookupButton(
            getDialogPane().getButtonTypes().get(0));
        
        // Add listeners for validation (ISBN is excluded since it's disabled)
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(!isFormValid());
        });
        authorField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(!isFormValid());
        });
        
        // Request focus on title field
        javafx.application.Platform.runLater(() -> titleField.requestFocus());
    }
    
    /**
     * Loads the existing book data into the form fields.
     */
    private void loadBookData() {
        if (originalBook != null) {
            isbnField.setText(originalBook.getIsbn());
            titleField.setText(originalBook.getTitle());
            authorField.setText(originalBook.getAuthor());
            stockSpinner.getValueFactory().setValue(originalBook.getStock());
        }
    }
    
    /**
     * Validates that all required fields are filled.
     * Note: ISBN is not validated as it's disabled and cannot be edited.
     * 
     * @return true if form is valid, false otherwise
     */
    private boolean isFormValid() {
        return !titleField.getText().trim().isEmpty() &&
               !authorField.getText().trim().isEmpty();
    }
    
    /**
     * Sets up the result converter to create a Book object with the updated data.
     */
    private void setupResultConverter() {
        setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                if (isFormValid()) {
                    // Create updated book with original ID
                    Book updatedBook = new Book(
                        isbnField.getText().trim(),
                        titleField.getText().trim(),
                        authorField.getText().trim(),
                        stockSpinner.getValue()
                    );
                    updatedBook.setId(originalBook.getId());
                    return updatedBook;
                }
            }
            return null;
        });
    }
}
