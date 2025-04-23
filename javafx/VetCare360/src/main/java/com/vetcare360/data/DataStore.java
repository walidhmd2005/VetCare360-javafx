package com.vetcare360.data;

import com.vetcare360.model.Owner;
import com.vetcare360.model.Pet;
import com.vetcare360.model.Vet;
import com.vetcare360.model.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * In-memory data store for the VetCare360 application.
 * This class manages all the data for the application.
 * In a real application, this would be replaced with a database.
 */
public class DataStore {
    private static DataStore instance;
    
    private final Map<Integer, Owner> owners;
    private final Map<Integer, Pet> pets;
    private final Map<Integer, Vet> vets;
    private final Map<Integer, Visit> visits;
    
    private int nextOwnerId = 1;
    private int nextPetId = 1;
    private int nextVetId = 1;
    private int nextVisitId = 1;
    
    /**
     * Private constructor to enforce singleton pattern
     */
    private DataStore() {
        owners = new HashMap<>();
        pets = new HashMap<>();
        vets = new HashMap<>();
        visits = new HashMap<>();
        
        // Initialize with some sample data
        initializeSampleData();
    }
    
    /**
     * Get the singleton instance of DataStore
     * @return the DataStore instance
     */
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }
    
    /**
     * Initialize the data store with sample data
     */
    private void initializeSampleData() {
        // Create sample owners
        Owner owner1 = new Owner(nextOwnerId++, "John", "Doe", "123 Main St", "New York", "555-1234");
        Owner owner2 = new Owner(nextOwnerId++, "Jane", "Smith", "456 Oak Ave", "Los Angeles", "555-5678");
        Owner owner3 = new Owner(nextOwnerId++, "Robert", "Johnson", "789 Pine Rd", "Chicago", "555-9012");
        
        owners.put(owner1.getId(), owner1);
        owners.put(owner2.getId(), owner2);
        owners.put(owner3.getId(), owner3);
        
        // Create sample vets
        Vet vet1 = new Vet(nextVetId++, "Alice", "Williams", "Surgery");
        Vet vet2 = new Vet(nextVetId++, "Bob", "Brown", "Dentistry");
        Vet vet3 = new Vet(nextVetId++, "Carol", "Davis", "Cardiology");
        
        vets.put(vet1.getId(), vet1);
        vets.put(vet2.getId(), vet2);
        vets.put(vet3.getId(), vet3);
        
        // Create sample pets
        Pet pet1 = new Pet(nextPetId++, "Max", LocalDate.of(2018, 5, 15), "Dog", owner1);
        Pet pet2 = new Pet(nextPetId++, "Bella", LocalDate.of(2019, 3, 10), "Cat", owner1);
        Pet pet3 = new Pet(nextPetId++, "Charlie", LocalDate.of(2020, 7, 22), "Bird", owner2);
        Pet pet4 = new Pet(nextPetId++, "Luna", LocalDate.of(2017, 11, 5), "Dog", owner3);
        
        pets.put(pet1.getId(), pet1);
        pets.put(pet2.getId(), pet2);
        pets.put(pet3.getId(), pet3);
        pets.put(pet4.getId(), pet4);
        
        owner1.addPet(pet1);
        owner1.addPet(pet2);
        owner2.addPet(pet3);
        owner3.addPet(pet4);
        
        // Create sample visits
        Visit visit1 = new Visit(nextVisitId++, LocalDate.of(2023, 1, 15), "Annual checkup", pet1, vet1);
        Visit visit2 = new Visit(nextVisitId++, LocalDate.of(2023, 2, 20), "Vaccination", pet2, vet2);
        Visit visit3 = new Visit(nextVisitId++, LocalDate.of(2023, 3, 10), "Wing clipping", pet3, vet3);
        Visit visit4 = new Visit(nextVisitId++, LocalDate.of(2023, 4, 5), "Dental cleaning", pet4, vet2);
        
        visits.put(visit1.getId(), visit1);
        visits.put(visit2.getId(), visit2);
        visits.put(visit3.getId(), visit3);
        visits.put(visit4.getId(), visit4);
        
        pet1.addVisit(visit1);
        pet2.addVisit(visit2);
        pet3.addVisit(visit3);
        pet4.addVisit(visit4);
        
        vet1.addVisit(visit1);
        vet2.addVisit(visit2);
        vet3.addVisit(visit3);
        vet2.addVisit(visit4);
    }
    
    // Owner methods
    public List<Owner> getAllOwners() {
        return new ArrayList<>(owners.values());
    }
    
    public Owner getOwnerById(int id) {
        return owners.get(id);
    }
    
    public List<Owner> findOwnersByLastName(String lastName) {
        return owners.values().stream()
                .filter(owner -> owner.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public void saveOwner(Owner owner) {
        if (owner.getId() == 0) {
            owner.setId(nextOwnerId++);
        }
        owners.put(owner.getId(), owner);
    }
    
    public void deleteOwner(int id) {
        owners.remove(id);
    }
    
    // Pet methods
    public List<Pet> getAllPets() {
        return new ArrayList<>(pets.values());
    }
    
    public Pet getPetById(int id) {
        return pets.get(id);
    }
    
    public void savePet(Pet pet) {
        if (pet.getId() == 0) {
            pet.setId(nextPetId++);
        }
        pets.put(pet.getId(), pet);
    }
    
    public void deletePet(int id) {
        pets.remove(id);
    }
    
    // Vet methods
    public List<Vet> getAllVets() {
        return new ArrayList<>(vets.values());
    }
    
    public Vet getVetById(int id) {
        return vets.get(id);
    }
    
    public void saveVet(Vet vet) {
        if (vet.getId() == 0) {
            vet.setId(nextVetId++);
        }
        vets.put(vet.getId(), vet);
    }
    
    public void deleteVet(int id) {
        vets.remove(id);
    }
    
    // Visit methods
    public List<Visit> getAllVisits() {
        return new ArrayList<>(visits.values());
    }
    
    public Visit getVisitById(int id) {
        return visits.get(id);
    }
    
    public void saveVisit(Visit visit) {
        if (visit.getId() == 0) {
            visit.setId(nextVisitId++);
        }
        visits.put(visit.getId(), visit);
    }
    
    public void deleteVisit(int id) {
        visits.remove(id);
    }
}