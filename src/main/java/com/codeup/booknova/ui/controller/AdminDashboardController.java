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
    
    private ServiceManager serviceManager;
    private ObservableList<UserTableModel> usersList;
    private ObservableList<BookTableModel> booksList;
    
    @FXML
    private void initialize() {
        serviceManager = ServiceManager.getInstance();
        usersList = FXCollections.observableArrayList();
        booksList = FXCollections.observableArrayList();
        
        setupUsersTable();
        setupBooksTable();
        setupLoansTable();
        
        loadData();
    }
    
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
    
    private void setupBooksTable() {
        if (booksTable != null) {
            booksTable.getColumns().clear();
            
            TableColumn<BookTableModel, Integer> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            idCol.setPrefWidth(50);
            
            TableColumn<BookTableModel, String> titleCol = new TableColumn<>("Títule");
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            titleCol.setPrefWidth(200);
            
            TableColumn<BookTableModel, String> authorCol = new TableColumn<>("Author");
            authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
            authorCol.setPrefWidth(150);
            
            TableColumn<BookTableModel, String> genreCol = new TableColumn<>("Gender");
            genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
            genreCol.setPrefWidth(100);
            
            TableColumn<BookTableModel, Integer> availableCol = new TableColumn<>("Available");
            availableCol.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
            availableCol.setPrefWidth(100);
            
            booksTable.getColumns().addAll(idCol, titleCol, authorCol, genreCol, availableCol);
            booksTable.setItems(booksList);
        }
    }
    
    private void setupLoansTable() {
        if (loansTable != null) {
            loansTable.getColumns().clear();
            TableColumn<String, String> loanCol = new TableColumn<>("Active Loans");
            loanCol.setCellValueFactory(new PropertyValueFactory<>("value"));
            loansTable.getColumns().add(loanCol);
        }
    }
    
    private void loadData() {
        loadUsers();
        loadBooks();
        loadLoans();
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
                    "No gender", // Book doesn't have genre field
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
                String loanInfo = String.format("ID: %d - Member: %d - Book: %d - State: %s",
                    loan.getId(), loan.getMemberId(), loan.getBookId(), 
                    loan.getReturned() ? "Devuelto" : "Activo");
                loansTable.getItems().add(loanInfo);
            }
        } catch (Exception e) {
            showAlert("Error", "Loans could not be loaded: " + e.getMessage());
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
            showAlert("Error", "Error opening dialog:: " + e.getMessage());
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
                    showAlert("Error", "Error creating workbook: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            showAlert("Error", "Error opening the dialog box: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleManageLoans() {
        loadLoans(); // Refresh loans table
        showAlert("Information", "Updated loan table");
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
                showAlert("Éxito", "Book catalog exported to  " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Error al exportar CSV: " + e.getMessage());
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
                showAlert("Success", "Overdue loans exported to" + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Error exporting CSV: " + e.getMessage());
            }
        }
    }
}