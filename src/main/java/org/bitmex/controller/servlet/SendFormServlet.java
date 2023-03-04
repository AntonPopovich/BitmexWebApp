package org.bitmex.controller.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebServlet("/sendFormServlet")
public class SendFormServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String apiKey = request.getParameter("akey");
        String apiSecret = request.getParameter("asecret");
        Double step = Double.valueOf(request.getParameter("stp"));
        Short level = Short.valueOf(request.getParameter("lvl"));
        Double coefficient = Double.valueOf(request.getParameter("coef"));

        System.out.println(apiKey);
        System.out.println(apiSecret);
        System.out.println(step);
        System.out.println(level);
        System.out.println(coefficient);
    }
}
