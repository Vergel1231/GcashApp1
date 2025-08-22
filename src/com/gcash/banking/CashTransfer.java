package com.gcash.banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

public class CashTransfer {

    private final Connection connection;
    private static final BigDecimal MIN_TRANSFER = new BigDecimal("1.00");
    private static final BigDecimal MAX_TRANSFER = new BigDecimal("100000.00");

    public CashTransfer(Connection connection) {
        this.connection = connection;
    }

    public String transfer(int senderId, int receiverId, BigDecimal amount) throws SQLException {
        if (senderId == receiverId) {
            return "You cannot transfer funds to your own account.";
        }

        if (amount.compareTo(MIN_TRANSFER) < 0) {
            return "Transfer amount must be at least ₱1.00.";
        }

        if (amount.compareTo(MAX_TRANSFER) > 0) {
            return "Transfer exceeds maximum allowed per transaction.";
        }

        if (!userExists(receiverId)) {
            return "Receiver account not found.";
        }

        if (!hasSufficientBalance(senderId, amount)) {
            return "Insufficient balance.";
        }

        String debitSql = "UPDATE balance SET amount = amount - ? WHERE user_id = ?";
        String creditSql = "UPDATE balance SET amount = amount + ? WHERE user_id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement debitStmt = connection.prepareStatement(debitSql)) {
                debitStmt.setBigDecimal(1, amount);
                debitStmt.setInt(2, senderId);
                debitStmt.executeUpdate();
            }

            try (PreparedStatement creditStmt = connection.prepareStatement(creditSql)) {
                creditStmt.setBigDecimal(1, amount);
                creditStmt.setInt(2, receiverId);
                creditStmt.executeUpdate();
            }

            connection.commit();
            return "Transfer successful.";

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private boolean userExists(int userId) throws SQLException {
        String sql = "SELECT 1 FROM balance WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean hasSufficientBalance(int userId, BigDecimal amount) throws SQLException {
        String sql = "SELECT amount FROM balance WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal currentBalance = rs.getBigDecimal("amount");
                    return currentBalance.compareTo(amount) >= 0;
                }
                return false;
            }
        }
    }
}
