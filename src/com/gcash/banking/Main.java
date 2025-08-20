package src.com.gcash.banking;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            CashTransferGUI transferGUI = new CashTransferGUI(conn);
            transferGUI.setVisible(true);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to launch CashTransferGUI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}