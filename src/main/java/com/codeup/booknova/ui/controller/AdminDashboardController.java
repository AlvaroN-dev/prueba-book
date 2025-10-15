package com.codeup.booknova.ui.controller;

import com.codeup.booknova.domain.*;
import com.codeup.booknova.ui.NovaBookApplication;
import com.codeup.booknova.ui.model.*;
import com.codeup.booknova.ui.service.ServiceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Controller for the Admin Dashboard view
 */
public class AdminDashboardController {
    
    @FXML private TabPane mainTabPane;
    @FXML private TableView<UserTableModel> usersTable;
    @FXML private TableView<BookTableModel> booksTable;
    @FXML private TableView<String> loansTable;
    @FXML private TableView<MembershipRequestTableModel> membershipRequestsTable;
    
    private ServiceManager serviceManager;
    private ObservableList<UserTableModel> usersList;
    private ObservableList<BookTableModel> booksList;
    private ObservableList<MembershipRequestTableModel> membershipRequestsList;
    private Integer currentAdminId; // ID of the current admin user
    
    /**
     * Sets the current admin user ID
     * 
     * @param adminId the ID of the current admin user
     */
    public void setCurrentAdminId(Integer adminId) {
        this.currentAdminId = adminId;
    }
    
    @FXML
    private void initialize() {
        serviceManager = ServiceManager.getInstance();
        usersList = FXCollections.observableArrayList();
        booksList = FXCollections.observableArrayList();
        membershipRequestsList = FXCollections.observableArrayList();
        
        setupUsersTable();
        setupBooksTable();
        setupLoansTable();
        setupMembershipRequestsTable();
        
        loadData();
    }
    
    @SuppressWarnings("unchecked")
    private void setupUsersTable() {
        if (usersTable != null) {
            usersTable.getColumns().clear();
            
            TableColumn<UserTableModel, Integer> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            idCol.setPrefWidth(50);
            
            TableColumn<UserTableModel, String> nameCol = new TableColumn<>("Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            nameCol.setPrefWidth(150);
            
            TableColumn<UserTableModel, String> emailCol = new TableColumn<>("Email");
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            emailCol.setPrefWidth(200);
            
            TableColumn<UserTableModel, String> roleCol = new TableColumn<>("Role");
            roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
            roleCol.setPrefWidth(100);
            
            TableColumn<UserTableModel, Boolean> activeCol = new TableColumn<>("Active");
            activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
            activeCol.setPrefWidth(80);
            
            usersTable.getColumns().addAll(idCol, nameCol, emailCol, roleCol, activeCol);
            usersTable.setItems(usersList);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void setupBooksTable() {
        if (booksTable != null) {
            booksTable.getColumns().clear();
            
            TableColumn<BookTableModel, Integer> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            idCol.setPrefWidth(50);
            
            TableColumn<BookTableModel, String> titleCol = new TableColumn<>("Title");
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            titleCol.setPrefWidth(250);
            
            TableColumn<BookTableModel, String> authorCol = new TableColumn<>("Author");
            authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
            authorCol.setPrefWidth(200);
            
            TableColumn<BookTableModel, String> isbnCol = new TableColumn<>("ISBN");
            isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
            isbnCol.setPrefWidth(150);
            
            TableColumn<BookTableModel, Integer> availableCol = new TableColumn<>("Available Copies");
            availableCol.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
            availableCol.setPrefWidth(120);
            
            booksTable.getColumns().addAll(idCol, titleCol, authorCol, isbnCol, availableCol);
            booksTable.setItems(booksList);
        }
    }
    
    private void setupLoansTable() {
        if (loansTable != null) {
            loansTable.getColumns().clear();
            // TODO: Implement proper loan table columns when loan management is needed
            // For now, show a simple message column
            TableColumn<String, String> messageCol = new TableColumn<>("Loan Management");
            messageCol.setPrefWidth(800);
            messageCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()));
            loansTable.getColumns().add(messageCol);
            
            // Add placeholder message
            loansTable.getItems().clear();
            loansTable.getItems().add("Loan management feature - Coming soon");
        }
    }
    
    @SuppressWarnings("unchecked")
    private void setupMembershipRequestsTable() {
        if (membershipRequestsTable != null) {
            membershipRequestsTable.getColumns().clear();
            
            TableColumn<MembershipRequestTableModel, Integer> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            idCol.setPrefWidth(50);
            
            TableColumn<MembershipRequestTableModel, String> nameCol = new TableColumn<>("Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
            nameCol.setPrefWidth(150);
            
            TableColumn<MembershipRequestTableModel, String> emailCol = new TableColumn<>("Email");
            emailCol.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
            emailCol.setPrefWidth(200);
            
            TableColumn<MembershipRequestTableModel, String> statusCol = new TableColumn<>("Status");
            statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
            statusCol.setPrefWidth(100);
            
            TableColumn<MembershipRequestTableModel, String> dateCol = new TableColumn<>("Request Date");
            dateCol.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
            dateCol.setPrefWidth(150);
            
            membershipRequestsTable.getColumns().addAll(idCol, nameCol, emailCol, statusCol, dateCol);
            membershipRequestsTable.setItems(membershipRequestsList);
        }
    }
    
    private void loadData() {
        loadUsers();
        loadBooks();
        loadLoans();
        loadMembershipRequests();
    }
    
    private void loadUsers() {
        try {
            List<User> users = serviceManager.getUserService().getAllUsers();
            usersList.clear();
            
            for (User user : users) {
                UserTableModel model = new UserTableModel(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole() != null ? user.getRole().toString() : "USER",
                    user.getAccessLevel() != null ? user.getAccessLevel().toString() : "READ_WRITE",
                    user.getActive()
                );
                usersList.add(model);
            }
        } catch (Exception e) {
            showAlert("Error", "Users could not be loaded: " + e.getMessage());
        }
    }
    
    private void loadBooks() {
        try {
            List<Book> books = serviceManager.getBookService().getAllBooks();
            booksList.clear();
            
            for (Book book : books) {
                BookTableModel model = new BookTableModel(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    "No genre", // Book doesn't have genre field
                    book.getStock(), // total copies
                    book.getStock()  // available copies (simplified)
                );
                booksList.add(model);
            }
        } catch (Exception e) {
            showAlert("Error", "The books could not be loaded.: " + e.getMessage());
        }
    }
    
    private void loadLoans() {
        try {
            List<Loan> loans = serviceManager.getLoanService().getAllLoans();
            loansTable.getItems().clear();
            
            for (Loan loan : loans) {
                String loanInfo = String.format("ID: %d - Member: %d - Book: %d - Status: %s",
                    loan.getId(), loan.getMemberId(), loan.getBookId(), 
                    loan.getReturned() ? "Returned" : "Active");
                loansTable.getItems().add(loanInfo);
            }
        } catch (Exception e) {
            showAlert("Error", "Loans could not be loaded: " + e.getMessage());
        }
    }
    
    private void loadMembershipRequests() {
        try {
            List<MembershipRequest> requests = serviceManager.getMembershipRequestService().getAllRequests();
            membershipRequestsList.clear();
            
            for (MembershipRequest request : requests) {
                String requestDate = request.getRequestedAt() != null 
                    ? request.getRequestedAt().toString().substring(0, 19).replace('T', ' ')
                    : "N/A";
                
                MembershipRequestTableModel model = new MembershipRequestTableModel(
                    request.getId(),
                    request.getUserName(),
                    request.getUserEmail(),
                    request.getStatus(),
                    requestDate
                );
                membershipRequestsList.add(model);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load membership requests: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAddUser() {
        try {
            RegisterDialog dialog = new RegisterDialog();
            dialog.showAndWait().ifPresent(user -> {
                try {
                    serviceManager.getUserService().adminRegister(
                        user.getName(), user.getEmail(), user.getPassword(), user.getPhone(),
                        user.getRole().toString(), user.getAccessLevel().toString()
                    );
                    showAlert("Success", "User successfully created");
                    loadUsers(); // Refresh table
                } catch (Exception e) {
                    showAlert("Error", "Error creating user: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            showAlert("Error", "Error opening dialog: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleEditUser() {
        UserTableModel selectedUser = usersTable.getSelectionModel().getSelectedItem();
        
        if (selectedUser == null) {
            showAlert("Warning", "Please select a user to edit");
            return;
        }
        
        try {
            // Get full user from service
            User user = serviceManager.getUserService()
                .findUserById(selectedUser.getId())
                .orElse(null);
            
            if (user == null) {
                showAlert("Error", "User not found");
                return;
            }
            
            EditUserDialog dialog = new EditUserDialog(user);
            dialog.showAndWait().ifPresent(updatedUser -> {
                try {
                    serviceManager.getUserService().updateUser(updatedUser);
                    showAlert("Success", "User successfully updated");
                    loadUsers(); // Refresh table
                } catch (Exception e) {
                    showAlert("Error", "Error updating user: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            showAlert("Error", "Error opening edit dialog: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteUser() {
        UserTableModel selectedUser = usersTable.getSelectionModel().getSelectedItem();
        
        if (selectedUser == null) {
            showAlert("Warning", "Please select a user to delete");
            return;
        }
        
        // Confirmation dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete User");
        confirmDialog.setContentText("Are you sure you want to delete the user: " + selectedUser.getName() + "?");
        
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    serviceManager.getUserService().deleteUser(selectedUser.getId());
                    showAlert("Success", "User successfully deleted");
                    loadUsers(); // Refresh table
                } catch (Exception e) {
                    showAlert("Error", "Error deleting user: " + e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void handleToggleUserStatus() {
        UserTableModel selectedUser = usersTable.getSelectionModel().getSelectedItem();
        
        if (selectedUser == null) {
            showAlert("Warning", "Please select a user to activate/deactivate");
            return;
        }
        
        try {
            // Get full user from service
            User user = serviceManager.getUserService()
                .findUserById(selectedUser.getId())
                .orElse(null);
            
            if (user == null) {
                showAlert("Error", "User not found");
                return;
            }
            
            String action = user.getActive() ? "deactivate" : "activate";
            
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Action");
            confirmDialog.setHeaderText("Change User Status");
            confirmDialog.setContentText("Are you sure you want to " + action + " the user: " + user.getName() + "?");
            
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        if (user.getActive()) {
                            serviceManager.getUserService().deactivateUser(user.getId());
                            showAlert("Success", "User successfully deactivated");
                            loadUsers();
                        } else {
                            serviceManager.getUserService().activateUser(user.getId());
                            showAlert("Success", "User successfully activated");
                            loadUsers();
                        }
                    } catch (Exception e) {
                        showAlert("Error", "Error changing user status: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            showAlert("Error", "Error processing request: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAddBook() {
        try {
            BookDialog dialog = new BookDialog();
            dialog.showAndWait().ifPresent(book -> {
                try {
                    serviceManager.getBookService().addBook(
                        book.getIsbn(), book.getTitle(), book.getAuthor(), book.getStock());
                    showAlert("Success", "Book successfully created");
                    loadBooks(); // Refresh table
                } catch (Exception e) {
                    showAlert("Error", "Error creating book: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            showAlert("Error", "Error opening dialog: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleManageLoans() {
        loadLoans(); // Refresh loans table
        showAlert("Information", "Loans table updated");
    }
    
    @FXML
    private void handleApproveMembershipRequest() {
        MembershipRequestTableModel selected = membershipRequestsTable.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            showAlert("Warning", "Please select a membership request to approve");
            return;
        }
        
        if (!"PENDING".equals(selected.getStatus())) {
            showAlert("Warning", "Only pending requests can be approved");
            return;
        }
        
        // Validate admin is logged in
        if (currentAdminId == null) {
            showAlert("Error", "Admin session not found");
            return;
        }
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Approve Membership");
        confirmDialog.setHeaderText("Approve Membership Request");
        confirmDialog.setContentText(
            "Are you sure you want to approve the membership request for:\n\n" +
            "Name: " + selected.getUserName() + "\n" +
            "Email: " + selected.getUserEmail() + "\n\n" +
            "This will create a new member account and allow the user to borrow books."
        );
        
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    serviceManager.getMembershipRequestService().approveRequest(
                        selected.getId(), 
                        currentAdminId
                    );
                    showAlert("Success", "Membership request approved successfully!");
                    loadMembershipRequests(); // Refresh table
                } catch (Exception e) {
                    showAlert("Error", "Failed to approve request: " + e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void handleRejectMembershipRequest() {
        MembershipRequestTableModel selected = membershipRequestsTable.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            showAlert("Warning", "Please select a membership request to reject");
            return;
        }
        
        if (!"PENDING".equals(selected.getStatus())) {
            showAlert("Warning", "Only pending requests can be rejected");
            return;
        }
        
        // Validate admin is logged in
        if (currentAdminId == null) {
            showAlert("Error", "Admin session not found");
            return;
        }
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Reject Membership");
        confirmDialog.setHeaderText("Reject Membership Request");
        confirmDialog.setContentText(
            "Are you sure you want to reject the membership request for:\n\n" +
            "Name: " + selected.getUserName() + "\n" +
            "Email: " + selected.getUserEmail() + "\n\n" +
            "The user will not be granted library membership."
        );
        
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    serviceManager.getMembershipRequestService().rejectRequest(
                        selected.getId(), 
                        currentAdminId
                    );
                    showAlert("Success", "Membership request rejected");
                    loadMembershipRequests(); // Refresh table
                } catch (Exception e) {
                    showAlert("Error", "Failed to reject request: " + e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void handleRefreshMembershipRequests() {
        loadMembershipRequests();
        showAlert("Success", "Membership requests table refreshed");
    }
    
    @FXML
    private void handleLogout() {
        NovaBookApplication.showLoginView();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleExportBooksCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Book Catalog as CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                serviceManager.getBookService().exportBooksToCSV(file.getAbsolutePath());
                showAlert("Success", "Book catalog exported to " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Error exporting CSV: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleExportOverdueLoansCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Overdue Loans as CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                serviceManager.getLoanService().exportOverdueLoansToCSV(file.getAbsolutePath());
                showAlert("Success", "Overdue loans exported to " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Error exporting CSV: " + e.getMessage());
            }
        }
    }
}