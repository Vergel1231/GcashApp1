package com.gcash.auth;

import com.gcash.db.DBConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserAuthenticationTest {
    private static Connection conn;

    @BeforeAll
    static void beforeAll() throws Exception {
        conn = DBConnection.getConnection();
    }

    @AfterAll
    static void afterAll() throws Exception {
        if (conn != null) conn.close();
    }

    @Test
    void validLoginReturnsPositiveId() {
        int id = UserAuthentication.loginUser(conn, "1234567890", "4321");
        assertTrue(id > 0, "Expected a positive user id for valid credentials");
    }

    @Test
    void invalidLoginReturnsMinusOne() {
        int id = UserAuthentication.loginUser(conn, "1234567890", "wrongpin");
        assertEquals(-1, id, "Invalid credentials should return -1");
    }
}