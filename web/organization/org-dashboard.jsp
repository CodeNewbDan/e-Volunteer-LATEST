<%-- 
    Document   : org-dashboard
    Created on : Jun 29, 2026, 9:23:59 PM
    Author     : syahm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.organization" %>
<%@ page import="model.event" %>
<%@ page import="dao.EventDAO" %>
<%@ page import="dao.AttendanceDAO" %>
<%@ page import="java.util.List" %>

<%
    // Session Guard: Verify organization login state
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
        <title>Organization Dashboard - E-Sukarelawan</title>
    </head>
    <body>
        <h1>E-Sukarelawan Organization Portal</h1>
        <p>Welcome back, <strong><%= currentOrg.getOrgName()%></strong> (Club Registration ID: <%= currentOrg.getRegistrationNum()%>)</p>

        <!-- Navigation Menu -->
        <nav>
            <strong>Navigation:</strong>
            <a href="org-dashboard.jsp">Dashboard Home</a> | 
            <a href="org-profile.jsp">Manage Profile</a> | 
            <a href="org-manage-events.jsp">My Campaigns (CRUD)</a> | 
            <a href="org-create-event.jsp">Create New Event</a> | 
            <a href="org-view-attendance.jsp">Track Registrations & Attendance</a> | 
            <a href="../LogoutServlet">Logout</a>
        </nav>
        <hr>

        <h2>Club Performance Metrics</h2>

        <%
            double totalHoursGenerated = 0.0;
            int activeCampaignsCount = 0;
            try {
                EventDAO eventDAO = new EventDAO();
                AttendanceDAO attendanceDAO = new AttendanceDAO();
                List<event> myEvents = eventDAO.getEventsByOrgId(currentOrg.getOrgId());

                if (myEvents != null) {
                    activeCampaignsCount = myEvents.size();
                    for (event ev : myEvents) {
                        // Fetch verified registrations to compute total hours generated
                        // This dynamically aggregates hours: (verified attendance count * event hourly credit)
                        int verifiedCount = 0;
                        List<model.registration> regs = attendanceDAO.getAttendanceByEventId(ev.getEventId());
                        if (regs != null) {
                            for (model.registration r : regs) {
                                if ("Verified".equalsIgnoreCase(r.getAttendanceStatus())) {
                                    verifiedCount++;
                                }
                            }
                        }
                        totalHoursGenerated += (verifiedCount * ev.getEventHour());
                    }
                }
            } catch (Exception ex) {
                out.println("<p style='color:red;'>Metric calculation error: " + ex.getMessage() + "</p>");
            }
        %>

        <table border="1" cellpadding="8">
            <tr>
                <th>Total Community Hours Generated</th>
                <td style="font-size: 1.2em; color: green;"><strong><%= totalHoursGenerated%> Hours</strong></td>
            </tr>
            <tr>
                <th>Active Created Campaigns</th>
                <td><strong><%= activeCampaignsCount%> Events</strong></td>
            </tr>
        </table>
            
                <hr>

                <h2>Active Campaigns List</h2>
                <table border="1" cellpadding="8">
                    <thead>
                        <tr>
                            <th>Event ID</th>
                            <th>Campaign Name</th>
                            <th>Date scheduled</th>
                            <th>Location Context</th>
                            <th>Validation Code</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            try {
                                EventDAO eventDAO = new EventDAO();
                                List<event> myEvents = eventDAO.getEventsByOrgId(currentOrg.getOrgId());
                                if (myEvents == null || myEvents.isEmpty()) {
                        %>
                        <tr>
                            <td colspan="5" align="center">You haven't posted any volunteer calls yet. <a href="org-create-event.jsp">Post your first event now</a>.</td>
                        </tr>
                        <%
                        } else {
                            for (event ev : myEvents) {
                        %>
                        <tr>
                            <td><%= ev.getEventId()%></td>
                            <td><strong><%= ev.getEventName()%></strong></td>
                            <td><%= ev.getEventDate()%></td>
                            <td><%= ev.getLocation()%></td>
                            <td><code><%= ev.getSecretCode()%></code></td>
                        </tr>
                        <%
                                }
                            }
                        } catch (Exception ex) {
                        %>
                        <tr>
                            <td colspan="5" style="color: red;">Error displaying campaigns: <%= ex.getMessage()%></td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
                    <hr>
                    <p><small>E-Sukarelawan Organization Secured Space &copy; 2026</small></p>
    </body>
</html>
