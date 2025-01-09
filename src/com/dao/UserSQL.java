package com.dao;

import com.model.User;

import java.sql.*;
import java.util.ArrayList;

public class UserSQL {
    public static ArrayList<User> loadUsers()  {
        Connection conn = ConnectionSQL.getConnection();
        ArrayList<User> users = new ArrayList<>();
        try{
            PreparedStatement stmt = conn.prepareCall("SELECT * FROM users");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                users.add(
                        new User(
                                rs.getString("username"),
                                rs.getString("password")
                        )
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }
}
