package com.gcash.banking;

import com.gcash.auth.UserAuthentication;
import com.gcash.db.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Welcome to GcashApp CLI");

            // Login
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            int userId = UserAuthentication.loginUser(conn, accountNumber, pin);
            if (userId == -1) {
                System.out.println("Login failed. Exiting...");
                return;
            }

            CashIn cashIn = new CashIn(conn);
            CashTransfer transfer = new CashTransfer(conn);
            Transactions transactions = new Transactions(conn);

            boolean continueSession = true;
            while (continueSession) {
                System.out.println("\nSelect an option:");
                System.out.println("1. Check Balance");
                System.out.println("2. Cash In");
                System.out.println("3. Transfer");
                System.out.println("4. View Transactions");
                System.out.println("5. Logout");

                System.out.print("Choice: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        double balance = CheckBalance.checkBalance(userId);
                        System.out.println("Your balance: ₱" + balance);
                        break;

                    case "2":
                        System.out.print("Enter amount to cash in: ₱");
                        BigDecimal cashInAmount = new BigDecimal(scanner.nextLine());
                        boolean cashInSuccess = cashIn.cashIn(userId, cashInAmount, accountNumber);
                        System.out.println(cashInSuccess ? "Cash-in successful." : "Cash-in failed.");
                        break;

                    case "3":
                        System.out.print("Enter recipient account number: ");
                        String recipientAcc = scanner.nextLine();
                        int recipientId = cashIn.resolveUserId(recipientAcc);
                        System.out.print("Enter amount to transfer: ₱");
                        BigDecimal transferAmount = new BigDecimal(scanner.nextLine());
                        String transferResult = transfer.transfer(userId, recipientId, transferAmount);
                        System.out.println(transferResult);
                        break;

                    case "4":
                        List<String> userTxns = transactions.viewUserAll(userId);
                        System.out.println("Your transactions:");
                        for (String txn : userTxns) {
                            System.out.println(txn);
                        }
                        break;

                    case "5":
                        UserAuthentication.logoutUser(userId);
                        continueSession = false;
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }

                if (continueSession) {
                    System.out.print("\nDo you want to perform another transaction? (yes/no): ");
                    String again = scanner.nextLine();
                    continueSession = again.equalsIgnoreCase("yes");
                }
            }

            System.out.println("Thank you for using GcashApp CLI!");

        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }
}
