<%-- 
    Document   : v-dashboard
    Created on : Jun 29, 2026, 9:20:25 PM
    Author     : syahm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.volunteer" %>
<%@ page import="model.event" %>
<%@ page import="model.registration" %>
<%@ page import="dao.AttendanceDAO" %>
<%@ page import="dao.EventDAO" %>
<%@ page import="java.util.List" %>
<%
    // Session Guard: Verify volunteer login state
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
        <title>Volunteer Dashboard - E-Sukarelawan</title>
    </head>
    <body>
        <h1>E-Sukarelawan Student Portal</h1>
        <p>Logged in as: <strong><%= currentVolunteer.getFullName()%></strong> (Student ID: <%= currentVolunteer.getStudentId()%>)</p>

        <!-- Status Action Alerts -->
        <%
            String status = request.getParameter("status");
            String error = request.getParameter("error");
            if ("success".equals(status)) {
                out.println("<p style='color:green;'><strong>Operation completed successfully!</strong></p>");
            } else if ("registered".equals(status)) {
                out.println("<p style='color:green;'><strong>Successfully registered for the event! Check details below.</strong></p>");
            } else if ("verified".equals(status)) {
                out.println("<p style='color:green;'><strong>Attendance verified successfully! Your hours have been updated.</strong></p>");
            } else if ("invalid_code".equals(error)) {
                out.println("<p style='color:red;'><strong>Verification failed: Incorrect Secret Code. Please try again.</strong></p>");
            } else if ("error".equals(error)) {
                out.println("<p style='color:red;'><strong>An unexpected error occurred during processing.</strong></p>");
            }
        %>

        <!-- Navigation Menu -->
        <nav>
            <strong>Navigation:</strong>
            <a href="v-dashboard.jsp">Dashboard Home</a> | 
            <a href="v-profile.jsp">My Profile</a> | 
            <a href="v-browse-events.jsp">Browse Volunteer Calls</a> | 
            <a href="v-verify-attendance.jsp">Self-Verify My Attendance</a> | 
            <a href="v-leaderboard.jsp">Leaderboard</a> | 
            <a href="../LogoutServlet">Logout</a>
        </nav>
        <hr>

        <h2>My Social Contribution Profile</h2>
        <table border="1" cellpadding="5">
            <tr>
                <th>Accumulated Volunteer Hours</th>
                <td style="font-size: 1.2em; color: green;"><strong><%= currentVolunteer.getTotalHours()%> Hours</strong></td>
            </tr>
            <tr>
                <th>Course / Programme</th>
                <td><%= currentVolunteer.getCourse()%></td>
            </tr>
        </table>

        <hr>

        <h2>My Registered Activities History</h2>
        <p>Below is the live status of all campaigns you have registered to join:</p>

        <table border="1" cellpadding="8">
            <thead>
                <tr>
                    <th>Registration ID</th>
                    <th>Event Name</th>
                    <th>Campaign Date</th>
                    <th>Assigned Task</th>
                    <th>Credits Offered</th>
                    <th>Verification Status</th>
                </tr>
            </thead>
            <tbody>
                <%
                    try {
                        AttendanceDAO attendanceDAO = new AttendanceDAO();
                        EventDAO eventDAO = new EventDAO();

                        // Fetch attendance list based on the active volunteer ID
                        List<registration> myRegs = attendanceDAO.getAttendanceByVolunteerId(currentVolunteer.getVolunteerId());

                        if (myRegs == null || myRegs.isEmpty()) {
                %>
                <tr>
                    <td colspan="6" align="center">You have not registered for any events yet. <a href="v-browse-events.jsp">Browse active campaigns here</a>.</td>
                </tr>
                <%
                } else {
                    for (registration reg : myRegs) {
                        // Fetch corresponding event metadata
                        event e = eventDAO.getEventById(reg.getEventId());
                        if (e != null) {
                %>
                <tr>
                    <td><%= reg.getRegisterId()%></td>
                    <td><strong><%= e.getEventName()%></strong></td>
                    <td><%= e.getEventDate()%></td>
                    <td><%= e.getTaskDesc()%></td>
                    <td><%= e.getEventHour()%> Hours</td>
                    <td>
                        <% if ("Verified".equalsIgnoreCase(reg.getAttendanceStatus())) { %>
                        <span style="color: green; font-weight: bold;">Verified ✔</span>
                        <% } else { %>
                        <span style="color: orange; font-weight: bold;">Pending verification</span> 
                        (<a href="v-verify-attendance.jsp">verify now</a>)
                        <% } %>
                    </td>
                </tr>
                <%
                            }
                        }
                    }
                } catch (Exception ex) {
                %>
                <tr>
                    <td colspan="6" style="color: red;">Failed to retrieve activity records: <%= ex.getMessage()%></td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>

        <hr>
        <p><small>E-Sukarelawan System Secured Space &copy; 2026</small></p>   
    </body>
</html>
