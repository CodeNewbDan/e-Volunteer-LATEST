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
import dao.OrganizationDAO;
import model.organization;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Asus
 */
public class DeleteOrgServlet extends HttpServlet {

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
            out.println("<title>Servlet DeleteOrgServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DeleteOrgServlet at " + request.getContextPath() + "</h1>");
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

        response.sendRedirect(request.getContextPath() + "/organization/org-profile.jsp");
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

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentOrg") == null) {
            response.sendRedirect(request.getContextPath() + "/public/org-login.html?error=session_expired");
            return;
        }

        organization org = (organization) session.getAttribute("currentOrg");
        String enteredPassword = request.getParameter("deletePassword");

        // 1. Authenticate coordinator's password authorization key
        if (enteredPassword == null || !enteredPassword.equals(org.getOrgPassword())) {
            response.sendRedirect(request.getContextPath() + "/organization/org-profile.jsp?status=delete_password_mismatch");

        }

        // 2. Perform Cascade Deletion in Apache Derby using DAO
        OrganizationDAO orgDAO = new OrganizationDAO();
        boolean deleted = orgDAO.deleteOrganization(org.getOrgId());

        if (deleted) {
            // 3. Purge operational session context and cookies
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/public/org-login.html?status=account_deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/organization/org-profile.jsp?status=delete_failed");
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
