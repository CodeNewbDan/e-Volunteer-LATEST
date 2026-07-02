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
import model.organization;
import dao.OrganizationDAO;

/**
 *
 * @author Asus
 */
public class RegisterOrgServlet extends HttpServlet {

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
            out.println("<title>Servlet RegisterOrgServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterOrgServlet at " + request.getContextPath() + "</h1>");
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
        
        request.setCharacterEncoding("UTF-8");
        
        try {
            // 1. Extract and parse parameters matching organization.java datatypes
            String orgName = request.getParameter("orgName");
            int registrationNum = Integer.parseInt(request.getParameter("registrationNum")); // int in your model
            String orgEmail = request.getParameter("orgEmail");
            int contactPerson = Integer.parseInt(request.getParameter("contactPerson")); // int in your model
            String orgAddress = request.getParameter("orgAddress");
            String orgType = request.getParameter("orgType");
            String orgPassword = request.getParameter("password"); // Maps form password field to orgPassword property

            String confirmPass = request.getParameter("confirmPassword");
            if (!orgPassword.equals(confirmPass)) {
                response.sendRedirect(request.getContextPath() + "/public/v-register.html?error=password_mismatch");
                return;
            }
            
            
            // 2. Bind parsed values to the lowercase organization POJO
            organization org = new organization();
            org.setOrgName(orgName);
            org.setRegistrationNum(registrationNum);
            org.setOrgEmail(orgEmail);
            org.setContactPerson(contactPerson);
            org.setOrgAddress(orgAddress);
            org.setOrgType(orgType);
            org.setOrgPassword(orgPassword);

            // 3. Delegate execution to your updated OrganizationDAO
            OrganizationDAO dao = new OrganizationDAO();
            boolean success = dao.registerOrg(org);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/public/org-login.html?status=registered");
            } else {
                response.sendRedirect(request.getContextPath() + "/public/org-register.html?error=failed");
            }

        } catch (NumberFormatException e) {
            // Triggers if registrationNum or contactPerson inputs are non-numeric strings
            e.printStackTrace();
            response.sendRedirect("org-register.html?error=invalid_number_format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("org-register.html?error=system_error");
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
