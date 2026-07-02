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
public class UpdateVolunteerProfileServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateVolunteerProfileServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateVolunteerProfileServlet at " + request.getContextPath() + "</h1>");
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

        // 2. Validate current session and retrieve logged-in volunteer context
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentVolunteer") == null) {
            response.sendRedirect("v-login.html?error=session_expired");
            return;
        }

        volunteer currentVolunteer = (volunteer) session.getAttribute("currentVolunteer");

        try {
            // 1. Current Password Verification Check (Mandatory field)
            String currentPasswordInput = request.getParameter("currentPassword");

            // Validate user's input password against the stored password in session/database
            if (currentPasswordInput == null || !currentPasswordInput.equals(currentVolunteer.getVolunteerPassword())) {
                response.sendRedirect(request.getContextPath() + "/volunteer/v-profile.jsp?status=wrong_password");
                return;
            }

            // 3. Extract updated form values and parse input formats safely
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("volunteerEmail");
            int phoneNum = Integer.parseInt(request.getParameter("phoneNum")); // Parsed safely to int
            String address = request.getParameter("volunteerAddress");
            String password = request.getParameter("newPassword");
            String course = request.getParameter("course");
            String confirmPass = request.getParameter("confirmNewPassword");

            // Validate that required fields are present
            if (fullName == null || fullName.trim().isEmpty()
                    || email == null || email.trim().isEmpty()) {

                response.sendRedirect(request.getContextPath() + "/volunteer/v-profile.jsp?status=missing_fields");
                return;
            }

            // New Credential Validation Layer (Optional fields)
            String finalPasswordToSave = currentVolunteer.getVolunteerPassword(); // Defaults to existing password

            if (password != null && !password.isEmpty()) {
                if (confirmPass == null || !password.equals(confirmPass)) {
                    // Fail redirect if new credentials don't match confirmation
                    response.sendRedirect(request.getContextPath() + "/volunteer/v-profile.jsp?status=password_mismatch");
                    return;
                }
                // Update finalPassword to the newly validated password
                finalPasswordToSave = password;
            }
            
            // Uppercase the name
            String upperName = fullName.trim().toUpperCase();

            
            // Construct the updated volunteer entity
            volunteer updated = new volunteer();

            // SECURITY check: Maintain critical read-only keys from session state
            updated.setVolunteerId(currentVolunteer.getVolunteerId()); // Matches exact primary key getter/setter
            updated.setStudentId(currentVolunteer.getStudentId());       // StudentID remains immutable
            updated.setTotalHours(currentVolunteer.getTotalHours());     // Verified hours are kept safe

            // Apply new profile modifications
            updated.setFullName(upperName);
            updated.setVolunteerEmail(email);
            updated.setPhoneNum(phoneNum);
            updated.setVolunteerAddress(address);
            updated.setVolunteerPassword(finalPasswordToSave);
            updated.setCourse(course);

            // 5. Delegate profile persistence to your VolunteerDAO
            VolunteerDAO dao = new VolunteerDAO();
            boolean success = dao.updateVolunteerProfile(updated);

            if (success) {
                // Update live active session attributes in real-time so changes reflect immediately
                session.setAttribute("currentVolunteer", updated);

                response.sendRedirect(request.getContextPath() + "/volunteer/v-profile.jsp?status=success");
            } else {
                response.sendRedirect(request.getContextPath() + "/volunteer/v-profile.jsp?status=error");
            }

        } catch (NumberFormatException e) {
            // Catches any invalid format issues when parsing phoneNum to primitive int
            e.printStackTrace();
            response.sendRedirect("v-profile.jsp?error=invalid_phone_format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("v-profile.jsp?error=system_error");
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
