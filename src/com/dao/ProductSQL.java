package com.dao;

import com.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductSQL {
    public static  ArrayList<Product> loadProducts(){
        ArrayList<Product> products = new ArrayList<>();

        try{
            Connection conn = ConnectionSQL.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products");
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                products.add(
                        new Product(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("brand"),
                                rs.getString("category"),
                                rs.getDouble("cost_price"),
                                rs.getDouble("sell_price"),
                                rs.getBoolean("rented")
                        )
                );
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return products;
    }

    public static int addProduct(String name, String brand, String category, double costPrice, double sellPrice) {
        try {
            Connection conn = ConnectionSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO products (name, brand, category, cost_price, sell_price) VALUES (?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, name);
            stmt.setString(2, brand);
            stmt.setString(3, category);
            stmt.setDouble(4, costPrice);
            stmt.setDouble(5, sellPrice);

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated product ID
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        Connection conn = ConnectionSQL.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
