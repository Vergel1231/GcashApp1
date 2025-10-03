package com.gcash.banking;

import com.gcash.db.DBConnection;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CashInTest {
    private static Connection conn;
    private CashIn cashIn;

    @BeforeAll
    static void beforeAll() throws Exception {
        conn = DBConnection.getConnection();
    }

    @AfterAll
    static void afterAll() throws Exception {
        if (conn != null) conn.close();
    }

    @BeforeEach
    void setUp() {
        cashIn = new CashIn(conn);
    }

    @Test
    void cashInIncreasesBalance() throws Exception {
        int userId = 1;
        double before = CheckBalance.checkBalance(userId);
        BigDecimal amount = new BigDecimal("50.0");

        boolean success = cashIn.cashIn(userId, amount, "1234567890");
        assertTrue(success, "Cash-in should succeed");

        double after = CheckBalance.checkBalance(userId);
        assertEquals(before + 50.0, after, 0.01,"Balance should increase by cash-in amount");
    }
}