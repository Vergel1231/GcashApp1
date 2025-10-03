package com.gcash.banking;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CheckBalanceTest {

    @Test
    void checkBalanceReturnsCorrectValue() {
        int userId = 1;
        double balance = CheckBalance.checkBalance(userId);
        assertTrue(balance >= 0, "Balance should be zero or positive");
    }
}