package com.example.devops;

import java.io.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    @Override  // ← AJOUTER
    public void init() {
        message = "Hello World!";
    }

    @Override  // ← AJOUTER
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    @Override  // ← AJOUTER
    public void destroy() {
        // ← AJOUTER UN COMMENTAIRE EXPLICATIF
        // Cette méthode est vide car aucune ressource à libérer
        // La méthode est override pour documentation
    }
}