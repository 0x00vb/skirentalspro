package com.dao;

import com.Controller.Services.ClientsService;
import com.Controller.Services.ProductsService;
import com.model.Client;
import com.model.Product;
import com.model.Rental;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class RentalsSQL {
    public static ArrayList<Rental> loadRentals() {
        ArrayList<Rental> rentals = new ArrayList<>();
        HashMap<Integer, Rental> rentalMap = new HashMap<>();

        try {
            Connection conn = ConnectionSQL.getConnection();
            String query = "SELECT rental.id, rental.client_id, " +
                    "products.id AS product_id, products.sell_price AS productPrice, " +
                    "rental.date_start, rental.date_end " +
                    "FROM rental " +
                    "JOIN clients ON rental.client_id = clients.id " +
                    "JOIN rental_product ON rental.id = rental_product.rental_id " +
                    "JOIN products ON rental_product.product_id = products.id";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int rentalId = rs.getInt("id");
                int clientId = rs.getInt("client_id");
                int prodId = rs.getInt("product_id");

                Client client = ClientsService.getClientById(clientId);
                Product product = ProductsService.getProductById(prodId);

                // Check if the rental already exists
                Rental rental = rentalMap.get(rentalId);

                    if (rental == null) {
                        // Convert SQL Timestamps to Calendar
                        Calendar dateStart = Calendar.getInstance();
                        dateStart.setTimeInMillis(rs.getTimestamp("date_start").getTime());

                        Calendar dateEnd = Calendar.getInstance();
                        dateEnd.setTimeInMillis(rs.getTimestamp("date_end").getTime());

                        // Create a new rental object
                        rental = new Rental(rentalId, dateStart, dateEnd, client, new ArrayList<>(), 0.0);
                        rentals.add(rental);
                        rentalMap.put(rentalId, rental);
                    }

                // Add the product to the existing rental
                if (product != null) {
                    rental.getProducts().add(product);
                }

                // Update the total cost
                rental.setTotalCost(rental.getTotalCost() + rs.getDouble("productPrice"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rentals;
    }

    public static int addRental(int cId, Calendar startDate, Calendar endDate, List<Product> products, double totalCost){
        Connection conn = null;
        try{
            String insertRentalQuery = "INSERT INTO rental (date_start, date_end, client_id, totalCost) VALUES (?, ?, ?, ?)";
            String insertRentalProductQuery = "INSERT INTO rental_product (rental_id, product_id) VALUES (?, ?)";

            conn = ConnectionSQL.getConnection();
            conn.setAutoCommit(false); // init transaction

            PreparedStatement rentalStatement = null;
            PreparedStatement rentalProductStatement = null;

            rentalStatement = conn.prepareStatement(insertRentalQuery, Statement.RETURN_GENERATED_KEYS);
            rentalStatement.setDate(1, new java.sql.Date(startDate.getTimeInMillis()));
            rentalStatement.setDate(2, new java.sql.Date(endDate.getTimeInMillis()));
            rentalStatement.setInt(3, cId);
            rentalStatement.setDouble(4, totalCost);
            rentalStatement.executeUpdate();

            ResultSet generatedKeys = rentalStatement.getGeneratedKeys(); // get new rental ID
            if (!generatedKeys.next()) {
                return -1;
            }
            int rentalId = generatedKeys.getInt(1);

            rentalProductStatement = conn.prepareStatement(insertRentalProductQuery);
            for (Product product : products) {
                rentalProductStatement.setInt(1, rentalId);
                rentalProductStatement.setInt(2, product.getId());
                rentalProductStatement.addBatch(); // batch for optimization
            }
            rentalProductStatement.executeBatch(); // exec batch
            conn.commit();
            conn.setAutoCommit(true);
            return rentalId;
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean deleteRental(int rentalId) {
        Connection conn = null;
        try {
            String deleteRentalProductQuery = "DELETE FROM rental_product WHERE rental_id = ?";
            String deleteRentalQuery = "DELETE FROM rental WHERE id = ?";

            conn = ConnectionSQL.getConnection();
            conn.setAutoCommit(false); // init transaction

            PreparedStatement rentalProductStatement = null;
            PreparedStatement rentalStatement = null;

            // First delete from rental_product (child table)
            rentalProductStatement = conn.prepareStatement(deleteRentalProductQuery);
            rentalProductStatement.setInt(1, rentalId);
            rentalProductStatement.executeUpdate();

            // Then delete from rental (parent table)
            rentalStatement = conn.prepareStatement(deleteRentalQuery);
            rentalStatement.setInt(1, rentalId);
            int rowsAffected = rentalStatement.executeUpdate();

            conn.commit();
            conn.setAutoCommit(true);

            return rowsAffected > 0;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    conn.setAutoCommit(true);
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeProductFromRental(int rentalId, int productId) {
        Connection conn = null;
        try {
            conn = ConnectionSQL.getConnection();
            conn.setAutoCommit(false); // init transaction

            // First update the total cost in the rental table
            String updateRentalCostQuery =
                    "UPDATE rental " +
                            "SET totalCost = totalCost - (SELECT sell_price FROM products WHERE id = ?) " +
                            "WHERE id = ?";

            PreparedStatement updateCostStatement = conn.prepareStatement(updateRentalCostQuery);
            updateCostStatement.setInt(1, productId);
            updateCostStatement.setInt(2, rentalId);
            updateCostStatement.executeUpdate();

            // Then remove the product from rental_product
            String deleteProductQuery =
                    "DELETE FROM rental_product " +
                            "WHERE rental_id = ? AND product_id = ?";

            PreparedStatement deleteProductStatement = conn.prepareStatement(deleteProductQuery);
            deleteProductStatement.setInt(1, rentalId);
            deleteProductStatement.setInt(2, productId);
            int rowsAffected = deleteProductStatement.executeUpdate();

            conn.commit();
            conn.setAutoCommit(true);

            return rowsAffected > 0;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    conn.setAutoCommit(true);
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        }
    }
}
