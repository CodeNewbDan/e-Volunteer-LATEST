<%-- 
    Document   : v-leaderboard
    Created on : Jun 29, 2026, 9:23:40 PM
    Author     : syahm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.volunteer" %>
<%@ page import="dao.VolunteerDAO" %>
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
        <title>University Social Contribution Leaderboard</title>
    </head>
    <body>
        <h1>Global Social Impact Leaderboard</h1>
    <p>Honoring our top contributors generating community hours dynamically across E-Sukarelawan.</p>

    <!-- Navigation Menu -->
    <nav>
        <a href="v-dashboard.jsp">Dashboard Home</a> | 
        <a href="v-profile.jsp">My Profile</a> | 
        <a href="v-browse-events.jsp">Browse Volunteer Calls</a> | 
        <a href="v-verify-attendance.jsp">Self-Verify My Attendance</a> | 
        <a href="v-leaderboard.jsp">My Leaderboard</a> | 
        <a href="../LogoutServlet">Logout</a>
    </nav>
    <hr>

    <h2>Top Individual Volunteer Leaders</h2>
    <table border="1" cellpadding="8">
        <thead>
            <tr>
                <th>Ranking Position</th>
                <th>Full Name</th>
                <th>Course Name</th>
                <th>Verified Social Hours Contribution</th>
            </tr>
        </thead>
        <tbody>
            <%
                try {
                    VolunteerDAO volunteerDAO = new VolunteerDAO();
                    // Custom order listing method to satisfy structural specification sorting 
                    List<volunteer> sortedVolunteers = volunteerDAO.getAllVolunteersSortedByHours();
                    
                    if (sortedVolunteers == null || sortedVolunteers.isEmpty()) {
            %>
                        <tr>
                            <td colspan="4" align="center">No volunteer leaderboard statistics calculated yet.</td>
                        </tr>
            <%
                    } else {
                        int ranking = 1;
                        for (volunteer v : sortedVolunteers) {
                            // Check if current row represents the logged-in user to mark visually
                            boolean isMe = (v.getVolunteerId() == currentVolunteer.getVolunteerId());
            %>
                            <tr <%= isMe ? "style='background-color:#E8F0FE; font-weight:bold;'" : "" %>>
                                <td align="center"><strong>#<%= ranking++ %></strong></td>
                                <td><%= v.getFullName() %> <%= isMe ? "(You)" : "" %></td>
                                <td><%= v.getCourse() %></td>
                                <td style="color: green; font-weight: bold;"><%= v.getTotalHours() %> Hours</td>
                            </tr>
            <%
                        }
                    }
                } catch (Exception ex) {
            %>
                    <tr>
                        <td colspan="4" style="color: red;">Error processing global ranking: <%= ex.getMessage() %>. Ensure the method getAllVolunteersSortedByHours() is implemented in VolunteerDAO.java.</td>
                    </tr>
            <%
                }
            %>
        </tbody>
    </table>
    </body>
</html>
