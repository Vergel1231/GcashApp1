package com.gcash.banking;

import com.gcash.db.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;

public class CashInTest {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection(); // assumes you have a DBConnection class
            CashIn cashInService = new CashIn(conn);

            // First transaction: Cash in 200
            cashInService.cashIn(101, new BigDecimal("200.00"), "Virgilio");
            System.out.println("Cash in of 200 successful.");

            // Second transaction: Cash in 300
            cashInService.cashIn(101, new BigDecimal("300.00"), "Virgilio");
            System.out.println("Cash in of 300 successful.");

        } catch (Exception e) {
            System.err.println("Error during cashIn test:");
            e.printStackTrace();
        }
    }
}
