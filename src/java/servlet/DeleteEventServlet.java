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
import model.event;
import model.organization;
import dao.EventDAO;

/**
 *
 * @author Asus
 */
public class DeleteEventServlet extends HttpServlet {

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
            out.println("<title>Servlet DeleteEventServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DeleteEventServlet at " + request.getContextPath() + "</h1>");
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

        // 1. Validate the active session context and fetch logged-in organization
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentOrg") == null) {
            response.sendRedirect("org-login.html?error=session_expired");
            return;
        }

        organization currentOrg = (organization) session.getAttribute("currentOrg");

        // 2. Validate input eventId parameter
        String eventIdParam = request.getParameter("eventId");
        if (eventIdParam == null || eventIdParam.trim().isEmpty()) {
            response.sendRedirect("org-manage-events.jsp?error=missing_event_id");
            return;
        }

        try {
            int eventId = Integer.parseInt(eventIdParam.trim());

            // 3. Fetch the event details to verify ownership
            EventDAO dao = new EventDAO();
            event existingEvent = dao.getEventById(eventId);

            if (existingEvent == null) {
                response.sendRedirect("org-manage-events.jsp?error=event_not_found");
                return;
            }

            // SECURITY CHECK: Verify target event belongs to this authenticated organization
            if (existingEvent.getOrgId() != currentOrg.getOrgId()) {
                response.sendRedirect("org-manage-events.jsp?error=access_denied");
                return;
            }

            // 4. Perform the deletion on passing secure validation checks
            boolean success = dao.deleteEvent(eventId);

            if (success) {
                response.sendRedirect("org-manage-events.jsp?status=deleted");
            } else {
                response.sendRedirect("org-manage-events.jsp?error=delete_failed");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("org-manage-events.jsp?error=invalid_event_id_format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("org-manage-events.jsp?error=system_error");
        }

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
        // Safe fall-through: Route any POST requests to the main GET authentication and deletion sequence
        doGet(request, response);
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
