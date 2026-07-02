/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.volunteer;
import dao.VolunteerDAO;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Asus
 */
public class VolunteerLoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet VolunteerLoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VolunteerLoginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        // 1. Force UTF-8 encoding standard
        request.setCharacterEncoding("UTF-8");
        
        // 2. Retrieve credentials submitted from v-login.html
        String email = request.getParameter("volunteerEmail");
        String password = request.getParameter("volunteerPassword");
        
        // Safety Fallback: Ensure basic input validation to prevent blank SQL lookups
        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            response.sendRedirect("public/v-login.html?error=missing_credentials");
            return;
        }
        
        // 3. Delegate lookup to your VolunteerDAO
        VolunteerDAO dao = new VolunteerDAO();
        volunteer v = dao.loginVolunteer(email.trim(), password);
        
        // 4. Handle Authentication Results
        if (v != null) {
            // Defend against session-fixation vulnerabilities
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate(); // Destroys previous session context
            }
            
            // Generate a secure new session structure
            HttpSession session = request.getSession(true);
            
            // Inject user details and access roles into session memory
            session.setAttribute("currentVolunteer", v);
            session.setAttribute("userRole", "volunteer");
            
            // Route the authenticated student to their private secured dashboard
            response.sendRedirect(request.getContextPath() + "/volunteer/v-dashboard.jsp");
        } else {
            // Unsuccessful authentication attempt: Redirect back with error token
            response.sendRedirect("public/v-login.html?error=invalid_credentials");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
