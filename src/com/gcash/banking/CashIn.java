package com.gcash.banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

public class CashIn {

    private final Connection connection;

    public CashIn(Connection connection) {
        this.connection = connection;
    }

    /**
     * Resolves the user ID from the given account number.
     *
     * @param accountNumber The account number to look up.
     * @return The user ID associated with the account number.
     * @throws SQLException if the account number is not found or a database error occurs.
     */
    public int resolveUserId(String accountNumber) throws SQLException {
        String query = "SELECT id FROM users WHERE account_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Account not found: " + accountNumber);
                }
            }
        }
    }

    /**
     * Adds the specified amount to the account balance and logs the transaction.
     *
     * @param accountId The account to credit.
     * @param amount    The amount to add.
     * @param name      The name of the user performing the cash-in.
     * @return true if successful, false otherwise.
     * @throws SQLException if a database error occurs.
     */
    public boolean cashIn(int accountId, BigDecimal amount, String name) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid amount. Must be greater than zero.");
            return false;
        }

        connection.setAutoCommit(false); // Begin transaction

        try {
            // Step 1: Update balance
            String updateSql = "UPDATE balance SET amount = amount + ? WHERE user_id = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                updateStmt.setBigDecimal(1, amount);
                updateStmt.setInt(2, accountId);
                int rowsAffected = updateStmt.executeUpdate();

                if (rowsAffected != 1) {
                    connection.rollback();
                    System.out.println("Cash-in failed. Account not found: " + accountId);
                    return false;
                }
            }

            // Step 2: Log transaction
            String insertSql = "INSERT INTO transaction (amount, name, account_id, transfertoid, transferfromid) VALUES (?, ?, ?, NULL, NULL)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                insertStmt.setBigDecimal(1, amount);
                insertStmt.setString(2, name);
                insertStmt.setInt(3, accountId);
                insertStmt.executeUpdate();
            }

            connection.commit();
            System.out.println("Cash-in successful and transaction logged for account: " + accountId);
            return true;

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true); // Restore default
        }
    }
}
