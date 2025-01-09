package com.model;
import java.util.ArrayList;

public class Client {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private ArrayList<Rental> rentals;

    public Client(int id, String firstName, String lastName, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rentals = new ArrayList<>(); // Initialize rentals list
        this.phoneNumber = phoneNumber;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public ArrayList<Rental> getRentals() {
        return rentals;
    }
    public void setRentals(ArrayList<Rental> rentals) {
        this.rentals = rentals;
    }
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void addRental(Rental rental) {
        this.rentals.add(rental);
    }
    public void removeRental(Rental rental) {
        this.rentals.remove(rental);
    }
}
