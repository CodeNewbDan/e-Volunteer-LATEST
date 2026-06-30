<%-- 
    Document   : org-view-attendance
    Created on : Jun 29, 2026, 9:25:34 PM
    Author     : syahm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.organization" %>
<%@ page import="model.event" %>
<%@ page import="model.registration" %>
<%@ page import="model.volunteer" %>
<%@ page import="dao.EventDAO" %>
<%@ page import="dao.AttendanceDAO" %>
<%@ page import="dao.VolunteerDAO" %>
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
        <title>Registration & Attendance Monitor</title>
    </head>
    <body>
        <h1>Student Registration & Hour Verification Monitor</h1>
        <p>Monitor in real-time which students have registered for your events and who successfully claimed credit via verification codes.</p>

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

        <h2>Filter Attendance by Created Events</h2>
        <form action="org-view-attendance.jsp" method="GET">
            <label for="eventId">Select Campaign To Track:</label>
            <select id="eventId" name="eventId" onchange="this.form.submit()">
                <option value="">-- Choose Campaign --</option>
                <%
                    try {
                        EventDAO eventDAO = new EventDAO();
                        List<event> myEvents = eventDAO.getEventsByOrgId(currentOrg.getOrgId());
                        String selectedId = request.getParameter("eventId");

                        if (myEvents != null) {
                            for (event ev : myEvents) {
                                boolean isSelected = selectedId != null && selectedId.equals(String.valueOf(ev.getEventId()));
                %>
                <option value="<%= ev.getEventId()%>" <%= isSelected ? "selected" : ""%>>
                    <%= ev.getEventName()%> (Date Scheduled: <%= ev.getEventDate()%>)
                </option>
                <%
                            }
                        }
                    } catch (Exception ex) {
                        out.println("<option value=''>Error loading options</option>");
                    }
                %>
            </select>
            <button type="submit">Query Roster</button>
        </form>

        <hr>

        <%
            String eventIdParam = request.getParameter("eventId");
            if (eventIdParam != null && !eventIdParam.trim().isEmpty()) {
                try {
                    int selectedEventId = Integer.parseInt(eventIdParam.trim());
                    EventDAO eventDAO = new EventDAO();
                    event selectedEvent = eventDAO.getEventById(selectedEventId);

                    // Security barrier: prevent looking up rosters for campaigns that do not belong to you
                    if (selectedEvent != null && selectedEvent.getOrgId() == currentOrg.getOrgId()) {
        %>
        <h3>Roster Report for: <strong><%= selectedEvent.getEventName()%></strong></h3>
        <p>Total Credits Offered: <strong><%= selectedEvent.getEventHour()%> Hours</strong> | Secret Code: <code><%= selectedEvent.getSecretCode()%></code></p>

        <table border="1" cellpadding="8" width="100%">
            <thead>
                <tr>
                    <th>Registration ID</th>
                    <th>Student ID</th>
                    <th>Student Name</th>
                    <th>Academic Course</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <%
                    AttendanceDAO attendanceDAO = new AttendanceDAO();
                    VolunteerDAO volunteerDAO = new VolunteerDAO();
                    List<registration> rosters = attendanceDAO.getAttendanceByEventId(selectedEventId);

                    if (rosters == null || rosters.isEmpty()) {
                %>
                <tr>
                    <td colspan="5" align="center">No students have registered to join this event yet.</td>
                </tr>
                <%
                } else {
                    for (registration reg : rosters) {
                        volunteer student = volunteerDAO.getVolunteerById(reg.getVolunteerId());
                        if (student != null) {
                %>
                <tr>
                    <td align="center"><%= reg.getRegisterId()%></td>
                    <td><%= student.getStudentId()%></td>
                    <td><strong><%= student.getFullName()%></strong></td>
                    <td><%= student.getCourse()%></td>
                    <td>
                        <% if ("Verified".equalsIgnoreCase(reg.getAttendanceStatus())) { %>
                        <span style="color: green; font-weight: bold;">Verified ✔ (Credits Disbursed)</span>
                        <% } else { %>
                        <span style="color: orange; font-weight: bold;">Registered (Pending verification code entry)</span>
                        <% } %>
                    </td>
                </tr>
                <%
                            }
                        }
                    }
                %>
            </tbody>
        </table>
        <%
                    } else {
                        out.println("<p style='color:red;'><strong>Security Access Violation: You do not possess clearance for this event identifier.</strong></p>");
                    }
                } catch (NumberFormatException nfe) {
                    out.println("<p style='color:red;'><strong>Invalid query parameter string format.</strong></p>");
                }
            } else {
                out.println("<p><em>Select an active campaign from the dropdown above and click 'Query Roster' to render registered student listings.</em></p>");
            }
        %>
    </body>
</html>
