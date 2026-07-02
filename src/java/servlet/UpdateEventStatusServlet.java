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
public class UpdateEventStatusServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateEventStatusServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateEventStatusServlet at " + request.getContextPath() + "</h1>");
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

        // 1. Session Guard: Verify organization authentication state
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentOrg") == null) {
            response.sendRedirect(request.getContextPath() + "/public/org-login.html");
            return;
        }

        organization currentOrg = (organization) session.getAttribute("currentOrg");

        try {
            // 2. Extract parameters
            String eventIdStr = request.getParameter("eventId");
            String newStatus = request.getParameter("eventStatus");

            if (eventIdStr == null || eventIdStr.trim().isEmpty() || newStatus == null || newStatus.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organization/org-manage-events.jsp?status=error");
                return;
            }

            int eventId = Integer.parseInt(eventIdStr.trim());
            EventDAO eventDAO = new EventDAO();
            event existingEvent = eventDAO.getEventById(eventId);

            // 3. Access Control Barrier: Verify event ownership
            if (existingEvent == null || existingEvent.getOrgId() != currentOrg.getOrgId()) {
                response.sendRedirect(request.getContextPath() + "/organization/org-manage-events.jsp?status=error");
                return;
            }

            // 4. Validate and apply status transitions
            String sanitizedStatus = newStatus.trim();
            if ("Active".equalsIgnoreCase(sanitizedStatus)
                    || "Canceled".equalsIgnoreCase(sanitizedStatus)
                    || "Finished".equalsIgnoreCase(sanitizedStatus)) {

                existingEvent.setStatus(sanitizedStatus);
                boolean success = eventDAO.updateEvent(existingEvent);

                if (success) {
                    response.sendRedirect(request.getContextPath() + "/organization/org-manage-events.jsp?status=updated");
                } else {
                    response.sendRedirect(request.getContextPath() + "/organization/org-manage-events.jsp?status=error");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/organization/org-manage-events.jsp?status=error");
            }

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/organization/org-manage-events.jsp?status=error");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/organization/org-manage-events.jsp?status=error");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
