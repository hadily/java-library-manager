package com.example.miniprojetjava.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // JDBC URL, username, and empty password for MariaDB server
    private static final String URL = "jdbc:mariadb://localhost:4000/projetJava";
    private static final String USER = "root";
    private static final String PASSWORD = "azerty";

    public static Connection StartConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void CloseConnection() throws SQLException {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.close();
    }

    // public static void main(String[] args) { }
}
