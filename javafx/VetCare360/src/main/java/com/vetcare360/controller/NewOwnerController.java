package com.vetcare360.controller;

import com.vetcare360.model.Owner;
import com.vetcare360.service.OwnerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the new/edit owner view.
 * This controller handles adding and editing owners.
 */
public class NewOwnerController {
    
    private final OwnerService ownerService = new OwnerService();
    
    private Owner currentOwner;
    private boolean ownerSaved = false;
    
    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField addressField;
    
    @FXML
    private TextField cityField;
    
    @FXML
    private TextField telephoneField;
    
    @FXML
    private Label errorLabel;
    
    /**
     * Initialize the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialization code if needed
    }
    
    /**
     * Set the owner to edit.
     * @param owner the owner to edit
     */
    public void setOwner(Owner owner) {
        this.currentOwner = owner;
        
        // Update the form fields
        firstNameField.setText(owner.getFirstName());
        lastNameField.setText(owner.getLastName());
        addressField.setText(owner.getAddress());
        cityField.setText(owner.getCity());
        telephoneField.setText(owner.getTelephone());
    }
    
    /**
     * Save the owner.
     * @param event the action event
     */
    @FXML
    private void saveOwner(ActionEvent event) {
        // Get the form values
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String address = addressField.getText().trim();
        String city = cityField.getText().trim();
        String telephone = telephoneField.getText().trim();
        
        // Create or update the owner
        if (currentOwner == null) {
            currentOwner = new Owner(firstName, lastName, address, city, telephone);
        } else {
            currentOwner.setFirstName(firstName);
            currentOwner.setLastName(lastName);
            currentOwner.setAddress(address);
            currentOwner.setCity(city);
            currentOwner.setTelephone(telephone);
        }
        
        // Validate the owner
        if (!ownerService.validateOwner(currentOwner)) {
            errorLabel.setText("Please fill in all required fields and ensure the telephone number is valid.");
            return;
        }
        
        // Save the owner
        ownerService.saveOwner(currentOwner);
        
        // Set the flag
        ownerSaved = true;
        
        // Close the window
        ((Stage) firstNameField.getScene().getWindow()).close();
    }
    
    /**
     * Cancel the operation.
     * @param event the action event
     */
    @FXML
    private void cancel(ActionEvent event) {
        // Close the window
        ((Stage) firstNameField.getScene().getWindow()).close();
    }
    
    /**
     * Check if the owner was saved.
     * @return true if the owner was saved, false otherwise
     */
    public boolean isOwnerSaved() {
        return ownerSaved;
    }
}