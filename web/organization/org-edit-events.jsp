<%-- 
    Document   : org-edit-events
    Created on : Jun 29, 2026, 9:25:16 PM
    Author     : syahm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.organization" %>
<%@ page import="model.event" %>
<%@ page import="dao.EventDAO" %>

<%
    // Session Guard
    organization currentOrg = (organization) session.getAttribute("currentOrg");
    if (currentOrg == null) {
        response.sendRedirect("../public/org-login.html");
        return;
    }

    // Retrieve requested event targeting
    String eventIdParam = request.getParameter("eventId");
    if (eventIdParam == null || eventIdParam.trim().isEmpty()) {
        response.sendRedirect("org-manage-events.jsp?status=error");
        return;
    }

    int eventId = Integer.parseInt(eventIdParam.trim());
    EventDAO eventDAO = new EventDAO();
    event e = eventDAO.getEventById(eventId);

    // Context / Ownership verification: Ensure organizations cannot edit events from other entities
    if (e == null || e.getOrgId() != currentOrg.getOrgId()) {
        response.sendRedirect("org-manage-events.jsp?status=error");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Modify Campaign - E-Sukarelawan</title>
    </head>
    <body>
        <h1>Modify Event Properties: <%= e.getEventName()%></h1>
        <p>Update dates, adjust locations, change capacities, or modify verification security credentials.</p>

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
        
        <form action="../UpdateEventServlet" method="POST">
            <!-- Maintain relational keys -->
            <input type="hidden" name="eventId" value="<%= e.getEventId()%>">

            <table border="1" cellpadding="8">
                <tr>
                    <td><strong>Event Record ID (Read-Only)</strong></td>
                    <td><strong><%= e.getEventId()%></strong></td>
                </tr>
                <tr>
                    <td><label for="eventName">Campaign / Event Name</label></td>
                    <td>
                        <input type="text" id="eventName" name="eventName" value="<%= e.getEventName()%>" required style="width: 300px;">
                    </td>
                </tr>
                <tr>
                    <td><label for="eventDate">Scheduled Date</label></td>
                    <td>
                        <input type="date" id="eventDate" name="eventDate" value="<%= e.getEventDate()%>" required>
                    </td>
                </tr>
                <tr>
                    <td><label for="location">Location Context</label></td>
                    <td>
                        <input type="text" id="location" name="location" value="<%= e.getLocation()%>" required style="width: 300px;">
                    </td>
                </tr>
                <tr>
                    <td><label for="numOfVolunteer">Target Volunteer Capacity (Numbers Only)</label></td>
                    <td>
                        <input type="number" id="numOfVolunteer" name="numOfVolunteer" value="<%= e.getNumOfVolunteer()%>" required min="1">
                    </td>
                </tr>
                <tr>
                    <td><label for="eventHour">Credits Gained (Hours - Double Precision)</label></td>
                    <td>
                        <input type="number" id="eventHour" name="eventHour" value="<%= e.getEventHour()%>" required step="0.01" min="0.5">
                    </td>
                </tr>
                <tr>
                    <td><label for="taskDesc">Assigned Task Descriptions</label></td>
                    <td>
                        <textarea id="taskDesc" name="taskDesc" rows="4" cols="40" required><%= e.getTaskDesc()%></textarea>
                    </td>
                </tr>
                <tr>
                    <td><label for="secretCode">Verification Secret Code</label></td>
                    <td>
                        <input type="text" id="secretCode" name="secretCode" value="<%= e.getSecretCode()%>" required>
                        <br><small>Adjusting this value alters required entry parameters for student verifications.</small>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <button type="submit">Save Changes</button>
                        <a href="org-manage-events.jsp">Cancel Modifications</a>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
