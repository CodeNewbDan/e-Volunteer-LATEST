<%-- 
    Document   : v-profile
    Created on : Jun 29, 2026, 9:22:35 PM
    Author     : syahm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.volunteer" %>
<%!
    // Safe HTML Escaper to prevent XSS injection issues on dynamic DB renders
    public String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }
%>

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
        <title>My Profile - E-Sukarelawan</title>
    </head>
    <body>
        <h1>Student Volunteer Profile</h1>
        <p>Manage your account credentials and personal information.</p>

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

        <%
            String status = request.getParameter("status");
            if ("success".equals(status)) {
                out.println("<p style='color:green;'><strong>Profile details successfully updated and saved to DB!</strong></p><hr>");
            } else if ("error".equals(status)) {
                out.println("<p style='color:red;'><strong>Failed to update profile. Please verify database constraints.</strong></p><hr>");
            }
        %>

        <!-- Triggers UpdateVolunteerProfileServlet, note the relative action pathway step-up -->
        <form action="../UpdateVolunteerProfileServlet" method="POST">
            <table border="1" cellpadding="8">
                <tr>
                    <td><strong>StudentID (Read-Only)</strong></td>
                    <td>
                        <input type="text" value="<%= currentVolunteer.getStudentId()%>" disabled>
                        <small>Contact administration to alter academic IDs.</small>
                    </td>
                </tr>
                <tr>
                    <td><strong>Cumulative Hours (Read-Only)</strong></td>
                    <td>
                        <strong><%= currentVolunteer.getTotalHours()%> Hours</strong>
                    </td>
                </tr>
                <tr>
                    <td><label for="fullName">Full Name</label></td>
                    <td>
                        <input type="text" id="fullName" name="fullName" value="<%= currentVolunteer.getFullName()%>" required style="width: 300px;">
                    </td>
                </tr>
                <tr>
                    <td><label for="volunteerEmail">Official Email</label></td>
                    <td>
                        <input type="email" id="volunteerEmail" name="volunteerEmail" value="<%= currentVolunteer.getVolunteerEmail()%>" required style="width: 300px;">
                    </td>
                </tr>
                <tr>
                    <td><label for="phoneNum">Phone Number (Numbers Only)</label></td>
                    <td>
                        <input type="number" id="phoneNum" name="phoneNum" value="<%= currentVolunteer.getPhoneNum()%>" required>
                    </td>
                </tr>
                <tr>
                    <td><label for="course">Course / Major Programme</label></td>
                    <td>
                        <input type="text" id="course" name="course" value="<%= currentVolunteer.getCourse()%>" required style="width: 300px;">
                    </td>
                </tr>
                <tr>
                    <td><label for="volunteerAddress">Home / Residential Address</label></td>
                    <td>
                        <textarea id="volunteerAddress" name="volunteerAddress" rows="4" cols="40" required><%= currentVolunteer.getVolunteerAddress()%></textarea>
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
                        <button type="submit">Save Profile Changes</button>
                        <button type="reset">Reset Fields</button>
                    </td>
                </tr>
            </table>
        </form>
        <script>
        // Client-side quick-validation checker
        function validateProfileForm() {
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
