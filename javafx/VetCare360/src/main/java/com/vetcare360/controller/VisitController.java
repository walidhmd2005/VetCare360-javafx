package com.vetcare360.controller;

import com.vetcare360.model.Pet;
import com.vetcare360.model.Vet;
import com.vetcare360.model.Visit;
import com.vetcare360.service.VetService;
import com.vetcare360.service.VisitService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for the new/edit visit view.
 * This controller handles adding and editing visits.
 */
public class VisitController {
    
    private final VisitService visitService = new VisitService();
    private final VetService vetService = new VetService();
    
    private Visit currentVisit;
    private Pet currentPet;
    private boolean visitSaved = false;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private ComboBox<Vet> vetComboBox;
    
    @FXML
    private Label petLabel;
    
    @FXML
    private Label errorLabel;
    
    /**
     * Initialize the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Set the date picker to today's date
        datePicker.setValue(LocalDate.now());
        
        // Load all vets into the combo box
        List<Vet> vets = vetService.getAllVets();
        vetComboBox.setItems(FXCollections.observableArrayList(vets));
        
        // Set the cell factory to display the vet's full name
        vetComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Vet item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFullName());
                }
            }
        });
        
        // Set the button cell to display the selected vet's full name
        vetComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Vet item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFullName());
                }
            }
        });
    }
    
    /**
     * Set the pet for a new visit.
     * @param pet the pet
     */
    public void setPet(Pet pet) {
        this.currentPet = pet;
        this.currentVisit = new Visit();
        
        // Update the pet label
        petLabel.setText(pet.getName() + " (" + pet.getType() + ")");
    }
    
    /**
     * Set the visit to edit.
     * @param visit the visit to edit
     */
    public void setVisit(Visit visit) {
        this.currentVisit = visit;
        this.currentPet = visit.getPet();
        
        // Update the form fields
        datePicker.setValue(visit.getDate());
        descriptionArea.setText(visit.getDescription());
        vetComboBox.setValue(visit.getVet());
        
        // Update the pet label
        Pet pet = visit.getPet();
        if (pet != null) {
            petLabel.setText(pet.getName() + " (" + pet.getType() + ")");
        }
    }
    
    /**
     * Save the visit.
     * @param event the action event
     */
    @FXML
    private void saveVisit(ActionEvent event) {
        // Get the form values
        LocalDate date = datePicker.getValue();
        String description = descriptionArea.getText().trim();
        Vet vet = vetComboBox.getValue();
        
        // Update the visit
        currentVisit.setDate(date);
        currentVisit.setDescription(description);
        currentVisit.setVet(vet);
        
        // Set the pet if this is a new visit
        if (currentVisit.getPet() == null) {
            currentVisit.setPet(currentPet);
            currentPet.addVisit(currentVisit);
        }
        
        // Validate the visit
        if (!visitService.validateVisit(currentVisit)) {
            errorLabel.setText("Please fill in all required fields.");
            return;
        }
        
        // Save the visit
        visitService.saveVisit(currentVisit);
        
        // Set the flag
        visitSaved = true;
        
        // Close the window
        ((Stage) datePicker.getScene().getWindow()).close();
    }
    
    /**
     * Cancel the operation.
     * @param event the action event
     */
    @FXML
    private void cancel(ActionEvent event) {
        // Close the window
        ((Stage) datePicker.getScene().getWindow()).close();
    }
    
    /**
     * Check if the visit was saved.
     * @return true if the visit was saved, false otherwise
     */
    public boolean isVisitSaved() {
        return visitSaved;
    }
}