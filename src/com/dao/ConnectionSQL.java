package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSQL {

    // Static reference to the single instance of the connection
    private static Connection connection = null;
    private static final String URL = "jdbc:mysql://localhost:3306/javaProject";
    private static final String USER = "root";
    private static final String PASSWORD = "Valentino";

    // Private constructor to prevent instantiation
    private ConnectionSQL() {}

    // Public method to get the connection
    public static Connection getConnection() {
        if (connection == null) {
            synchronized (ConnectionSQL.class) {
                // Double check for thread safety
                if (connection == null) {
                    try {
                        // Load MySQL JDBC Driver (optional in newer versions of JDBC)
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        // Create connection to database
                        connection = DriverManager.getConnection(URL, USER, PASSWORD);
                        System.out.println("[+] DB connected!");
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                        System.out.println("[!] DB connection failed!");
                    }
                }
            }
        }
        return connection;
    }
}
