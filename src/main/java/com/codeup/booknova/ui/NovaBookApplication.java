package com.codeup.booknova.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Main JavaFX Application class for NovaBook
 */
public class NovaBookApplication extends Application {
    
    private static Stage primaryStage;
    
    @Override
    public void start(Stage stage) throws Exception {
        // Configuración de logging
        Logger logger = Logger.getLogger("NovaBook");
        try {
            FileHandler fh = new FileHandler("app.log", true); // Append mode
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.info("Aplicación iniciada");
        } catch (IOException e) {
            System.err.println("Error al configurar logging: " + e.getMessage());
        }

        primaryStage = stage;
        primaryStage.setTitle("NovaBook - Sistema de Gestión de Biblioteca");
        primaryStage.setResizable(false);
        
        showLoginView();
    }
    
    public static void showLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(NovaBookApplication.class.getResource("/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 400, 300);
            scene.getStylesheets().add(NovaBookApplication.class.getResource("/styles/application.css").toExternalForm());
            
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void showDashboard(String userType, String username) {
        try {
            String fxmlFile = switch (userType.toLowerCase()) {
                case "admin" -> "/fxml/admin-dashboard.fxml";
                case "member" -> "/fxml/member-dashboard.fxml";
                default -> "/fxml/user-dashboard.fxml";
            };
            
            FXMLLoader loader = new FXMLLoader(NovaBookApplication.class.getResource(fxmlFile));
            Scene scene = new Scene(loader.load(), 900, 600);
            scene.getStylesheets().add(NovaBookApplication.class.getResource("/styles/application.css").toExternalForm());
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("NovaBook - Dashboard " + userType + " (" + username + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}