package src.com.gcash.banking;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;

public class CashTransferGUI extends JFrame {

    private final JTextField senderField = new JTextField(10);
    private final JTextField receiverField = new JTextField(10);
    private final JTextField amountField = new JTextField(10);
    private final JButton transferButton = new JButton("Transfer");
    private final JLabel statusLabel = new JLabel(" ");

    public CashTransferGUI(Connection connection) {
        super("Cash Transfer");

        CashTransfer cashTransfer = new CashTransfer(connection);

        transferButton.addActionListener(e -> {
            try {
                int senderId = Integer.parseInt(senderField.getText().trim());
                int receiverId = Integer.parseInt(receiverField.getText().trim());
                BigDecimal amount = new BigDecimal(amountField.getText().trim());

                if (amount.compareTo(BigDecimal.ONE) < 0) {
                    statusLabel.setText("Transfer amount must be at least ₱1.00.");
                    return;
                }

                String result = cashTransfer.transfer(senderId, receiverId, amount);
                statusLabel.setText(result);

            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid input. Please enter valid numbers.");
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Sender ID:")); formPanel.add(senderField);
        formPanel.add(new JLabel("Receiver ID:")); formPanel.add(receiverField);
        formPanel.add(new JLabel("Amount:")); formPanel.add(amountField);
        formPanel.add(transferButton); formPanel.add(statusLabel);

        add(formPanel);

        setSize(400, 200);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
