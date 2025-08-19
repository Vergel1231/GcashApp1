package src.com.gcash.banking;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class CashInTestGUI extends JFrame {

    private JTextField accountField;
    private JTextField amountField;
    private JTextField nameField;
    private JButton cashInButton;
    private JLabel resultLabel;

    private Connection conn;

    public CashInTestGUI() {
        setTitle("CashIn Tester");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        accountField = new JTextField();
        amountField = new JTextField();
        nameField = new JTextField();
        cashInButton = new JButton("Cash In");
        resultLabel = new JLabel(" ");

        add(new JLabel("Account ID:"));
        add(accountField);
        add(new JLabel("Amount:"));
        add(amountField);
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel(""));
        add(cashInButton);
        add(new JLabel("Result:"));
        add(resultLabel);

        cashInButton.addActionListener(e -> handleCashIn());

        try {
            conn = DBConnection.getConnection();
            System.out.println("DB connection established.");
        } catch (SQLException ex) {
            resultLabel.setText("DB connection failed.");
            cashInButton.setEnabled(false);
            ex.printStackTrace();
        }
    }

    private void handleCashIn() {
        try {
            String accountNumber = accountField.getText().trim();
            BigDecimal amount = new BigDecimal(amountField.getText().trim());
            String name = nameField.getText().trim();

            CashIn cashInService = new CashIn(conn);
            System.out.println("Resolving account number: " + accountNumber + "]");
            int accountId = cashInService.resolveUserId(accountNumber);

            boolean success = cashInService.cashIn(accountId, amount, name);

            if (success) {
                resultLabel.setText("Cash-in successful.");
            } else {
                resultLabel.setText("Cash-in failed.");
            }

        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid amount format.");
        } catch (SQLException ex) {
            resultLabel.setText("Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CashInTestGUI().setVisible(true));
    }
}
