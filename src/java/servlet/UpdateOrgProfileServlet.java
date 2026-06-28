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
import model.organization;
import dao.OrganizationDAO;

/**
 *
 * @author Asus
 */
public class UpdateOrgProfileServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateOrgProfileServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateOrgProfileServlet at " + request.getContextPath() + "</h1>");
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

        // 2. Validate session and retrieve authenticated organization context
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentOrg") == null) {
            response.sendRedirect("org-login.html?error=session_expired");
            return;
        }

        organization currentOrg = (organization) session.getAttribute("currentOrg");

        try {
            // 3. Extract form parameters
            String orgName = request.getParameter("orgName");
            String orgEmail = request.getParameter("orgEmail");

            // contactPerson is defined as 'int' in your organization.java model
            int contactPerson = Integer.parseInt(request.getParameter("contactPerson"));

            String orgAddress = request.getParameter("orgAddress");
            String orgType = request.getParameter("orgType");
            String orgPassword = request.getParameter("password"); // Maps the form password field

            // 4. Construct updated organization entity
            organization updated = new organization();

            // SECURITY check: Maintain read-only keys from authenticated session
            updated.setOrgId(currentOrg.getOrgId());                  // Retains immutable primary key
            updated.setRegistrationNum(currentOrg.getRegistrationNum()); // Retains immutable registration number

            // Apply new profile modifications
            updated.setOrgName(orgName);
            updated.setOrgEmail(orgEmail);
            updated.setContactPerson(contactPerson);
            updated.setOrgAddress(orgAddress);
            updated.setOrgType(orgType);
            updated.setOrgPassword(orgPassword);

            // 5. Delegate profile persistence to your OrganizationDAO
            OrganizationDAO dao = new OrganizationDAO();
            boolean success = dao.updateOrgProfile(updated);

            if (success) {
                // Instantly sync the active session to display updated credentials in JSP pages
                session.setAttribute("currentOrg", updated);
                response.sendRedirect("org-profile.jsp?status=success");
            } else {
                response.sendRedirect("org-profile.jsp?error=database_error");
            }

        } catch (NumberFormatException e) {
            // Catches invalid numeric values passed into contactPerson
            e.printStackTrace();
            response.sendRedirect("org-profile.jsp?error=invalid_contact_person_format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("org-profile.jsp?error=system_error");
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
