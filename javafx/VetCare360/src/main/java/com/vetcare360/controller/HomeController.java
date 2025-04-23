package com.vetcare360.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the home view.
 * This controller handles the home page functionality.
 */
public class HomeController {
    
    @FXML
    private BorderPane contentPane;
    
    /**
     * Initialize the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialization code if needed
    }
    
    /**
     * Show the owner search view.
     * This method is called when the "Find Owners" button is clicked.
     * @param event the action event
     */
    @FXML
    private void showOwnerSearch(ActionEvent event) {
        try {
            // Get the main controller to handle navigation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vetcare360/view/main.fxml"));
            Parent root = loader.load();
            MainController controller = loader.getController();
            
            // Call the showOwnerSearch method on the main controller
            controller.showOwnerSearch(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Show the veterinarians view.
     * This method is called when the "Veterinarians" button is clicked.
     * @param event the action event
     */
    @FXML
    private void showVets(ActionEvent event) {
        try {
            // Get the main controller to handle navigation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vetcare360/view/main.fxml"));
            Parent root = loader.load();
            MainController controller = loader.getController();
            
            // Call the showVets method on the main controller
            controller.showVets(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}