package com.codeup.booknova.ui.controller;

import com.codeup.booknova.domain.Book;
import com.codeup.booknova.ui.NovaBookApplication;
import com.codeup.booknova.ui.model.BookTableModel;
import com.codeup.booknova.ui.service.ServiceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Controller for the Member Dashboard view
 */
public class MemberDashboardController {
    
    @FXML private TableView<BookTableModel> availableBooksTable;
    @FXML private TableView<String> myLoansTable;
    @FXML private TextField searchField;
    
    private ServiceManager serviceManager;
    private ObservableList<BookTableModel> availableBooksList;
    
    @FXML
    private void initialize() {
        serviceManager = ServiceManager.getInstance();
        availableBooksList = FXCollections.observableArrayList();
        
        setupAvailableBooksTable();
        setupMyLoansTable();
        loadData();
    }
    
    /**
     * Sets up the available books table with appropriate columns.
     * Columns are displayed in order: ID, ISBN, Title, Author, Available
     */
    private void setupAvailableBooksTable() {
        if (availableBooksTable != null) {
            availableBooksTable.getColumns().clear();
            
            // ID Column
            TableColumn<BookTableModel, Integer> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            idCol.setPrefWidth(50);
            
            // ISBN Column
            TableColumn<BookTableModel, String> isbnCol = new TableColumn<>("ISBN");
            isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
            isbnCol.setPrefWidth(130);
            
            // Title Column
            TableColumn<BookTableModel, String> titleCol = new TableColumn<>("Title");
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            titleCol.setPrefWidth(200);
            
            // Author Column
            TableColumn<BookTableModel, String> authorCol = new TableColumn<>("Author");
            authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
            authorCol.setPrefWidth(150);
            
            // Available Copies Column
            TableColumn<BookTableModel, Integer> stockCol = new TableColumn<>("Available");
            stockCol.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
            stockCol.setPrefWidth(80);
            
            // Add columns in the correct order: ID, ISBN, Title, Author, Available
            availableBooksTable.getColumns().addAll(idCol, isbnCol, titleCol, authorCol, stockCol);
            availableBooksTable.setItems(availableBooksList);
        }
    }
    
    private void setupMyLoansTable() {
        if (myLoansTable != null) {
            myLoansTable.getColumns().clear();
            // TODO: Implement proper loan table columns when loan history is needed
            // For now, show a simple message column
            TableColumn<String, String> messageCol = new TableColumn<>("My Loans");
            messageCol.setPrefWidth(800);
            messageCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()));
            myLoansTable.getColumns().add(messageCol);
            
            // Add placeholder message
            myLoansTable.getItems().clear();
            myLoansTable.getItems().add("Your loan history will appear here");
        }
    }
    
    private void loadData() {
        loadAvailableBooks();
        loadMyLoans();
    }
    
    private void loadAvailableBooks() {
        try {
            List<Book> books = serviceManager.getBookService().getAvailableBooks();
            availableBooksList.clear();
            
            for (Book book : books) {
                if (book.getStock() > 0) { // Only show books with stock
                    BookTableModel model = new BookTableModel(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        "General",
                        book.getStock(),
                        book.getStock()
                    );
                    availableBooksList.add(model);
                }
            }
        } catch (Exception e) {
            showAlert("Error", "No se pudieron cargar los libros: " + e.getMessage());
        }
    }
    
    private void loadMyLoans() {
        try {
            // For now, show sample data - in real implementation would load user's loans
            myLoansTable.getItems().clear();
            myLoansTable.getItems().addAll(
                "Libro ID: 1 - Vence: 15/11/2025",
                "Libro ID: 2 - Vence: 20/11/2025"
            );
        } catch (Exception e) {
            showAlert("Error", "No se pudieron cargar los préstamos: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            try {
                List<Book> books = serviceManager.getBookService().searchBooksByTitle(searchTerm);
                availableBooksList.clear();
                
                for (Book book : books) {
                    if (book.getStock() > 0) {
                        BookTableModel model = new BookTableModel(
                            book.getId(),
                            book.getTitle(),
                            book.getAuthor(),
                            book.getIsbn(),
                            "General",
                            book.getStock(),
                            book.getStock()
                        );
                        availableBooksList.add(model);
                    }
                }
                
                if (availableBooksList.isEmpty()) {
                    showAlert("Búsqueda", "No se encontraron libros con el término: " + searchTerm);
                }
            } catch (Exception e) {
                showAlert("Error", "Error en la búsqueda: " + e.getMessage());
            }
        } else {
            loadAvailableBooks(); // Reload all books if search is empty
        }
    }
    
    @FXML
    private void handleBorrowBook() {
        BookTableModel selectedBook = availableBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                // For now, just show confirmation - would need member ID and loan creation logic
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirmar Préstamo");
                confirmAlert.setHeaderText("¿Desea solicitar el préstamo del libro?");
                confirmAlert.setContentText("Libro: " + selectedBook.getTitle() + " - " + selectedBook.getAuthor());
                
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        showAlert("Préstamo", "Préstamo solicitado exitosamente\n(Funcionalidad en desarrollo)");
                        loadMyLoans(); // Refresh loans
                    }
                });
            } catch (Exception e) {
                showAlert("Error", "Error al solicitar préstamo: " + e.getMessage());
            }
        } else {
            showAlert("Seleccionar Libro", "Por favor, seleccione un libro de la lista");
        }
    }
    
    @FXML
    private void handleReturnBook() {
        String selectedLoan = myLoansTable.getSelectionModel().getSelectedItem();
        if (selectedLoan != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmar Devolución");
            confirmAlert.setHeaderText("¿Desea devolver este libro?");
            confirmAlert.setContentText("Préstamo: " + selectedLoan);
            
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    showAlert("Devolución", "Libro devuelto exitosamente\n(Funcionalidad en desarrollo)");
                    loadMyLoans(); // Refresh loans
                }
            });
        } else {
            showAlert("Seleccionar Préstamo", "Por favor, seleccione un préstamo de la lista");
        }
    }
    
    @FXML
    private void handleRenewLoan() {
        String selectedLoan = myLoansTable.getSelectionModel().getSelectedItem();
        if (selectedLoan != null) {
            showAlert("Renovación", "Préstamo renovado por 15 días más\n(Funcionalidad en desarrollo)");
            loadMyLoans(); // Refresh loans
        } else {
            showAlert("Seleccionar Préstamo", "Por favor, seleccione un préstamo de la lista");
        }
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
}