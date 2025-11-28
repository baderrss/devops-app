package com.example.devops;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HelloServletTest {

    @Test
    public void testBasicMath() {
        assertEquals(4, 2 + 2, "2+2 devrait être 4");
    }

    @Test
    public void testStringOperations() {
        String message = "DevOps";
        assertTrue(message.contains("Dev"));
        assertFalse(message.isEmpty());
    }
}