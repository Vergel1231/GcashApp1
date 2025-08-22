package com.gcash.banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/gcashdb";
        String user = "postgres";
        String password = "new_secure_password"; // update securely

        return DriverManager.getConnection(url, user, password);
    }
}
