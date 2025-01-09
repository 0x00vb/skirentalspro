package com.dao;

import com.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ProductSQL {
    public ArrayList<Product> loadProducts(){
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
                                rs.getDouble("sell_price")
                        )
                );
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return products;
    }

    public boolean addProduct(Product product) {
        try (Connection conn = ConnectionSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO products (id, name, brand, category, costPrice, sellPrice) VALUES (?, ?, ?, ?, ?, ?)")) {

            stmt.setInt(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getBrand());
            stmt.setString(4, product.getCategory());
            stmt.setDouble(5, product.getCostPrice());
            stmt.setDouble(6, product.getSellPrice());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Product getProductById(int id) {
        try (Connection conn = ConnectionSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products WHERE id = ?")) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getString("category"),
                        rs.getDouble("costPrice"),
                        rs.getDouble("sellPrice")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
