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
public class UpdateEventServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateEventServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateEventServlet at " + request.getContextPath() + "</h1>");
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

        // Safety Fallback: Ensure the event ID parameter is present
        String eventIdParam = request.getParameter("eventId");
        if (eventIdParam == null || eventIdParam.trim().isEmpty()) {
            response.sendRedirect("org-manage-events.jsp?error=missing_event_id");
            return;
        }

        try {
            int eventId = Integer.parseInt(eventIdParam.trim());

            // 3. Retrieve existing event from database to perform security check
            EventDAO dao = new EventDAO();
            event existingEvent = dao.getEventById(eventId);

            // SECURITY Guardrail: Verify target campaign exists and actually belongs to this logged-in organization
            if (existingEvent == null || existingEvent.getOrgId() != currentOrg.getOrgId()) {
                response.sendRedirect("org-manage-events.jsp?error=access_denied");
                return;
            }

            // 4. Extract updated parameters from HTML Form submission
            String eventName = request.getParameter("eventName");
            String eventDate = request.getParameter("eventDate"); // Comes in "yyyy-MM-dd" format
            String location = request.getParameter("location");
            String status = request.getParameter("status");       // Active or Completed
            int numOfVolunteer = Integer.parseInt(request.getParameter("numOfVolunteer"));
            String taskDesc = request.getParameter("taskDesc");
            double eventHour = Double.parseDouble(request.getParameter("eventHour"));
            String secretCode = request.getParameter("secretCode");

            if (secretCode != null) {
                secretCode = secretCode.trim();
            }

            // 5. Construct updated event model
            event updatedEvent = new event();
            updatedEvent.setEventId(eventId);
            updatedEvent.setOrgId(currentOrg.getOrgId()); // Maintain authenticated owner relationship
            updatedEvent.setEventName(eventName);
            updatedEvent.setEventDate(eventDate);
            updatedEvent.setLocation(location);
            updatedEvent.setStatus(status);
            updatedEvent.setNumOfVolunteer(numOfVolunteer);
            updatedEvent.setTaskDesc(taskDesc);
            updatedEvent.setEventHour(eventHour);
            updatedEvent.setSecretCode(secretCode);

            // 6. Persist edits using EventDAO
            boolean success = dao.updateEvent(updatedEvent);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/organization/org-manage-events.jsp?status=updated");
            } else {
                response.sendRedirect("org-edit-event.jsp?eventId=" + eventId + "&error=database_failure");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("org-manage-events.jsp?error=invalid_number_format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("org-manage-events.jsp?error=system_error");
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
