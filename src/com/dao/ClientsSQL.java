package com.dao;

import com.Controller.Services.RentalsService;
import com.model.Client;
import com.model.Rental;

import java.sql.*;
import java.util.ArrayList;

public class ClientsSQL {
    public static ArrayList<Client> loadClients(){
        ArrayList<Client> clients = new ArrayList<>();
        try{
            Connection conn = ConnectionSQL.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM clients");
            while(rs.next()){
                int clientId = rs.getInt("id");

                Client client = new Client(
                    clientId,
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phoneNumber"),
                    rs.getString("address")
                );
                clients.add(client);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return clients;
    }

    public static int addClient(String firstName, String lastName, String phoneNumber, String address){
        String sql = "INSERT INTO clients (first_name, last_name, phoneNumber, address) VALUES (?,?,?,?)";
        try{
            Connection conn = ConnectionSQL.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1,firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, address);

            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated client ID
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean removeClient(int id){
        String sql = "DELETE FROM clients WHERE id = ?";
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
