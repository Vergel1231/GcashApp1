package com.gcash.banking;

import com.gcash.db.DBConnection;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionsTest {
    private static Connection conn;
    private Transactions transactions;

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
        transactions = new Transactions(conn);
    }

    @Test
    void viewUserAllReturnsTransactions() {
        int userId = 1;
        List<String> txs = transactions.viewUserAll(userId);
        assertNotNull(txs, "Transactions list should not be null");
    }
}