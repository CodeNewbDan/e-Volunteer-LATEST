<%-- 
    Document   : org-manage-events
    Created on : Jun 29, 2026, 9:24:49 PM
    Author     : syahm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.organization" %>
<%@ page import="model.event" %>
<%@ page import="dao.EventDAO" %>
<%@ page import="java.util.List" %>
<%
    // Session Guard
    organization currentOrg = (organization) session.getAttribute("currentOrg");
    if (currentOrg == null) {
        response.sendRedirect("../public/org-login.html");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My CSR Campaigns - E-Sukarelawan</title>
    </head>
    <body>
        <h1>Manage CSR Activities & Event Listings</h1>
    <p>Perform full database CRUD modifications on active campaigns. Generate, edit or delete social service calls.</p>

    <!-- Navigation Menu -->
    <nav>
        <a href="org-dashboard.jsp">Dashboard Home</a> | 
        <a href="org-profile.jsp">Manage Profile</a> | 
        <a href="org-manage-events.jsp">My Campaigns (CRUD)</a> | 
        <a href="org-create-event.jsp">Create New Event</a> | 
        <a href="org-view-attendance.jsp">Track Registrations & Attendance</a> | 
        <a href="../LogoutServlet">Logout</a>
    </nav>
    <hr>
    
    <% 
        String status = request.getParameter("status");
        if ("created".equals(status)) {
            out.println("<p style='color:green;'><strong>New CSR volunteer call successfully added to database!</strong></p><hr>");
        } else if ("updated".equals(status)) {
            out.println("<p style='color:green;'><strong>Campaign information successfully modified and updated.</strong></p><hr>");
        } else if ("deleted".equals(status)) {
            out.println("<p style='color:blue;'><strong>Campaign record successfully removed from the system registry.</strong></p><hr>");
        } else if ("error".equals(status)) {
            out.println("<p style='color:red;'><strong>An database exception occurred processing this command. Check server logs.</strong></p><hr>");
        }
    %>
    
    <p><a href="org-create-event.jsp"><strong>[+] Post a New Volunteer Call</strong></a></p>
    
    <table border="1" cellpadding="8" width="100%">
        <thead>
            <tr>
                <th>Event ID</th>
                <th>Campaign Title</th>
                <th>Target Date</th>
                <th>Location Context</th>
                <th>Total Credits</th>
                <th>Vacancies</th>
                <th>Secret verification Code</th>
                <th>Operations</th>
            </tr>
        </thead>
        <tbody>
            <%
                try {
                    EventDAO eventDAO = new EventDAO();
                    List<event> list = eventDAO.getEventsByOrgId(currentOrg.getOrgId());
                    
                    if (list == null || list.isEmpty()) {
            %>
                        <tr>
                            <td colspan="8" align="center">No events found under your account. Click 'Post a New Volunteer Call' above to start.</td>
                        </tr>
            <%
                    } else {
                        for (event ev : list) {
            %>
                            <tr>
                                <td><%= ev.getEventId() %></td>
                                <td><strong><%= ev.getEventName() %></strong></td>
                                <td><%= ev.getEventDate() %></td>
                                <td><%= ev.getLocation() %></td>
                                <td><%= ev.getEventHour() %> Hours</td>
                                <td><%= ev.getNumOfVolunteer() %> volunteers</td>
                                <td><code><%= ev.getSecretCode() %></code></td>
                                <td>
                                    <% if ("Active".equalsIgnoreCase(ev.getStatus())) { %>
                                    <span style="color: green; font-weight: bold;">Active</span>
                                    <% } else if ("Canceled".equalsIgnoreCase(ev.getStatus())) { %>
                                    <span style="color: red; font-weight: bold;">Canceled</span>
                                    <% } else { %>
                                    <span style="color: blue; font-weight: bold;">Finished</span>
                                    <% }%>
                                </td>
                                <td>
                                    <!-- Status update dropdown triggers submission automatically -->
                                    <form action="../UpdateEventStatusServlet" method="POST" style="margin: 0; display: inline;">
                                        <input type="hidden" name="eventId" value="<%= ev.getEventId()%>">
                                        <select name="eventStatus" onchange="this.form.submit()">
                                            <option value="">-- Toggle Status --</option>
                                            <option value="Active" <%= "Active".equalsIgnoreCase(ev.getStatus()) ? "selected" : ""%>>Active</option>
                                            <option value="Finished" <%= "Finished".equalsIgnoreCase(ev.getStatus()) ? "selected" : ""%>>Finished</option>
                                            <option value="Canceled" <%= "Canceled".equalsIgnoreCase(ev.getStatus()) ? "selected" : ""%>>Canceled</option>
                                        </select>
                                    </form>
                                </td>
                                <td>
                                    <a href="org-edit-events.jsp?eventId=<%= ev.getEventId()%>">Edit Details</a> | 
                                    <a href="../DeleteEventServlet?eventId=<%= ev.getEventId()%>" 
                                       onclick="return confirm('Are you sure you want to permanently delete this event?');"
                                       style="color:red;">Delete</a>
                                </td>
                            </tr>
            <%
                        }
                    }
                } catch (Exception ex) {
            %>
                    <tr>
                        <td colspan="8" style="color: red;">Failed to parse event listings: <%= ex.getMessage() %></td>
                    </tr>
            <%
                }
            %>
        </tbody>
    </table>
    </body>
</html>
