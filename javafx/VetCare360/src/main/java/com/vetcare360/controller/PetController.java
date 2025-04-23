package com.vetcare360.controller;

import com.vetcare360.model.Owner;
import com.vetcare360.model.Pet;
import com.vetcare360.model.Visit;
import com.vetcare360.service.OwnerService;
import com.vetcare360.service.PetService;
import com.vetcare360.service.VisitService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

/**
 * Controller for the pet details and new pet views.
 */
public class PetController {

    private final PetService petService = new PetService();
    private final OwnerService ownerService = new OwnerService();
    private final VisitService visitService = new VisitService();

    private Pet currentPet;
    private Owner currentOwner;
    private boolean petSaved = false;

    // FXML fields for all views
    @FXML private Label nameLabel;
    @FXML private Label typeLabel;
    @FXML private Label birthDateLabel;
    @FXML private Label ownerLabel;
    @FXML private TableView<Visit> visitsTable;
    @FXML private TableColumn<Visit, LocalDate> dateColumn;
    @FXML private TableColumn<Visit, String> vetColumn;
    @FXML private TableColumn<Visit, String> descriptionColumn;
    @FXML private TextField nameField;
    @FXML private TextField typeField;
    @FXML private DatePicker birthDatePicker;
    @FXML private Label errorLabel;

    /**
     * Initialize the controller.
     */
    @FXML
    private void initialize() {
        System.out.println("PetController initialized");
        
        try {
            // Initialize visit table if it exists (not in newPet.fxml)
            if (visitsTable != null) {
                // Initialize table columns
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                vetColumn.setCellValueFactory(cellData -> {
                    Visit visit = cellData.getValue();
                    if (visit != null && visit.getVet() != null) {
                        return javafx.beans.binding.Bindings.createStringBinding(
                            () -> visit.getVet().getFullName());
                    }
                    return javafx.beans.binding.Bindings.createStringBinding(() -> "");
                });
                descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

                // Set row click handler for visits
                visitsTable.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            showVisitDetails(newSelection);
                        }
                    });
            }
        } catch (Exception e) {
            System.err.println("Error initializing PetController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set the pet to display in pet details view.
     */
    public void setPet(Pet pet) {
        try {
            System.out.println("Setting pet: " + (pet != null ? pet.getName() : "null"));
            if (pet == null) {
                System.err.println("Error: Null pet passed to setPet");
                return;
            }

            this.currentPet = pet;
            this.currentOwner = pet.getOwner();

            // Check if UI components exist before using them
            if (nameLabel != null) nameLabel.setText(pet.getName());
            if (typeLabel != null) typeLabel.setText(pet.getType());
            if (birthDateLabel != null) birthDateLabel.setText(pet.getBirthDate().toString());
            if (ownerLabel != null) ownerLabel.setText(pet.getOwner() != null ? pet.getOwner().getFullName() : "");

            // Load the pet's visits if the table exists
            if (visitsTable != null) {
                List<Visit> visits = visitService.getVisitsByPetId(pet.getId());
                visitsTable.setItems(FXCollections.observableArrayList(visits != null ? visits : Collections.emptyList()));
            }

            // Set up the edit form if fields exist
            if (nameField != null) nameField.setText(pet.getName());
            if (typeField != null) typeField.setText(pet.getType());
            if (birthDatePicker != null) birthDatePicker.setValue(pet.getBirthDate());

        } catch (Exception e) {
            System.err.println("Error in setPet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set the owner for a new pet in the new pet form.
     */
    public void setOwner(Owner owner) {
        try {
            System.out.println("Setting owner: " + (owner != null ? owner.getFullName() : "null"));
            if (owner == null) {
                System.err.println("Error: Null owner passed to setOwner");
                return;
            }

            this.currentOwner = owner;
            this.currentPet = new Pet();
            this.currentPet.setOwner(owner);

            // We use debug prints to check which UI elements are available
            System.out.println("nameLabel: " + (nameLabel != null ? "present" : "null"));
            System.out.println("typeLabel: " + (typeLabel != null ? "present" : "null"));
            System.out.println("birthDateLabel: " + (birthDateLabel != null ? "present" : "null"));
            System.out.println("ownerLabel: " + (ownerLabel != null ? "present" : "null"));
            System.out.println("nameField: " + (nameField != null ? "present" : "null"));
            System.out.println("typeField: " + (typeField != null ? "present" : "null"));
            System.out.println("birthDatePicker: " + (birthDatePicker != null ? "present" : "null"));

            // Clear the display labels if they exist
            if (nameLabel != null) nameLabel.setText("");
            if (typeLabel != null) typeLabel.setText("");
            if (birthDateLabel != null) birthDateLabel.setText("");
            if (ownerLabel != null) ownerLabel.setText(owner.getFullName());

            // Clear the visits table if it exists
            if (visitsTable != null) {
                visitsTable.setItems(FXCollections.observableArrayList());
            }

            // Set up the edit form if fields exist
            if (nameField != null) nameField.setText("");
            if (typeField != null) typeField.setText("");
            if (birthDatePicker != null) birthDatePicker.setValue(LocalDate.now());

        } catch (Exception e) {
            System.err.println("Error in setOwner: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Save the pet (used in both new pet and pet details views).
     */
    @FXML
    private void savePet(ActionEvent event) {
        try {
            System.out.println("Attempting to save pet");
            
            // Check for required references
            if (currentPet == null) {
                showError("Error", "No pet to save");
                return;
            }
            
            if (currentOwner == null) {
                showError("Error", "No owner assigned to this pet");
                return;
            }

            // Verify UI components are available
            if (nameField == null || typeField == null || birthDatePicker == null) {
                showError("Error", "UI components not initialized properly");
                return;
            }

            // Get the form values
            String name = nameField.getText().trim();
            String type = typeField.getText().trim();
            LocalDate birthDate = birthDatePicker.getValue();

            System.out.println("Pet info: Name=" + name + ", Type=" + type + 
                            ", BirthDate=" + birthDate);

            // Basic validation
            if (name.isEmpty() || type.isEmpty() || birthDate == null) {
                if (errorLabel != null) {
                    errorLabel.setText("Please fill in all required fields");
                }
                showError("Validation Error", "Please fill in all required fields");
                return;
            }

            // Update the pet
            currentPet.setName(name);
            currentPet.setType(type);
            currentPet.setBirthDate(birthDate);

            // Set the owner if not already set
            if (currentPet.getOwner() == null) {
                currentPet.setOwner(currentOwner);
                currentOwner.addPet(currentPet);
            }

            // Save the pet
            System.out.println("Saving pet with name: " + currentPet.getName());
            petService.savePet(currentPet);
            System.out.println("Pet saved successfully with ID: " + currentPet.getId());

            // Also save the owner to ensure the relationship is persisted
            ownerService.saveOwner(currentOwner);

            // Set the flag
            petSaved = true;

            // Close the window
            closeWindow();

        } catch (Exception e) {
            System.err.println("Error saving pet: " + e.getMessage());
            e.printStackTrace();
            showError("Error", "Failed to save pet: " + e.getMessage());
        }
    }

    /**
     * Show the add visit view.
     */
    @FXML
    private void showAddVisit(ActionEvent event) {
        // Implementation omitted for brevity - this is for the pet details view
    }

    /**
     * Show the visit details.
     */
    private void showVisitDetails(Visit visit) {
        // Implementation omitted for brevity - this is for the pet details view
    }

    /**
     * Check if the pet was saved.
     */
    public boolean isPetSaved() {
        return petSaved;
    }

    /**
     * Cancel and close the window.
     */
    @FXML
    public void cancelButton(ActionEvent event) {
        closeWindow();
    }

    /**
     * Close the current window.
     */
    private void closeWindow() {
        try {
            // Try to find a UI component to get the Stage
            Control control = null;
            if (nameField != null) control = nameField;
            else if (typeField != null) control = typeField;
            else if (birthDatePicker != null) control = birthDatePicker;
            
            if (control != null && control.getScene() != null) {
                Stage stage = (Stage) control.getScene().getWindow();
                stage.close();
            } else {
                System.err.println("Cannot close window - no UI controls available");
            }
        } catch (Exception e) {
            System.err.println("Error closing window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Show an error dialog.
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
}