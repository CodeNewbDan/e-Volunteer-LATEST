<%-- 
    Document   : v-browse-events
    Created on : Jun 29, 2026, 9:22:56 PM
    Author     : syahm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.volunteer" %>
<%@ page import="model.event" %>
<%@ page import="dao.EventDAO" %>
<%@ page import="java.util.List" %>
<%
    // Session Guard
    volunteer currentVolunteer = (volunteer) session.getAttribute("currentVolunteer");
    if (currentVolunteer == null) {
        response.sendRedirect("../public/v-login.html");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Browse Active Volunteer Campaigns - E-Sukarelawan</title>
    </head>
    <body>
        <h1>Active University Volunteer Calls</h1>
    <p>Review and join ongoing CSR campaigns to secure social service hour credits.</p>

    <!-- Navigation Menu -->
    <nav>
        <a href="v-dashboard.jsp">Dashboard Home</a> | 
        <a href="v-profile.jsp">My Profile</a> | 
        <a href="v-browse-events.jsp">Browse Volunteer Calls</a> | 
        <a href="v-verify-attendance.jsp">Self-Verify My Attendance</a> | 
        <a href="v-leaderboard.jsp">Leaderboard</a> | 
        <a href="../LogoutServlet">Logout</a>
    </nav>
    <hr>

    <table border="1" cellpadding="10" width="100%">
        <thead>
            <tr>
                <th>Event Name</th>
                <th>Target Date</th>
                <th>Location Context</th>
                <th>Assigned / Needed Tasks</th>
                <th>Volunteer Vacancies</th>
                <th>Hour Credits Gained</th>
                <th>Action Button</th>
            </tr>
        </thead>
        <tbody>
            <%
                try {
                    EventDAO eventDAO = new EventDAO();
                    List<event> activeEvents = eventDAO.getAllEvents();
                    
                    if (activeEvents == null || activeEvents.isEmpty()) {
            %>
                        <tr>
                            <td colspan="7" align="center">No volunteer calls are currently active in the database system. Please check back later.</td>
                        </tr>
            <%
                    } else {
                        for (event ev : activeEvents) {
            %>
                            <tr>
                                <td><strong><%= ev.getEventName() %></strong></td>
                                <td><%= ev.getEventDate() %></td>
                                <td><%= ev.getLocation() %></td>
                                <td><%= ev.getTaskDesc() %></td>
                                <td><%= ev.getNumOfVolunteer() %> seats open</td>
                                <td style="color: blue; font-weight: bold;"><%= ev.getEventHour() %> Hours</td>
                                <td>
                                    <!-- Sends the registration event submission directly to RegisterEventServlet -->
                                    <form action="../RegisterEventServlet" method="POST" style="display:inline;">
                                        <input type="hidden" name="eventId" value="<%= ev.getEventId() %>">
                                        <button type="submit">Join Event Registration</button>
                                    </form>
                                </td>
                            </tr>
            <%
                        }
                    }
                } catch (Exception ex) {
            %>
                    <tr>
                        <td colspan="7" style="color: red;">Error displaying volunteer campaigns: <%= ex.getMessage() %></td>
                    </tr>
            <%
                }
            %>
        </tbody>
    </table>
    </body>
</html>
