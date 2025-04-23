package com.vetcare360.controller;

import com.vetcare360.model.Owner;
import com.vetcare360.model.Pet;
import com.vetcare360.service.OwnerService;
import com.vetcare360.service.PetService;
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

/**
 * Controller for the owner details view.
 * This controller handles displaying and editing owner details.
 */
public class OwnerController {
    
    private final OwnerService ownerService = new OwnerService();
    private final PetService petService = new PetService();
    
    private Owner currentOwner;
    
    @FXML
    private Label nameLabel;
    
    @FXML
    private Label addressLabel;
    
    @FXML
    private Label cityLabel;
    
    @FXML
    private Label telephoneLabel;
    
    @FXML
    private TableView<Pet> petsTable;
    
    @FXML
    private TableColumn<Pet, String> nameColumn;
    
    @FXML
    private TableColumn<Pet, String> typeColumn;
    
    @FXML
    private TableColumn<Pet, Integer> ageColumn;
    
    /**
     * Initialize the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Set up the table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        
        // Add a row click handler
        petsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showPetDetails(newSelection);
            }
        });
    }
    
    /**
     * Set the owner to display.
     * @param owner the owner to display
     */
    public void setOwner(Owner owner) {
        this.currentOwner = owner;
        
        // Update the UI with owner details
        nameLabel.setText(owner.getFullName());
        addressLabel.setText(owner.getAddress());
        cityLabel.setText(owner.getCity());
        telephoneLabel.setText(owner.getTelephone());
        
        // Load the owner's pets
        petsTable.setItems(FXCollections.observableArrayList(owner.getPets()));
    }
    
    /**
     * Show the edit owner view.
     * @param event the action event
     */
    @FXML
    private void showEditOwner(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vetcare360/view/newOwner.fxml"));
            Parent root = loader.load();
            
            NewOwnerController controller = loader.getController();
            controller.setOwner(currentOwner);
            
            Stage stage = new Stage();
            stage.setTitle("Edit Owner");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            // Refresh the owner details after editing
            if (controller.isOwnerSaved()) {
                setOwner(ownerService.getOwnerById(currentOwner.getId()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Could not open the edit owner view.");
        }
    }


    /**
     * Show the add pet view.
     * @param event the action event
     */
    @FXML
    private void showAddPet(ActionEvent event) {
        try {
            System.out.println("Attempting to open 'Add Pet' view.");

            // Chemin vers newPet.fxml
            String resourcePath = "/com/vetcare360/view/newPet.fxml";
            var resource = getClass().getResource(resourcePath);

            // Vérification de la ressource FXML
            if (resource == null) {
                System.err.println("FXML resource not found: " + resourcePath);
                showError("Resource Not Found", "Could not locate: " + resourcePath);
                return;
            }

            System.out.println("FXML resource loaded from: " + resource.toExternalForm());
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            // Contrôleur de la vue
            PetController controller = loader.getController();
            if (controller == null) {
                System.err.println("PetController not initialized.");
                showError("Controller Error", "Could not initialize PetController.");
                return;
            }

            // Configuration du propriétaire dans PetController
            controller.setOwner(currentOwner);

            // Création et affichage de la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Add New Pet");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Rafraîchissement de la table des animaux si un pet a été sauvegardé
            if (controller.isPetSaved()) {
                System.out.println("New pet added successfully. Refreshing owner details.");
                setOwner(ownerService.getOwnerById(currentOwner.getId()));
            }
        } catch (IOException e) {
            System.err.println("Error loading Add Pet view: " + e.getMessage());
            e.printStackTrace();
            showError("Loading Error", "Failed to load the Add Pet view: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            showError("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
    }


    /**
     * Show the pet details view.
     * @param pet the pet to display
     */
    private void showPetDetails(Pet pet) {
        try {
            System.out.println("Attempting to open pet details for: " + pet.getName());

            String resourcePath = "/com/vetcare360/view/petDetails.fxml";
            var resource = getClass().getResource(resourcePath);

            if (resource == null) {
                showError("Resource Error", "Could not find the petDetails.fxml file.");
                System.err.println("Resource not found: " + resourcePath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            PetController controller = loader.getController();
            if (controller == null) {
                showError("Controller Error", "Could not load the PetController.");
                return;
            }

            controller.setPet(pet);

            Stage stage = new Stage();
            stage.setTitle("Pet Details: " + pet.getName());
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh the pets table after editing
            if (controller.isPetSaved()) {
                System.out.println("Pet was saved, refreshing owner data");
                setOwner(ownerService.getOwnerById(currentOwner.getId()));
            } else {
                System.out.println("Pet was not saved");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Could not open the pet details view: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected Error", "An unexpected error occurred while showing pet details: " + e.getMessage());
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