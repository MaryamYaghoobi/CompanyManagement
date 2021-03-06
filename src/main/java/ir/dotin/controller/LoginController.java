package ir.dotin.controller;


import ir.dotin.entity.Employee;
import ir.dotin.service.ManagerService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/LoginController")

public class LoginController extends HttpServlet {
    // private static final long serialVersionUID = 1 L;
    private ManagerService managerService;

    public void init() {
        managerService = new ManagerService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            authenticate(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void authenticate(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Employee employee = managerService.login(username, password);
        HttpSession session = request.getSession();
        if (employee != null) {
            session.setAttribute("username", username);
            if (employee.getRole().getCode().equals("manager")) {
                response.sendRedirect("managerDashboard.jsp");
            } else {
                    response.sendRedirect("employeeDashboard.jsp");
            }

        } else {
            String message = "invalidationUserOrPassword";
            request.setAttribute(message, true);
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }
    }
}