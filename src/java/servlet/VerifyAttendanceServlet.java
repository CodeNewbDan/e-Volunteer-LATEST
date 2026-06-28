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
import model.*;
import dao.*;

/**
 *
 * @author Asus
 */
public class VerifyAttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
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
            out.println("<title>Servlet VerifyAttendanceServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VerifyAttendanceServlet at " + request.getContextPath() + "</h1>");
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
        
        // 1. Force UTF-8 encoding standards
        request.setCharacterEncoding("UTF-8");
        
        // 2. Validate current session and retrieve logged-in volunteer context
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentVolunteer") == null) {
            response.sendRedirect("v-login.html?error=session_expired");
            return;
        }
        
        volunteer student = (volunteer) session.getAttribute("currentVolunteer");
        
        // 3. Extract inputs from form parameters
        String eventIdStr = request.getParameter("eventId");
        String userInputCode = request.getParameter("secretCode");
        
        // Validate eventId parameter using helper method
        if (isParamEmpty(eventIdStr)) {
            response.sendRedirect("v-verify-attendance.jsp?error=missing_event_id");
            return;
        }
        
        if (userInputCode == null) {
            userInputCode = "";
        }
        userInputCode = userInputCode.trim();
        
        try {
            int eventId = Integer.parseInt(eventIdStr.trim());
            
            // 4. Retrieve event details from the database using EventDAO to compare codes
            EventDAO eventDAO = new EventDAO();
            event targetEvent = eventDAO.getEventById(eventId);
            
            if (targetEvent == null) {
                response.sendRedirect("v-verify-attendance.jsp?error=invalid_event");
                return;
            }
            
            // 5. Evaluate the user's input code case-insensitively
            if (targetEvent.getSecretCode().equalsIgnoreCase(userInputCode)) {
                
                AttendanceDAO attendanceDAO = new AttendanceDAO();
                int volunteerId = student.getVolunteerId(); // Aligned to getVolunteerId() in volunteer.java
                
                // 6. Mark Registration attendance status as 'Verified'
                boolean updateStatusSuccess = attendanceDAO.verifyRegistrationStatus(volunteerId, eventId);
                
                if (updateStatusSuccess) {
                    // 7. Calculate and persist new denormalized total hours (Current Hours + Event Hours)
                    double addedHours = targetEvent.getEventHour(); // Aligned to getEventHour() in event.java POJO
                    double updatedTotal = student.getTotalHours() + addedHours;
                    
                    VolunteerDAO volunteerDAO = new VolunteerDAO();
                    boolean updateHoursSuccess = volunteerDAO.updateVolunteerHours(volunteerId, updatedTotal);
                    
                    if (updateHoursSuccess) {
                        // 8. CRITICAL: Refresh the active HTTP Session object in real-time!
                        // This updates the user's session instantly without needing a logout/login cycle.
                        student.setTotalHours(updatedTotal);
                        session.setAttribute("currentVolunteer", student);
                        
                        response.sendRedirect("v-dashboard.jsp?status=verification_success&hours=" + addedHours);
                    } else {
                        response.sendRedirect("v-verify-attendance.jsp?eventId=" + eventId + "&error=hours_update_failed");
                    }
                } else {
                    response.sendRedirect("v-verify-attendance.jsp?eventId=" + eventId + "&error=attendance_status_update_failed");
                }
            } else {
                // Secret validation code mismatch
                response.sendRedirect("v-verify-attendance.jsp?eventId=" + eventId + "&error=code_mismatch");
            }
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("v-verify-attendance.jsp?error=invalid_event_id_format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("v-verify-attendance.jsp?error=system_error");
        }
        
    }
    
    private boolean isParamEmpty(String value) {
        return value == null || value.trim().isEmpty();
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
