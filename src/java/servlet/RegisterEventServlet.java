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
import model.volunteer;
import dao.AttendanceDAO;
import model.event;
import dao.EventDAO;

/**
 *
 * @author Asus
 */
public class RegisterEventServlet extends HttpServlet {
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
            out.println("<title>Servlet RegisterEventServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterEventServlet at " + request.getContextPath() + "</h1>");
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
        // Safe fall-through: Route any GET requests to the main POST handler
        doPost(request, response);
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
       
        // 1. Force UTF-8 character encoding standards
        request.setCharacterEncoding("UTF-8");
        
        // 2. Validate current session and retrieve logged-in volunteer context
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentVolunteer") == null) {
            response.sendRedirect("v-login.html?error=session_expired");
            return;
        }
        
        volunteer student = (volunteer) session.getAttribute("currentVolunteer");
        
        // 3. Extract parameter from HTML Form submission (sign-up button click)
        String eventParam = request.getParameter("eventId");
        if (eventParam == null || eventParam.trim().isEmpty()) {
            response.sendRedirect("v-browse-events.jsp?error=missing_event");
            return;
        }
        
        try {
            int eventId = Integer.parseInt(eventParam.trim());
            EventDAO eventDAO = new EventDAO();
            int volunteerId = student.getVolunteerId(); // Aligned to getVolunteerId() in volunteer.java POJO
            
            // 4. Delegate registration creation to AttendanceDAO
            AttendanceDAO dao = new AttendanceDAO();
            
            
            event event = eventDAO.getEventById(eventId);
            if (event == null) {
                response.sendRedirect(request.getContextPath() + "/volunteer/v-browse-events.jsp?error=event_not_found");
                return;
            }
            
            // Count total currently registered students for this event
            int currentRegisteredCount = dao.getAttendanceByEventId(eventId).size();

            if (currentRegisteredCount >= event.getNumOfVolunteer()) {
                // Fail transaction gracefully - Event has reached full capacity!
                response.sendRedirect(request.getContextPath() + "/volunteer/v-browse-events.jsp?error=event_full");
                return;
            }
            
            boolean success = dao.registerForEvent(volunteerId, eventId);
            
            if (success) {
                // FIXED REDIRECT PATH: Explicitly route the browser into the /volunteer/ subfolder
                response.sendRedirect(request.getContextPath() + "/volunteer/v-dashboard.jsp?status=registered");
            } else {
                // FIXED REDIRECT PATH: Route back to the browse page within the secure subdirectory
                response.sendRedirect(request.getContextPath() + "/volunteer/v-browse-events.jsp?error=registration_failed_or_already_registered");
            }
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("v-browse-events.jsp?error=invalid_event_id_format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("v-browse-events.jsp?error=system_error");
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
