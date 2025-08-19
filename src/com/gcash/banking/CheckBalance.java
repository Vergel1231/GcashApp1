package com.gcash.banking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CheckBalance extends JFrame {
    private JTextField userIdField;
    private JButton checkButton;
    private JLabel resultLabel;

    public CheckBalance() {
        setTitle("Gcash Balance Checker");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        userIdField = new JTextField(10);
        checkButton = new JButton("Check Balance");
        resultLabel = new JLabel("Enter User ID and click 'Check Balance'");

        add(new JLabel("User ID:"));
        add(userIdField);
        add(checkButton);
        add(resultLabel);

        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int userId = Integer.parseInt(userIdField.getText());
                    double balance = checkBalance(userId);
                    if (balance >= 0) {
                        resultLabel.setText("User " + userId + " Balance: ₱" + balance);
                    } else {
                        resultLabel.setText("No balance found for User ID: " + userId);
                    }
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Invalid User ID format.");
                }
            }
        });
    }

    public static double checkBalance(int userId) {
        double balance = -1.0;

        String url = "jdbc:postgresql://localhost:5432/gcashdb";
        String user = "postgres";
        String password = "new_secure_password";

        String query = "SELECT amount FROM balance WHERE user_id = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                balance = rs.getDouble("amount");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return balance;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CheckBalance().setVisible(true);
        });
    }
}
