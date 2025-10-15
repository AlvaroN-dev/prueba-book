package com.codeup.booknova.ui.controller;

import com.codeup.booknova.domain.Book;
import com.codeup.booknova.domain.User;
import com.codeup.booknova.ui.NovaBookApplication;
import com.codeup.booknova.ui.model.BookTableModel;
import com.codeup.booknova.ui.service.ServiceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Controller for the User Dashboard view.
 * Handles user catalog browsing, book search, and book details display.
 */
public class UserDashboardController {
    
    @FXML private TableView<BookTableModel> catalogTable;
    @FXML private TextField searchField;
    @FXML private TextArea bookDetailsArea;
    @FXML private ComboBox<String> searchTypeComboBox;
    @FXML private Button refreshButton;
    @FXML private Button requestLoanButton;
    
    private ServiceManager serviceManager;
    private ObservableList<BookTableModel> booksList;
    private ObservableList<BookTableModel> allBooks;
    private Integer currentMemberId; // Member ID for the logged-in user
    private BookTableModel selectedBook; // Currently selected book
    private User currentUser; // Currently logged-in user
    
    /**
     * Sets the current logged-in user
     * 
     * @param user the currently logged-in user
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    /**
     * Sets the member ID for the current logged-in user.
     * This is required to enable loan functionality.
     * 
     * @param memberId the ID of the member associated with the current user
     */
    public void setCurrentMemberId(Integer memberId) {
        this.currentMemberId = memberId;
    }
    
    /**
     * Loads the membership status for the current user.
     * If the user has an approved membership, enables loan functionality.
     * This method should be called after setCurrentUser().
     */
    public void loadMembershipStatus() {
        if (currentUser == null) {
            return;
        }
        
        try {
            // Try to find member by user_id first
            var memberOptional = serviceManager.getMemberService().findMemberByUserId(currentUser.getId());
            
            // If not found by user_id, try searching by name as fallback
            if (memberOptional.isEmpty()) {
                var membersList = serviceManager.getMemberService().searchMembersByName(currentUser.getName());
                
                // Find exact match by name
                memberOptional = membersList.stream()
                    .filter(m -> m.getName().equalsIgnoreCase(currentUser.getName()))
                    .findFirst();
            }
            
            if (memberOptional.isPresent()) {
                var member = memberOptional.get();
                
                // Check if member is active and can borrow books
                if (member.canBorrow()) {
                    this.currentMemberId = member.getId();
                    
                    // Show welcome message only once
                    showAlert("Welcome Member", 
                        "Welcome " + currentUser.getName() + "!\n\n" +
                        "Your membership is active.\n" +
                        "You can now borrow books from the catalog.");
                }
            }
        } catch (Exception e) {
            // Log error but don't show to user (non-critical)
            // This prevents errors if user_id column doesn't exist yet
            System.err.println("Error loading membership status: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes the controller after FXML loading.
     * Sets up the table, loads data, and configures listeners.
     */
    @FXML
    private void initialize() {
        serviceManager = ServiceManager.getInstance();
        booksList = FXCollections.observableArrayList();
        allBooks = FXCollections.observableArrayList();
        
        setupTable();
        setupSearchType();
        loadBooks();
    }
    
    /**
     * Sets up the catalog table with appropriate columns and configurations.
     */
    @SuppressWarnings("unchecked")
    private void setupTable() {
        if (catalogTable != null) {
            catalogTable.getColumns().clear();
            
            // ID Column
            TableColumn<BookTableModel, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            idColumn.setPrefWidth(50);
            
            // Title Column
            TableColumn<BookTableModel, String> titleColumn = new TableColumn<>("Title");
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            titleColumn.setPrefWidth(250);
            
            // Author Column
            TableColumn<BookTableModel, String> authorColumn = new TableColumn<>("Author");
            authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
            authorColumn.setPrefWidth(200);
            
            // ISBN Column
            TableColumn<BookTableModel, String> isbnColumn = new TableColumn<>("ISBN");
            isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
            isbnColumn.setPrefWidth(150);
            
            // Stock Column
            TableColumn<BookTableModel, Integer> stockColumn = new TableColumn<>("Available");
            stockColumn.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
            stockColumn.setPrefWidth(100);
            
            catalogTable.getColumns().addAll(idColumn, titleColumn, authorColumn, isbnColumn, stockColumn);
            catalogTable.setItems(booksList);
            
            // Add selection listener to show book details
            catalogTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        showBookDetails(newValue);
                    }
                }
            );
        }
    }
    
    /**
     * Sets up the search type combo box with available search options.
     */
    private void setupSearchType() {
        if (searchTypeComboBox != null) {
            searchTypeComboBox.getItems().addAll("Title", "Author", "ISBN");
            searchTypeComboBox.getSelectionModel().selectFirst();
        }
    }
    
    /**
     * Loads all books from the database and displays them in the table.
     */
    private void loadBooks() {
        try {
            List<Book> books = serviceManager.getBookService().getAllBooks();
            booksList.clear();
            allBooks.clear();
            
            for (Book book : books) {
                BookTableModel model = new BookTableModel(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    "", // Genre not available in Book entity
                    book.getStock(), // Total copies
                    book.getStock()  // Available copies (simplified - same as total)
                );
                booksList.add(model);
                allBooks.add(model);
            }
            
            if (booksList.isEmpty()) {
                showAlert("Information", "No books available in the catalog.");
            }
        } catch (Exception e) {
            showAlert("Error", "Could not load books: " + e.getMessage());
        }
    }
    
    /**
     * Displays detailed information about the selected book.
     * Also enables/disables the request loan button based on availability and membership status.
     * 
     * @param book The selected book model
     */
    private void showBookDetails(BookTableModel book) {
        if (bookDetailsArea != null) {
            // Store selected book for loan request
            this.selectedBook = book;
            
            String availability = book.getAvailableCopies() > 0 ? "Available" : "Not Available";
            String stockInfo = book.getAvailableCopies() + " copies available";
            
            String details = String.format(
                "BOOK DETAILS\n" +
                "════════════════════════════════════════\n\n" +
                "ID:            %d\n" +
                "Title:         %s\n" +
                "Author:        %s\n" +
                "ISBN:          %s\n" +
                "Status:        %s\n" +
                "Stock:         %s\n\n" +
                "════════════════════════════════════════\n" +
                "This book is %s for borrowing.",
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                availability,
                stockInfo,
                availability.toLowerCase()
            );
            
            bookDetailsArea.setText(details);
            
            // Enable/disable loan button based on availability and membership
            if (requestLoanButton != null) {
                boolean canRequestLoan = book.getAvailableCopies() > 0 && currentMemberId != null;
                requestLoanButton.setDisable(!canRequestLoan);
            }
        }
    }
    
    /**
     * Handles the search functionality based on the selected search type.
     * Searches by title, author, or ISBN.
     */
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            // If search is empty, show all books
            booksList.setAll(allBooks);
            bookDetailsArea.clear();
            return;
        }
        
        String searchType = searchTypeComboBox.getSelectionModel().getSelectedItem();
        
        try {
            List<Book> searchResults;
            
            switch (searchType) {
                case "Title" -> searchResults = serviceManager.getBookService()
                    .searchBooksByTitle(searchTerm);
                    
                case "Author" -> searchResults = serviceManager.getBookService()
                    .searchBooksByAuthor(searchTerm);
                    
                case "ISBN" -> {
                    searchResults = serviceManager.getBookService()
                        .findBookByIsbn(searchTerm)
                        .map(List::of)
                        .orElse(List.of());
                }
                    
                default -> searchResults = List.of();
            }
            
            // Convert to BookTableModel
            booksList.clear();
            for (Book book : searchResults) {
                BookTableModel model = new BookTableModel(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    "",
                    book.getStock(),
                    book.getStock()
                );
                booksList.add(model);
            }
            
            if (booksList.isEmpty()) {
                showAlert("Search Results", 
                    String.format("No books found matching '%s' in %s.", searchTerm, searchType));
                bookDetailsArea.setText("No results found.\n\nTry a different search term or search type.");
            } else {
                showAlert("Search Results", 
                    String.format("Found %d book(s) matching '%s' in %s.", 
                        booksList.size(), searchTerm, searchType));
            }
            
        } catch (Exception e) {
            showAlert("Error", "Search failed: " + e.getMessage());
        }
    }
    
    /**
     * Handles the refresh button action.
     * Reloads all books from the database.
     */
    @FXML
    private void handleRefresh() {
        searchField.clear();
        bookDetailsArea.clear();
        loadBooks();
        showAlert("Success", "Catalog refreshed successfully!");
    }
    
    /**
     * Handles the request membership functionality.
     * Creates a new membership request that will be reviewed by an administrator.
     */
    @FXML
    private void handleRequestMembership() {
        // Validate user is logged in
        if (currentUser == null) {
            showErrorAlert("Session Error", "User session not found. Please login again.");
            return;
        }
        
        // Check if service manager is initialized
        if (serviceManager == null) {
            showErrorAlert("System Error", "Service manager not initialized. Please restart the application.");
            return;
        }
        
        // Check if membership request service is available
        if (serviceManager.getMembershipRequestService() == null) {
            showErrorAlert("System Error", "Membership request service is not available. Please contact support.");
            return;
        }
        
        // Check if user already has a pending request
        try {
            if (serviceManager.getMembershipRequestService().hasPendingRequest(currentUser.getId())) {
                showAlert("Information", 
                    "You already have a pending membership request.\n\n" +
                    "Please wait for an administrator to review your application.");
                return;
            }
        } catch (Exception e) {
            showErrorAlert("Database Error", 
                "Failed to check existing requests.\n\n" +
                "Error details: " + e.getMessage() + "\n\n" +
                "Please try again or contact support if the problem persists.");
            e.printStackTrace(); // Print stack trace for debugging
            return;
        }
        
        // Create dialog with text input for reason
        TextInputDialog reasonDialog = new TextInputDialog();
        reasonDialog.setTitle("Request Membership");
        reasonDialog.setHeaderText("Library Membership Request");
        reasonDialog.setContentText("Please tell us why you want to join (optional):");
        
        reasonDialog.showAndWait().ifPresent(reason -> {
            // Show confirmation dialog
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Request");
            confirmDialog.setHeaderText("Submit Membership Request");
            confirmDialog.setContentText(
                "Your request will be submitted with the following information:\n\n" +
                "Name: " + currentUser.getName() + "\n" +
                "Email: " + currentUser.getEmail() + "\n" +
                "Reason: " + (reason.trim().isEmpty() ? "(No reason provided)" : reason) + "\n\n" +
                "Benefits of membership:\n" +
                "• Borrow books from the catalog\n" +
                "• Access premium features\n" +
                "• Track your reading history\n\n" +
                "An administrator will review your request.\n\n" +
                "Do you want to proceed?"
            );
            
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Create membership request in database
                        var createdRequest = serviceManager.getMembershipRequestService().createRequest(
                            currentUser.getId(),
                            currentUser.getName(),
                            currentUser.getEmail(),
                            reason.trim().isEmpty() ? null : reason.trim()
                        );
                        
                        // Show success message
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Request Submitted");
                        successAlert.setHeaderText("Membership Request Submitted Successfully");
                        successAlert.setContentText(
                            "Your membership request has been submitted!\n\n" +
                            "Request ID: " + createdRequest.getId() + "\n" +
                            "Status: " + createdRequest.getStatus() + "\n\n" +
                            "An administrator will review your application and you will be notified once approved.\n\n" +
                            "Thank you for your interest in joining our library!"
                        );
                        successAlert.showAndWait();
                        
                    } catch (Exception e) {
                        showErrorAlert("Request Failed", 
                            "Failed to submit membership request.\n\n" +
                            "Error: " + e.getMessage() + "\n\n" +
                            "Please try again or contact support if the problem persists.");
                        e.printStackTrace(); // Print stack trace for debugging
                    }
                }
            });
        });
    }
    
    /**
     * Handles the view available books only action.
     * Filters the catalog to show only books with stock > 0.
     */
    @FXML
    private void handleViewAvailableOnly() {
        try {
            List<Book> availableBooks = serviceManager.getBookService().getAvailableBooks();
            booksList.clear();
            
            for (Book book : availableBooks) {
                BookTableModel model = new BookTableModel(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    "",
                    book.getStock(),
                    book.getStock()
                );
                booksList.add(model);
            }
            
            showAlert("Available Books", 
                String.format("Showing %d available book(s).", booksList.size()));
                
        } catch (Exception e) {
            showAlert("Error", "Could not load available books: " + e.getMessage());
        }
    }
    
    /**
     * Handles the view all books action.
     * Shows all books in the catalog.
     */
    @FXML
    private void handleViewAllBooks() {
        loadBooks();
    }
    
    /**
     * Handles the clear search action.
     * Clears the search field and shows all books.
     */
    @FXML
    private void handleClearSearch() {
        searchField.clear();
        bookDetailsArea.clear();
        booksList.setAll(allBooks);
    }
    
    /**
     * Handles the request loan button action.
     * Creates a new loan for the selected book if the user has an active membership.
     */
    @FXML
    private void handleRequestLoan() {
        // Validate that a book is selected
        if (selectedBook == null) {
            showAlert("No Book Selected", "Please select a book from the catalog first.");
            return;
        }
        
        // Validate that user has a member ID
        if (currentMemberId == null) {
            Alert confirmDialog = new Alert(Alert.AlertType.WARNING);
            confirmDialog.setTitle("Membership Required");
            confirmDialog.setHeaderText("Active Membership Needed");
            confirmDialog.setContentText(
                "You need an active membership to borrow books.\n\n" +
                "Would you like to request a membership now?"
            );
            
            ButtonType requestButton = new ButtonType("Request Membership");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmDialog.getButtonTypes().setAll(requestButton, cancelButton);
            
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == requestButton) {
                    handleRequestMembership();
                }
            });
            return;
        }
        
        // Validate book availability
        if (selectedBook.getAvailableCopies() <= 0) {
            showAlert("Book Not Available", 
                "Sorry, this book is currently out of stock.\n\n" +
                "Please try again later or select a different book.");
            return;
        }
        
        // Show confirmation dialog with loan details
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Loan Request");
        confirmDialog.setHeaderText("Request Book Loan");
        confirmDialog.setContentText(
            "Book: " + selectedBook.getTitle() + "\n" +
            "Author: " + selectedBook.getAuthor() + "\n" +
            "ISBN: " + selectedBook.getIsbn() + "\n\n" +
            "Loan Period: 14 days\n" +
            "Available Copies: " + selectedBook.getAvailableCopies() + "\n\n" +
            "Do you want to proceed with this loan?"
        );
        
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                processLoanRequest();
            }
        });
    }
    
    /**
     * Processes the loan request by creating a new loan in the database.
     */
    private void processLoanRequest() {
        try {
            // Create the loan with default period of 14 days
            var loan = serviceManager.getLoanService().createLoan(
                currentMemberId, 
                selectedBook.getId(), 
                14
            );
            
            // Show success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Loan Successful");
            successAlert.setHeaderText("Book Loan Created");
            successAlert.setContentText(
                "Your loan has been successfully created!\n\n" +
                "Loan ID: " + loan.getId() + "\n" +
                "Book: " + selectedBook.getTitle() + "\n" +
                "Due Date: " + loan.getDateDue() + "\n\n" +
                "Please return the book by the due date to avoid penalties."
            );
            successAlert.showAndWait();
            
            // Refresh the book list to update available copies
            loadBooks();
            
            // Clear selection
            catalogTable.getSelectionModel().clearSelection();
            bookDetailsArea.clear();
            selectedBook = null;
            requestLoanButton.setDisable(true);
            
        } catch (Exception e) {
            // Handle different types of errors
            String errorMessage;
            if (e.getMessage().contains("not eligible")) {
                errorMessage = "Your membership is not eligible to borrow books.\n" +
                             "Please contact an administrator for assistance.";
            } else if (e.getMessage().contains("limit")) {
                errorMessage = "You have reached your borrowing limit.\n" +
                             "Please return some books before borrowing more.";
            } else if (e.getMessage().contains("not available")) {
                errorMessage = "This book is no longer available.\n" +
                             "The last copy may have just been borrowed.";
            } else {
                errorMessage = "Failed to create loan: " + e.getMessage() + "\n\n" +
                             "Please try again or contact support if the problem persists.";
            }
            
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Loan Request Failed");
            errorAlert.setHeaderText("Unable to Process Loan");
            errorAlert.setContentText(errorMessage);
            errorAlert.showAndWait();
            
            // Refresh the book list in case stock changed
            loadBooks();
        }
    }
    
    /**
     * Handles the logout action.
     * Returns to the login view.
     */
    @FXML
    private void handleLogout() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Logout");
        confirmDialog.setHeaderText("Confirm Logout");
        confirmDialog.setContentText("Are you sure you want to logout?");
        
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                NovaBookApplication.showLoginView();
            }
        });
    }
    
    /**
     * Displays an alert dialog with the specified title and message.
     * 
     * @param title   The alert title
     * @param message The alert message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Displays an error alert dialog with the specified title and message.
     * 
     * @param title   The alert title
     * @param message The error message
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("An Error Occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
}