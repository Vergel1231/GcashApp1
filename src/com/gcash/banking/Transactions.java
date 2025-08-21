package src.com.gcash.banking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class Transactions {

    private final Connection connection;

    public Transactions(Connection connection) {
        this.connection = connection;
    }

    // Step 1: View all transactions
    public List<String> viewAll() {
        List<String> transactionList = new ArrayList<>();
        String query = "SELECT * FROM transaction ORDER BY date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                transactionList.add(formatTransaction(rs));
            }

        } catch (SQLException e) {
            transactionList.add("Error retrieving transactions: " + e.getMessage());
        }

        return transactionList;
    }

    // Step 3: View all transactions for a specific user
    public List<String> viewUserAll(int userId) {
        List<String> transactionList = new ArrayList<>();
        String query = "SELECT * FROM transaction WHERE account_id = ? OR transferfromid = ? OR transfertoid = ? ORDER BY date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transactionList.add(formatTransaction(rs));
            }

        } catch (SQLException e) {
            transactionList.add("Error retrieving user transactions: " + e.getMessage());
        }

        return transactionList;
    }

    // Step 4: View a specific transaction by ID
    public String viewTransaction(int transactionId) {
        String query = "SELECT * FROM transaction WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return formatTransaction(rs);
            } else {
                return "Transaction ID " + transactionId + " not found.";
            }

        } catch (SQLException e) {
            return "Error retrieving transaction: " + e.getMessage();
        }
    }

    // Helper method to format transaction output
    private String formatTransaction(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        BigDecimal amount = rs.getBigDecimal("amount");
        String name = rs.getString("name");
        int accountId = rs.getInt("account_id");
        Timestamp date = rs.getTimestamp("date");
        int transferTo = rs.getInt("transfertoid");
        int transferFrom = rs.getInt("transferfromid");

        return String.format(
                "ID: %d | Amount: ₱%.2f | Name: %s | Account ID: %d | Date: %s | From: %d | To: %d",
                id, amount, name, accountId, date.toLocalDateTime(), transferFrom, transferTo
        );
    }
}
