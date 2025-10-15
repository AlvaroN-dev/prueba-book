package com.codeup.booknova.ui.controller;

import com.codeup.booknova.ui.NovaBookApplication;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller for the User Dashboard view
 */
public class UserDashboardController {
    
    @FXML private TableView<String> catalogTable;
    @FXML private TextField searchField;
    @FXML private TextArea bookDetailsArea;
    
    @FXML
    private void initialize() {
        setupTable();
    }
    
    private void setupTable() {
        // Setup catalog table
        if (catalogTable != null) {
            catalogTable.getColumns().clear();
            TableColumn<String, String> bookCol = new TableColumn<>("Catálogo de Libros");
            bookCol.setCellValueFactory(new PropertyValueFactory<>("value"));
            catalogTable.getColumns().add(bookCol);
            catalogTable.getItems().addAll(
                "El Quijote - Miguel de Cervantes",
                "Cien años de soledad - Gabriel García Márquez",
                "1984 - George Orwell",
                "El Principito - Antoine de Saint-Exupéry",
                "Rayuela - Julio Cortázar",
                "La Casa de los Espíritus - Isabel Allende"
            );
            
            // Add selection listener
            catalogTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    showBookDetails(newSelection);
                }
            });
        }
    }
    
    private void showBookDetails(String bookTitle) {
        if (bookDetailsArea != null) {
            String details = switch (bookTitle) {
                case "El Quijote - Miguel de Cervantes" -> 
                    "Título: Don Quijote de la Mancha\n" +
                    "Autor: Miguel de Cervantes\n" +
                    "Año: 1605\n" +
                    "Género: Novela\n" +
                    "Estado: Disponible\n" +
                    "Descripción: La historia del ingenioso hidalgo don Quijote de la Mancha.";
                    
                case "Cien años de soledad - Gabriel García Márquez" ->
                    "Título: Cien años de soledad\n" +
                    "Autor: Gabriel García Márquez\n" +
                    "Año: 1967\n" +
                    "Género: Realismo mágico\n" +
                    "Estado: Disponible\n" +
                    "Descripción: La saga de la familia Buendía en Macondo.";
                    
                case "1984 - George Orwell" ->
                    "Título: 1984\n" +
                    "Autor: George Orwell\n" +
                    "Año: 1949\n" +
                    "Género: Distopía\n" +
                    "Estado: Prestado\n" +
                    "Descripción: Una novela distópica sobre el totalitarismo.";
                    
                default -> 
                    "Título: " + bookTitle + "\n" +
                    "Estado: Disponible\n" +
                    "Descripción: Información detallada no disponible.";
            };
            
            bookDetailsArea.setText(details);
        }
    }
    
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            showAlert("Búsqueda", "Buscando: " + searchTerm + "\n(Funcionalidad a implementar)");
        }
    }
    
    @FXML
    private void handleRequestMembership() {
        showAlert("Membresía", "Solicitar membresía de biblioteca\n(Funcionalidad a implementar)");
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