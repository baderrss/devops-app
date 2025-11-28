package com.example.devops;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HelloServletTest {  // ← RETIRER "public"

    @Test
    void testInit() {  // ← RETIRER "public"
        HelloServlet servlet = new HelloServlet();
        servlet.init();
        // Test implementation
    }

    @Test
    void testDoGet() {  // ← RETIRER "public"
        // Test implementation
    }
}