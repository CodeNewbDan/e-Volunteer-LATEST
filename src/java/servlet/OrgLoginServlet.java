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
import jakarta.servlet.http.HttpSession;
import dao.OrganizationDAO;
import model.organization;

/**
 *
 * @author Asus
 */
public class OrgLoginServlet extends HttpServlet {

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
            out.println("<title>Servlet OrgLoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrgLoginServlet at " + request.getContextPath() + "</h1>");
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

        // 2. Retrieve credentials submitted from org-login.html
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Safety Fallback: Ensure basic input validation to prevent blank database queries
        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            response.sendRedirect("org-login.html?error=missing_credentials");
            return;
        }

        // 3. Delegate authentication logic to your updated OrganizationDAO
        OrganizationDAO dao = new OrganizationDAO();
        organization org = dao.loginOrg(email.trim(), password);

        // 4. Evaluate Authentication Results
        if (org != null) {
            // Defend against session-fixation vulnerabilities
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate(); // Destroys previous session context
            }

            // Establish a secure, fresh session structure
            HttpSession session = request.getSession(true);

            // Bind organization object and authority role to the session
            session.setAttribute("currentOrg", org);
            session.setAttribute("userRole", "organization");

            // Route the organizer straight to their secured management space
            response.sendRedirect("org-dashboard.jsp");
        } else {
            // Unsuccessful authentication: Redirect back with warning flag
            response.sendRedirect("org-login.html?error=invalid_credentials");
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
