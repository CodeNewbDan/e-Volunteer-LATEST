<%-- 
    Document   : org-profile
    Created on : Jun 29, 2026, 9:24:35 PM
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
        <title>Manage Club Profile - E-Sukarelawan</title>
    </head>
    <body>
        <h1>Organization Account Details</h1>
        <p>Review registration records and keep your organization's metadata up to date.</p>

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
            if ("success".equals(status)) {
                out.println("<p style='color:green;'><strong>Profile details successfully updated in database!</strong></p><hr>");
            } else if ("error".equals(status)) {
                out.println("<p style='color:red;'><strong>Failed to update profile. Check logs for attribute null constraint errors.</strong></p><hr>");
            }
        %>

        <form action="../UpdateOrgProfileServlet" method="POST">
            <table border="1" cellpadding="8">
                <tr>
                    <td><strong>Registration / Club ID (Read-Only)</strong></td>
                    <td>
                        <input type="text" value="<%= currentOrg.getRegistrationNum()%>" disabled>
                        <small>Official registration code cannot be changed online.</small>
                    </td>
                </tr>
                <tr>
                    <td><label for="orgName">Organization Name</label></td>
                    <td>
                        <input type="text" id="orgName" name="orgName" value="<%= currentOrg.getOrgName()%>" required style="width: 300px;">
                    </td>
                </tr>
                <tr>
                    <td><label for="email">Official Email</label></td>
                    <td>
                        <input type="email" id="email" name="orgEmail" value="<%= currentOrg.getOrgEmail()%>" required style="width: 300px;">
                    </td>
                </tr>
                <tr>
                    <td><label for="contactPerson">Official Contact Number (Numbers Only)</label></td>
                    <td>
                        <input type="number" id="contactPerson" name="contactPerson" value="<%= currentOrg.getContactPerson()%>" required>
                    </td>
                </tr>
                <tr>
                    <td><label for="orgType">Organization Classification</label></td>
                    <td>
                        <select id="orgType" name="orgType" required>
                            <option value="Club" <%= "Club".equalsIgnoreCase(currentOrg.getOrgType()) ? "selected" : ""%>>Student Club / Society</option>
                            <option value="NGO" <%= "NGO".equalsIgnoreCase(currentOrg.getOrgType()) ? "selected" : ""%>>Non-Governmental Organization (NGO)</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label for="address">Postal / Office Address</label></td>
                    <td>
                        <textarea id="address" name="orgAddress" rows="4" cols="40" required><%= currentOrg.getOrgAddress()%></textarea>
                    </td>
                </tr>
                <tr>
                    <td><label for="password">Account Password</label></td>
                    <td>
                        <input type="password" id="password" name="password" value="<%= currentOrg.getOrgPassword()%>" required>
                        <br><small>Password verification required to process updates.</small>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <button type="submit">Save Changes</button>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
