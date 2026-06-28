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
import dao.EventDAO;
import model.event;
import model.organization;

/**
 *
 * @author Asus
 */
public class CreateEventServlet extends HttpServlet {

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
            out.println("<title>Servlet CreateEventServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateEventServlet at " + request.getContextPath() + "</h1>");
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
        
        // 1. Force UTF-8 character encoding standards
        request.setCharacterEncoding("UTF-8");

        // 2. Validate current session and retrieve logged-in organization context
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentOrg") == null) {
            response.sendRedirect("org-login.html?error=session_expired");
            return;
        }

        organization currentOrg = (organization) session.getAttribute("currentOrg");

        try {
            // 3. Extract parameters directly from HTML Form submission
            String eventName = request.getParameter("eventName");
            String eventDate = request.getParameter("eventDate"); // Comes in "yyyy-MM-dd" format from date inputs
            String location = request.getParameter("location");
            int numOfVolunteer = Integer.parseInt(request.getParameter("numOfVolunteer"));
            String taskDesc = request.getParameter("taskDesc");
            double eventHour = Double.parseDouble(request.getParameter("eventHour"));
            String secretCode = request.getParameter("secretCode");

            if (secretCode != null) {
                secretCode = secretCode.trim();
            }

            // 4. Construct and populate the event POJO model
            event newEvent = new event();

            // SECURITY: Set relational foreign key owner ID directly from validated session context
            newEvent.setOrgId(currentOrg.getOrgId());

            // Set properties matching exact casings and data types in event.java
            newEvent.setEventName(eventName);
            newEvent.setEventDate(eventDate); // Maps straight to your String property!
            newEvent.setLocation(location);
            newEvent.setStatus("Active");     // Default starting state for campaign
            newEvent.setNumOfVolunteer(numOfVolunteer);
            newEvent.setTaskDesc(taskDesc);
            newEvent.setEventHour(eventHour);
            newEvent.setSecretCode(secretCode);

            // 5. Delegate event creation persistence to your EventDAO
            EventDAO dao = new EventDAO();
            boolean success = dao.createEvent(newEvent);

            if (success) {
                response.sendRedirect("org-manage-events.jsp?status=created");
            } else {
                response.sendRedirect("org-create-event.jsp?error=database_failure");
            }

        } catch (NumberFormatException e) {
            // Handle parsing issues for double hours and integer volunteer capacities safely
            e.printStackTrace();
            response.sendRedirect("org-create-event.jsp?error=invalid_number_format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("org-create-event.jsp?error=system_error");
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
