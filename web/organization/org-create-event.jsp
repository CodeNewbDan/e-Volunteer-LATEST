<%-- 
    Document   : org-create-events
    Created on : Jun 29, 2026, 9:25:02 PM
    Author     : syahm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.organization" %>
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
        <title>Create CSR Campaign - E-Sukarelawan</title>
    </head>
    <body>
        <h1>Post a New CSR Volunteer Call</h1>
    <p>Fill out the forms to launch a new activity and distribute community hours to students.</p>

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
    
    <form action="../CreateEventServlet" method="POST">
        <table border="1" cellpadding="8">
            <tr>
                <td><label for="eventName">Campaign / Event Name</label></td>
                <td>
                    <input type="text" id="eventName" name="eventName" required placeholder="e.g., Beach Cleanup CSR" style="width: 300px;">
                </td>
            </tr>
            <tr>
                <td><label for="eventDate">Scheduled Execution Date</label></td>
                <td>
                    <input type="date" id="eventDate" name="eventDate" required>
                    <br><small>Format: YYYY-MM-DD</small>
                </td>
            </tr>
            <tr>
                <td><label for="location">Location Context</label></td>
                <td>
                    <input type="text" id="location" name="location" required placeholder="e.g., Pantai Morib Beachfront" style="width: 300px;">
                </td>
            </tr>
            <tr>
                <td><label for="numOfVolunteer">Target Volunteer Capacity (Numbers Only)</label></td>
                <td>
                    <input type="number" id="numOfVolunteer" name="numOfVolunteer" required placeholder="e.g., 30" min="1">
                </td>
            </tr>
            <tr>
                <td><label for="eventHour">Accumulated Credits Offered (Hours - Double Precision)</label></td>
                <td>
                    <input type="number" id="eventHour" name="eventHour" required placeholder="e.g., 4.5" step="0.01" min="0.5">
                    <br><small>Decimal fractions are allowed (e.g. 5.5 hours).</small>
                </td>
            </tr>
            <tr>
                <td><label for="taskDesc">Assigned Task Descriptions</label></td>
                <td>
                    <textarea id="taskDesc" name="taskDesc" rows="4" cols="40" required placeholder="Outline basic responsibilities expected from student volunteer forces..."></textarea>
                </td>
            </tr>
            <tr>
                <td><label for="secretCode">Event Verification Secret Code</label></td>
                <td>
                    <input type="text" id="secretCode" name="secretCode" required placeholder="e.g., CLEAN99">
                    <br><small>Students must enter this code case-insensitively on their portals to claim credits.</small>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <button type="submit">Submit Campaign Form</button>
                    <button type="reset">Clear Form</button>
                </td>
            </tr>
        </table>
    </form>
    </body>
</html>
