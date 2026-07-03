<%-- 
    Document   : org-profile
    Created on : Jun 29, 2026, 9:24:35 PM
    Author     : syahm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.organization" %>

<%!
    // Safe HTML Escaper to prevent XSS injection issues on dynamic DB renders
    public String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
%>

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

        <!-- Context Status Message Blocks -->
        <%
            String status = request.getParameter("status");
            if ("success".equals(status)) {
                out.println("<p style='color:green;'><strong>Profile details successfully updated and saved!</strong></p><hr>");
            } else if ("wrong_password".equals(status)) {
                out.println("<p style='color:red;'><strong>Verification Failed: The Current Password entered was incorrect. Changes refused.</strong></p><hr>");
            } else if ("password_mismatch".equals(status)) {
                out.println("<p style='color:red;'><strong>Validation Failed: New Password and Confirm New Password inputs do not match!</strong></p><hr>");
            } else if ("missing_fields".equals(status)) {
                out.println("<p style='color:red;'><strong>Validation Failed: Please fill in all required fields.</strong></p><hr>");
            } else if ("error".equals(status)) {
                out.println("<p style='color:red;'><strong>System Error: Could not update organization profile in database.</strong></p><hr>");
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
                <!-- SECTION: SECURE PASSWORDS UPDATE FIELDS -->
                <tr style="background-color: #f2f2f2;">
                    <td colspan="2"><strong>Credential Updates (Optional)</strong><br><small>Only fill in below if you wish to change your active login password.</small></td>
                </tr>
                <tr>
                    <td><label for="newPassword">New Password:</label></td>
                    <td>
                        <input type="password" id="newPassword" name="newPassword" placeholder="Leave blank to keep current">
                    </td>
                </tr>
                <tr>
                    <td><label for="confirmNewPassword">Confirm New Password:</label></td>
                    <td>
                        <input type="password" id="confirmNewPassword" name="confirmNewPassword" placeholder="Leave blank to keep current">
                    </td>
                </tr>

                <!-- SECTION: SECURITY IDENTITY AUTHORIZATION (MANDATORY ROW) -->
                <tr style="background-color: #fff9f9;">
                    <td><label for="currentPassword" style="color:red; font-weight:bold;">CURRENT PASSWORD:</label></td>
                    <td>
                        <input type="password" id="currentPassword" name="currentPassword" required>
                        <br><small style="color:red; font-weight:bold;">Required to authorize and authenticate these profile modifications.</small>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <button type="submit">Save Changes</button>
                        <button type="reset">Reset Fields</button>
                    </td>
                </tr>
            </table>
        </form>
                    
        <br>
        <hr style="border: 1px dashed red;">
        <h3 style="color: red;">Organization Account Details</h3>
        <p>Permanently deleting your organization will immediately terminate access and drop all hosted campaigns, upcoming event schedules, and active student registration history from our records. <b>This action cannot be undone.</b></p>

        <form action="../DeleteOrgServlet" method="POST" onsubmit="return confirm('Are you completely sure you want to permanently delete your Organization? All events, schedules, and active rosters will be cascade wiped!');">
            <table border="0" style="background-color: #fff6f6; border: 1px solid red; padding: 10px;">
                <tr>
                    <td>Verify Password to Confirm Permanent Deletion:</td>
                    <td><input type="password" name="deletePassword" required placeholder="Verify profile password"></td>
                    <td><input type="submit" value="Permanently Delete My Organization" style="color: red; font-weight: bold;"></td>
                </tr>
            </table>
        </form>
        
        <script>
            // Client-side quick-validation checker
            function validateOrgProfileForm() {
                const currentPass = document.getElementById("currentPassword").value.trim();
                const newPass = document.getElementById("newPassword").value;
                const confirmNewPass = document.getElementById("confirmNewPassword").value;

                if (currentPass === "") {
                    alert("Please enter your current password to authorize updates.");
                    return false;
                }

                if (newPass !== "" || confirmNewPass !== "") {
                    if (newPass !== confirmNewPass) {
                        alert("Validation Error: New Password and Confirm New Password fields do not match!");
                        return false;
                    }
                }
                return true;
            }
        </script>
    </body>
</html>
