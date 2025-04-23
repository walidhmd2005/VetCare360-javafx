package com.vetcare360.controller;

import com.vetcare360.model.Owner;
import com.vetcare360.service.OwnerService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the owner search view.
 * This controller handles searching for owners and displaying the results.
 */
public class OwnerSearchController {
    
    private final OwnerService ownerService = new OwnerService();
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TableView<Owner> ownersTable;
    
    @FXML
    private TableColumn<Owner, String> nameColumn;
    
    @FXML
    private TableColumn<Owner, String> addressColumn;
    
    @FXML
    private TableColumn<Owner, String> cityColumn;
    
    @FXML
    private TableColumn<Owner, String> telephoneColumn;
    
    @FXML
    private TableColumn<Owner, Integer> petsColumn;
    
    @FXML
    private Label statusLabel;
    
    /**
     * Initialize the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Set up the table columns
        nameColumn.setCellValueFactory(cellData -> {
            Owner owner = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(() -> owner.getFullName());
        });
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        petsColumn.setCellValueFactory(cellData -> {
            Owner owner = cellData.getValue();
            return javafx.beans.binding.Bindings.createObjectBinding(() -> owner.getPets().size());
        });
        
        // Add a row click handler
        ownersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showOwnerDetails(newSelection);
            }
        });
        
        // Load all owners initially
        searchOwners(null);
    }
    
    /**
     * Search for owners by last name.
     * @param event the action event
     */
    @FXML
    private void searchOwners(ActionEvent event) {
        String lastName = lastNameField.getText().trim();
        
        List<Owner> owners;
        if (lastName.isEmpty()) {
            // If no last name is provided, get all owners
            owners = ownerService.getAllOwners();
            statusLabel.setText("Showing all owners");
        } else {
            // Otherwise, search by last name
            owners = ownerService.findOwnersByLastName(lastName);
            statusLabel.setText("Found " + owners.size() + " owner(s) with last name containing '" + lastName + "'");
        }
        
        // Update the table
        ownersTable.setItems(FXCollections.observableArrayList(owners));
    }
    
    /**
     * Show the add owner view.
     * @param event the action event
     */
    @FXML
    private void showAddOwner(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vetcare360/view/newOwner.fxml"));
            Parent root = loader.load();
            
            NewOwnerController controller = loader.getController();
            
            Stage stage = new Stage();
            stage.setTitle("Add Owner");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            // Refresh the table if an owner was added
            if (controller.isOwnerSaved()) {
                searchOwners(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Could not open the add owner view.");
        }
    }
    
    /**
     * Show the owner details view.
     * @param owner the owner to display
     */
    private void showOwnerDetails(Owner owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vetcare360/view/ownerDetails.fxml"));
            Parent root = loader.load();
            
            OwnerController controller = loader.getController();
            controller.setOwner(owner);
            
            Stage stage = new Stage();
            stage.setTitle("Owner Details");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            // Refresh the table after viewing owner details
            searchOwners(null);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Could not open the owner details view.");
        }
    }
    
    /**
     * Show an error dialog.
     * @param title the dialog title
     * @param message the error message
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
}