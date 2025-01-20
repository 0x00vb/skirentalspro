package com.Controller.Services;

import com.dao.ProductSQL;
import com.dao.RentalsSQL;
import com.model.Product;
import com.model.Rental;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductsService {
    private static ObservableList<Product> products = FXCollections.observableArrayList();

    // Method to load rentals (from database or other source)
    public static void loadProducts() {
        products.setAll(ProductSQL.loadProducts());
    }

    // Getter for the rentals list
    public static ObservableList<Product> getProducts() {
        return products;
    }

    public static Product getProductById(int id){
        for(Product p : products){
            if(p.getId() == id){
                return p;
            }
        }
        return null;
    }

    public static boolean addProduct(String name, String brand, String category, double costPrice, double sellPrice){
        int productAddedDb = ProductSQL.addProduct(name, brand, category, costPrice, sellPrice);
        if(productAddedDb != -1){
            Product p = new Product(productAddedDb, name, brand, category, costPrice, sellPrice, false);
            products.add(p);
            return true;
        }
        return false;
    }
}
