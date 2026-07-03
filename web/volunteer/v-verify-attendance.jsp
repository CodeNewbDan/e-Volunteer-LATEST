<%-- 
    Document   : v-verify-attendance
    Created on : Jun 29, 2026, 9:23:29 PM
    Author     : syahm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.volunteer" %>
<%@ page import="model.event" %>
<%@ page import="dao.AttendanceDAO" %>
<%@ page import="java.util.List" %>
<%
    // Session Guard: Verify volunteer authentication state
    volunteer currentVolunteer = (volunteer) session.getAttribute("currentVolunteer");
    if (currentVolunteer == null) {
        response.sendRedirect(request.getContextPath() + "/public/v-login.html");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>E-Sukarelawan - Verify Attendance</title>
    </head>
    <body>
        <h1>Self-Verify Your Event Attendance</h1>
        <p>Logged in as: <strong><%= currentVolunteer.getFullName()%></strong> (Student ID: <%= currentVolunteer.getStudentId()%>)</p>

        <!-- Navigation Menu -->
        <nav>
            <strong>Navigation:</strong>
            <a href="v-dashboard.jsp">Dashboard Home</a> | 
            <a href="v-profile.jsp">My Profile</a> | 
            <a href="v-browse-events.jsp">Browse Volunteer Calls</a> | 
            <a href="v-verify-attendance.jsp">Self-Verify My Attendance</a> | 
            <a href="v-leaderboard.jsp">My Leaderboard</a> | 
            <a href="../LogoutServlet">Logout</a>
        </nav>
        <hr>

        <h2>Enter Event Verification Code</h2>
        <p>Select your registered campaign below and input the secret validation code provided by your event coordinator.</p>

        <!-- Context Status Message Blocks -->
        <%
            String error = request.getParameter("error");
            if ("1".equals(error)) {
                out.println("<p style='color:red;'><strong>Verification Mismatch: The secret validation code entered is incorrect. Please try again!</strong></p><hr>");
            } else if ("missing_fields".equals(error)) {
                out.println("<p style='color:red;'><strong>Verification Error: Please select an event and fill in the verification code.</strong></p><hr>");
            } else if ("database_error".equals(error)) {
                out.println("<p style='color:red;'><strong>System Error: Could not complete the database transaction.</strong></p><hr>");
            }
        %>
        
        <%
            try {
                AttendanceDAO attendanceDAO = new AttendanceDAO();
                List<event> pendingList = attendanceDAO.getPendingRegisteredEvents(currentVolunteer.getVolunteerId());

                if (pendingList == null || pendingList.isEmpty()) {
        %>
        <p><strong>You have no registered events pending verification at this time.</strong></p>
        <p>Register for upcoming activities on the <a href="v-browse-events.jsp">Browse Events</a> page to earn social contribution hours!</p>
        <%
        } else {
        %>
        <!-- Form targets the context root controller to step back from the secure subfolder level -->
        <form action="../VerifyAttendanceServlet" method="POST">
            <table>
                <tr>
                    <td><label for="eventId">Select Campaign Activity:</label></td>
                    <td>
                        <select id="eventId" name="eventId" required>
                            <option value="">-- Choose Pending Event --</option>
                            <%
                                for (event ev : pendingList) {
                            %>
                            <option value="<%= ev.getEventId()%>">
                                <%= ev.getEventName()%> (Date Scheduled: <%= ev.getEventDate()%>) - <%= ev.getEventHour()%> Hours
                            </option>
                            <%
                                }
                            %>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label for="secretCode">Secret Verification Code:</label></td>
                    <td>
                        <input type="text" id="secretCode" name="secretCode" required placeholder="e.g., RELIEF2026">
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <button type="submit">Verify & Claim Hours</button>
                        <button type="reset">Clear Fields</button>
                    </td>
                </tr>
            </table>
        </form>

        <hr>

        <h3>Campaign Reference List (Pending Claim Status)</h3>
        <table border="1" cellpadding="8">
            <thead>
                <tr>
                    <th>Event ID</th>
                    <th>Campaign Name</th>
                    <th>Location Context</th>
                    <th>Event Hour Value</th>
                    <th>Verification Status</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (event ev : pendingList) {
                %>
                <tr>
                    <td><%= ev.getEventId()%></td>
                    <td><strong><%= ev.getEventName()%></strong></td>
                    <td><%= ev.getLocation()%></td>
                    <td><%= ev.getEventHour()%> Hours</td>
                    <td style="color: orange; font-weight: bold;">Unverified (Code entry required)</td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
        <%
            }
        } catch (Exception ex) {
        %>
        <p style="color: red;">Error displaying pending registered campaigns: <%= ex.getMessage()%></p>
        <%
            }
        %>
        
        <hr>
        <p><small>E-Sukarelawan Hour Verification Portal &copy; 2026</small></p>
    </body>
</html>
