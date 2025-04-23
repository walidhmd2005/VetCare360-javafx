package com.vetcare360.controller;

import com.vetcare360.model.Owner;
import com.vetcare360.model.Pet;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class PetControllerTest {

    private PetController petController;
    private Stage stage;
    private TextField nameField;
    private boolean stageClosed = false;

    @Start
    public void start(Stage stage) {
        this.stage = stage;
        this.nameField = new TextField();
        Scene scene = new Scene(nameField, 100, 100);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        petController = new PetController();
        // Use reflection to set the nameField
        try {
            java.lang.reflect.Field field = PetController.class.getDeclaredField("nameField");
            field.setAccessible(true);
            field.set(petController, nameField);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Mock the stage closing behavior
        stage.setOnCloseRequest(event -> stageClosed = true);
    }

    @Test
    public void testCancelButton() {
        // Create a mock ActionEvent
        ActionEvent event = mock(ActionEvent.class);
        
        // Call the cancelButton method
        petController.cancelButton(event);
        
        // Verify that the stage was closed
        assertTrue(stageClosed, "The stage should be closed after calling cancelButton");
    }
}