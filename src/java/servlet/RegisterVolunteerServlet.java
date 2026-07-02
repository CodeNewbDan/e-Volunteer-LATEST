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

/**
 *
 * @author Asus
 */
public class RegisterVolunteerServlet extends HttpServlet {

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
            out.println("<title>Servlet RegisterVolunteerServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterVolunteerServlet at " + request.getContextPath() + "</h1>");
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
            // 1. Retrieve and parse parameter types exactly as declared in volunteer.java (int for studentId and phoneNum)
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("volunteerEmail");
            int phoneNum = Integer.parseInt(request.getParameter("phoneNum"));
            String address = request.getParameter("volunteerAddress");
            String password = request.getParameter("volunteerPassword");
            String confirmPass = request.getParameter("confirmPassword");
            String course = request.getParameter("course");

            // Uppercase names and confirm password check
            String upperName = fullName.trim().toUpperCase();
            if (!password.equals(confirmPass)) {
                response.sendRedirect(request.getContextPath() + "/public/v-register.html?error=password_mismatch");
                return;
            }

            // 2. Instantiate and bind to the lowercase 'volunteer' model
            volunteer v = new volunteer();
            v.setStudentId(studentId);
            v.setFullName(upperName);
            v.setVolunteerEmail(email);
            v.setPhoneNum(phoneNum);
            v.setVolunteerAddress(address);
            v.setVolunteerPassword(password);
            v.setCourse(course);
            v.setTotalHours(0.00); // Initializes starting hours to 0.00

            // 3. Process database insert via synchronized DAO
            VolunteerDAO dao = new VolunteerDAO();
            boolean success = dao.registerVolunteer(v);

            if (success) {
                response.sendRedirect("public/v-login.html?status=registered"); 
            } else {
                response.sendRedirect("public/v-register.html?error=failed");
            }

        } catch (NumberFormatException e) {
            // Catches any invalid numerical inputs for StudentID or Phone Number
            e.printStackTrace();
            response.sendRedirect("v-register.html?error=invalid_number_format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("v-register.html?error=system_error");
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
