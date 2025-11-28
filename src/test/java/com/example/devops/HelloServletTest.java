package com.example.devops;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.*;

class HelloServletTest {

    private HelloServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new HelloServlet();
    }

    @Test
    void testInit() {
        // Test that init sets the message
        servlet.init();

        // Use reflection to access private field for assertion
        try {
            var field = HelloServlet.class.getDeclaredField("message");
            field.setAccessible(true);
            String message = (String) field.get(servlet);
            assertEquals("Hello World!", message);  // ← ASSERTION
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    void testDoGet() throws IOException {
        servlet.init();

        // Mock HttpServletRequest and HttpServletResponse properly
        var request = mock(jakarta.servlet.http.HttpServletRequest.class);
        var response = mock(jakarta.servlet.http.HttpServletResponse.class);

        var writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        // Execute doGet
        servlet.doGet(request, response);

        // Verify the response contains expected content
        verify(response).setContentType("text/html");  // ← ASSERTION
        assertTrue(writer.toString().contains("Hello World!"));  // ← ASSERTION
    }
}