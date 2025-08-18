import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AuthTestGUI extends JFrame {

    private JTextField accountField;
    private JPasswordField pinField;
    private JButton loginButton;
    private JLabel resultLabel;

    private Connection conn;

    public AuthTestGUI() {
        setTitle("Bank Login Tester");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        accountField = new JTextField();
        pinField = new JPasswordField();
        loginButton = new JButton("Login");
        resultLabel = new JLabel(" ");

        add(new JLabel("Account Number:"));
        add(accountField);
        add(new JLabel("PIN:"));
        add(pinField);
        add(new JLabel(""));
        add(loginButton);
        add(new JLabel("Result:"));
        add(resultLabel);

        loginButton.addActionListener(e -> handleLogin());

        // Print classpath for auditability
        System.out.println("Classpath: " + System.getProperty("java.class.path"));

        // 🔍 Confirm JDBC driver is loaded
        try {
            Driver driver = DriverManager.getDriver("jdbc:postgresql://127.0.0.1:5432/gcashdb");
            System.out.println("JDBC Driver loaded: " + driver.getClass().getName());
        } catch (SQLException e) {
            System.out.println("No suitable JDBC driver: " + e.getMessage());
        }

        // 🔗 Attempt DB connection
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/gcashdb", "postgres", "new_secure_password");
            System.out.println("DB connection established.");
        } catch (SQLException ex) {
            resultLabel.setText("DB connection failed: " + ex.getMessage());
            loginButton.setEnabled(false);
            ex.printStackTrace(); // Full trace for CLI debugging
        }
    }

    private void handleLogin() {
        String account = accountField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();

        int userId = com.gcash.auth.UserAuthentication.loginUser(conn, account, pin);

        if (userId > 0) {
            resultLabel.setText("Login successful. ID: " + userId);
            // Optional: trigger changePin or logout here
        } else {
            resultLabel.setText("Login failed.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthTestGUI().setVisible(true));
    }
}
