package com.gcash.banking;

import com.gcash.db.DBConnection;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CashTransferTest {
    private static Connection conn;
    private CashTransfer transfer;

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
        transfer = new CashTransfer(conn);
    }

    @Test
    void transferMovesMoneyBetweenAccounts() throws Exception {
        int senderId = 1;   // replace with actual sender user ID
        int receiverId = 4; // replace with actual receiver user ID

        double senderBefore = CheckBalance.checkBalance(senderId);
        double receiverBefore = CheckBalance.checkBalance(receiverId);

        transfer.transfer(senderId, receiverId, new BigDecimal("50.0"));

        double senderAfter = CheckBalance.checkBalance(senderId);
        double receiverAfter = CheckBalance.checkBalance(receiverId);

        assertTrue(senderAfter <= senderBefore - 50.0, "Sender balance should decrease");
        assertTrue(receiverAfter >= receiverBefore + 50.0, "Receiver balance should increase");
    }
}