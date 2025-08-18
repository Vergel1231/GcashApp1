package com.gcash.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthentication {

    public static boolean registerUser(Connection conn, String name, String email, String accountNumber, String pin) {
        if (!isValidName(name) || !isValidEmail(email) || !isValidAccountNumber(accountNumber) || !isValidPin(pin)) {
            System.err.println("Validation failed: One or more fields are invalid.");
            return false;
        }

        String sql = "INSERT INTO users (name, email, account_number, pin) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, accountNumber);
            pstmt.setString(4, pin);
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted == 1;
        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    public static int loginUser(Connection conn, String accountNumber, String pin) {
        if (!isValidAccountNumber(accountNumber) || !isValidPin(pin)) {
            System.err.println("Login failed: Invalid account number or PIN format.");
            return -1;
        }

        String sql = "SELECT id FROM users WHERE account_number = ? AND pin = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, pin);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                System.err.println("Login failed: Incorrect credentials or user not found.");
                return -1;
            }
        } catch (SQLException e) {
            System.err.println("Login error: Please try again later.");
            return -1;
        }
    }

    public static boolean changePin(Connection conn, int userId, String currentPin, String newPin) {
        if (!isValidPin(currentPin) || !isValidPin(newPin)) {
            System.err.println("PIN change failed: Invalid PIN format.");
            return false;
        }

        String verifySql = "SELECT id FROM users WHERE id = ? AND pin = ?";
        try (PreparedStatement verifyStmt = conn.prepareStatement(verifySql)) {
            verifyStmt.setInt(1, userId);
            verifyStmt.setString(2, currentPin);
            ResultSet rs = verifyStmt.executeQuery();
            if (!rs.next()) {
                System.err.println("PIN change failed: Current PIN is incorrect.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("PIN verification error: " + e.getMessage());
            return false;
        }

        String updateSql = "UPDATE users SET pin = ? WHERE id = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setString(1, newPin);
            updateStmt.setInt(2, userId);
            int rowsUpdated = updateStmt.executeUpdate();
            return rowsUpdated == 1;
        } catch (SQLException e) {
            System.err.println("PIN update error: " + e.getMessage());
            return false;
        }
    }

    public static void logoutUser(int userId) {
        // Placeholder for session cleanup
        System.out.println("User ID " + userId + " has been logged out.");
    }

    private static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 100;
    }

    private static boolean isValidEmail(String email) {
        return email != null && email.length() <= 100 &&
                email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private static boolean isValidAccountNumber(String acc) {
        return acc != null && acc.matches("^\\d{1,20}$");
    }

    private static boolean isValidPin(String pin) {
        return pin != null && pin.matches("^\\d{4,10}$");
    }
}
