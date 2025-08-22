package com.gcash.banking;

import com.gcash.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class TransactionViewerGUI extends JFrame {

    private JTextField userIdField;
    private JTextField transactionIdField;
    private JButton viewUserButton;
    private JButton viewOneButton;
    private JTextArea resultArea;

    private Transactions transactions;

    public TransactionViewerGUI(Connection connection) {
        super("Transaction Viewer");
        this.transactions = new Transactions(connection);

        userIdField = new JTextField(10);
        transactionIdField = new JTextField(10);
        viewUserButton = new JButton("View User Transactions");
        viewOneButton = new JButton("View Transaction by ID");
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(new JLabel("User ID:"));
        inputPanel.add(userIdField);
        inputPanel.add(new JLabel("Transaction ID:"));
        inputPanel.add(transactionIdField);
        inputPanel.add(viewUserButton);
        inputPanel.add(viewOneButton);

        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        viewUserButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText().trim());
                List<String> transactionsList = transactions.viewUserAll(userId);
                resultArea.setText(String.join("\n", transactionsList));
            } catch (Exception ex) {
                resultArea.setText("Error: " + ex.getMessage());
            }
        });

        viewOneButton.addActionListener(e -> {
            try {
                int transactionId = Integer.parseInt(transactionIdField.getText().trim());
                String transaction = transactions.viewTransaction(transactionId);
                resultArea.setText(transaction);
            } catch (Exception ex) {
                resultArea.setText("Error: " + ex.getMessage());
            }
        });

        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            SwingUtilities.invokeLater(() -> new TransactionViewerGUI(conn));
        } catch (Exception e) {
            System.err.println("Failed to launch TransactionViewerGUI: " + e.getMessage());
        }
    }
}
