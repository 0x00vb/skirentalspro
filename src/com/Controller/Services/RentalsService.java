package com.Controller.Services;
import com.dao.RentalsSQL;
import com.model.Client;
import com.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.model.Rental;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RentalsService {
    private static ObservableList<Rental> rentalList = FXCollections.observableArrayList();

    // Method to load rentals (from database or other source)
    public static void loadRentals() {
        rentalList.setAll(RentalsSQL.loadRentals());
    }

    // Getter for the rentals list
    public static ObservableList<Rental> getRentals() {
        return rentalList;
    }

    public static int addRental(Client client, List<Product> products, Calendar startDate, Calendar endDate, double totalCost){
        int rentalAddedDb = RentalsSQL.addRental(client.getId(), startDate, endDate, products, totalCost);
        if(rentalAddedDb != -1){
            Rental rental = new Rental(rentalAddedDb, startDate, endDate, client, (ArrayList<Product>) products, totalCost);
            rentalList.add(rental);
            System.out.println("Added");
            return rental.getId();
        }
        return -1;
    }

    public static boolean removeRental(int id){
        if (RentalsSQL.deleteRental(id)) {
            rentalList.removeIf( r -> r.getId() == id );
            return true;
        }
        return false;
    }

    public static boolean removeProdFromRental(int rentalId, int prodId){
        if(RentalsSQL.removeProductFromRental(rentalId, prodId)){
            Rental r = rentalList.stream()
                    .filter(rental -> rental.getId() == rentalId)
                    .findFirst()
                    .orElse(null);

            if (r != null) {
                r.getProducts().removeIf(product -> product.getId() == prodId);
                return true;
            }
        }
        return false;
    }

}
