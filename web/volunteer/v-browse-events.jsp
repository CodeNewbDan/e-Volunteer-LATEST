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
        <title>Browse Campaigns - E-Sukarelawan</title>
        <style>
            @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap');
            :root {
                --c-primary: #00A99D;
--c-primary-dark: #00877D;
--c-primary-light: #E5F7F5;
                --c-gradient: linear-gradient(135deg, #00A99D 0%, #00C9B7 100%);
                --c-bg: #F7F8FA;
--c-surface: #FFFFFF;
--c-text: #1A1A1A;
                --c-muted: #6B7280;
--c-border: #E5E7EB;
                --c-success: #22C55E;
--c-success-bg: #ECFDF5;
                --c-danger: #EF4444;
--c-danger-bg: #FEF2F2;
                --c-warning: #F59E0B;
--c-warning-bg: #FFFBEB;
                --c-info: #3B82F6;
--c-info-bg: #EFF6FF;
                --radius: 16px;
--radius-sm: 12px;
--radius-xs: 8px;
                --shadow: 0 4px 24px rgba(0,0,0,0.06);
                --shadow-hover: 0 8px 32px rgba(0,0,0,0.10);
            }
            * {
box-sizing: border-box;
margin: 0;
padding: 0;
}
            body {
font-family:"Poppins",system-ui,-apple-system,sans-serif;
background:var(--c-bg);
color:var(--c-text);
line-height:1.6;
}

            /* ---- Top bar ---- */
            .topbar {
background:var(--c-gradient);
padding:0 24px;
display:flex;
align-items:center;
justify-content:space-between;
min-height:56px;
position:sticky;
top:0;
z-index:100;
box-shadow:0 2px 12px rgba(0,0,0,0.1);
}
            .topbar .brand {
color:#fff;
font-weight:700;
font-size:1.05rem;
text-decoration:none;
display:flex;
align-items:center;
gap:8px;
}
            .topbar .brand svg {
flex-shrink:0;
}
            .topbar .user-badge {
background:rgba(255,255,255,0.15);
backdrop-filter:blur(8px);
color:#fff;
font-size:0.82rem;
font-weight:500;
padding:6px 14px;
border-radius:20px;
border:1px solid rgba(255,255,255,0.2);
}

            /* ---- Nav ---- */
            .nav-bar {
background:var(--c-surface);
border-bottom:1px solid var(--c-border);
padding:0 24px;
overflow-x:auto;
white-space:nowrap;
box-shadow:0 1px 4px rgba(0,0,0,0.03);
}
            .nav-bar a {
display:inline-block;
padding:14px 16px;
font-size:0.85rem;
font-weight:500;
color:var(--c-muted);
text-decoration:none;
border-bottom:2px solid transparent;
transition:color 0.15s, border-color 0.15s;
}
            .nav-bar a:hover {
color:var(--c-primary);
border-bottom-color:var(--c-primary);
}
            .nav-bar a.active {
color:var(--c-primary-dark);
border-bottom-color:var(--c-primary-dark);
font-weight:600;
}
            .nav-bar a.nav-logout {
color:var(--c-danger);
}
            .nav-bar a.nav-logout:hover {
border-bottom-color:var(--c-danger);
}

            /* ---- Page shell ---- */
            .page-wrap {
max-width:1100px;
margin:0 auto;
padding:28px 24px 64px;
}
            .page-header {
margin-bottom:24px;
}
            .page-header h1 {
font-size:1.5rem;
font-weight:700;
margin-bottom:4px;
}
            .page-header p {
color:var(--c-muted);
font-size:0.9rem;
}

            /* ---- Cards ---- */
            .card {
background:var(--c-surface);
border-radius:var(--radius);
box-shadow:var(--shadow);
padding:28px;
margin-bottom:20px;
border:1px solid var(--c-border);
transition:box-shadow 0.2s;
}
            .card:hover {
box-shadow:var(--shadow-hover);
}
            .card-title {
font-size:1.05rem;
font-weight:700;
margin-bottom:4px;
display:flex;
align-items:center;
gap:8px;
}
            .card-title svg {
color:var(--c-primary);
flex-shrink:0;
}
            .card-subtitle {
font-size:0.82rem;
color:var(--c-muted);
margin-bottom:16px;
}

            /* ---- Stat cards grid ---- */
            .stats-grid {
display:grid;
grid-template-columns:repeat(auto-fit, minmax(220px,1fr));
gap:16px;
margin-bottom:24px;
}
            .stat-card {
background:var(--c-surface);
border-radius:var(--radius);
box-shadow:var(--shadow);
padding:24px;
border:1px solid var(--c-border);
text-align:center;
transition:transform 0.2s, box-shadow 0.2s;
}
            .stat-card:hover {
transform:translateY(-3px);
box-shadow:var(--shadow-hover);
}
            .stat-value {
font-size:2rem;
font-weight:800;
color:var(--c-primary);
line-height:1.2;
}
            .stat-label {
font-size:0.8rem;
font-weight:600;
color:var(--c-muted);
text-transform:uppercase;
letter-spacing:0.5px;
margin-top:4px;
}

            /* ---- Tables ---- */
            .table-wrap {
overflow-x:auto;
border-radius:var(--radius-sm);
border:1px solid var(--c-border);
}
            table {
width:100%;
border-collapse:collapse;
font-size:0.88rem;
}
            table thead {
background:var(--c-primary-light);
}
            table thead th {
padding:12px 16px;
text-align:left;
font-weight:600;
color:var(--c-primary-dark);
font-size:0.8rem;
text-transform:uppercase;
letter-spacing:0.5px;
white-space:nowrap;
}
            table tbody td {
padding:12px 16px;
border-top:1px solid var(--c-border);
vertical-align:middle;
}
            table tbody tr {
transition:background 0.1s;
}
            table tbody tr:hover {
background:var(--c-primary-light);
}
            table tbody tr.highlight {
background:#FFFDE7;
}

            /* ---- Badges ---- */
            .badge {
display:inline-flex;
align-items:center;
gap:4px;
padding:4px 12px;
border-radius:20px;
font-size:0.78rem;
font-weight:600;
white-space:nowrap;
}
            .badge-success {
background:var(--c-success-bg);
color:#15803D;
}
            .badge-warning {
background:var(--c-warning-bg);
color:#92400E;
}
            .badge-danger  {
background:var(--c-danger-bg);
color:#991B1B;
}
            .badge-info    {
background:var(--c-info-bg);
color:#1E40AF;
}
            .badge-rank    {
background:var(--c-gradient);
color:#fff;
font-weight:700;
min-width:32px;
justify-content:center;
}

            /* ---- Alerts ---- */
            .alert {
padding:14px 18px;
border-radius:var(--radius-sm);
font-size:0.88rem;
margin-bottom:16px;
display:flex;
align-items:center;
gap:10px;
border:1px solid transparent;
animation:slideDown 0.3s ease;
}
            .alert-success {
background:var(--c-success-bg);
border-color:var(--c-success);
color:#15803D;
}
            .alert-error   {
background:var(--c-danger-bg);
border-color:var(--c-danger);
color:#991B1B;
}
            .alert-info    {
background:var(--c-info-bg);
border-color:var(--c-info);
color:#1E40AF;
}
            @keyframes slideDown {
from {
opacity:0;
transform:translateY(-8px);
}
to {
opacity:1;
transform:translateY(0);
}
}

            /* ---- Forms ---- */
            .form-group {
margin-bottom:20px;
}
            .form-group label {
display:block;
font-size:0.82rem;
font-weight:600;
color:var(--c-muted);
margin-bottom:6px;
text-transform:uppercase;
letter-spacing:0.5px;
}
            .form-group .hint {
font-size:0.78rem;
color:var(--c-muted);
margin-top:4px;
}

            form table {
border:none !important;
}
            form table td {
border:none !important;
padding:0;
}
            form table tr {
display:block;
margin-bottom:18px;
}
            form table td {
display:block;
width:100%;
}
            form table td:first-child {
margin-bottom:6px;
}
            form table td:first-child strong,
            form table td label {
display:block;
font-size:0.82rem;
font-weight:600;
color:var(--c-muted);
text-transform:uppercase;
letter-spacing:0.5px;
}

            input[type="text"], input[type="email"], input[type="password"],
            input[type="number"], input[type="date"], select, textarea {
                width:100%;
font-family:inherit;
font-size:0.92rem;
                padding:12px 16px;
border:2px solid var(--c-border);
                border-radius:var(--radius-sm);
background:var(--c-surface);
                color:var(--c-text);
transition:border-color 0.2s, box-shadow 0.2s;
            }
            input:focus, select:focus, textarea:focus {
                outline:none;
border-color:var(--c-primary);
                box-shadow:0 0 0 4px var(--c-primary-light);
            }
            input:disabled {
background:var(--c-bg);
color:var(--c-muted);
cursor:not-allowed;
opacity:0.7;
}
            textarea {
resize:vertical;
min-height:90px;
}
            select {
                appearance:none;
                background-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='%236B7280' stroke-width='2'%3E%3Cpolyline points='6 9 12 15 18 9'/%3E%3C/svg%3E");
                background-repeat:no-repeat;
background-position:right 14px center;
padding-right:40px;
            }
            small {
font-size:0.78rem;
color:var(--c-muted);
}

            /* ---- Buttons ---- */
            .btn {
appearance:none;
display:inline-flex;
align-items:center;
gap:6px;
border:none;
font-family:inherit;
font-weight:600;
font-size:0.9rem;
padding:11px 22px;
border-radius:var(--radius-sm);
cursor:pointer;
transition:transform 0.1s, box-shadow 0.2s;
text-decoration:none;
}
            .btn-primary {
background:var(--c-gradient);
color:#fff;
box-shadow:0 4px 14px rgba(0,169,157,0.3);
}
            .btn-primary:hover {
transform:translateY(-1px);
box-shadow:0 6px 20px rgba(0,169,157,0.4);
}
            .btn-primary:active {
transform:translateY(0) scale(0.99);
}

            button[type="submit"] {
appearance:none;
border:none;
background:var(--c-gradient);
color:#fff;
font-family:inherit;
font-weight:600;
font-size:0.92rem;
padding:12px 24px;
border-radius:var(--radius-sm);
cursor:pointer;
transition:transform 0.1s, box-shadow 0.2s;
box-shadow:0 4px 14px rgba(0,169,157,0.3);
}
            button[type="submit"]:hover {
transform:translateY(-1px);
box-shadow:0 6px 20px rgba(0,169,157,0.4);
}
            button[type="submit"]:active {
transform:translateY(0) scale(0.99);
}
            button[type="reset"], .btn-outline {
appearance:none;
background:transparent;
color:var(--c-muted);
border:2px solid var(--c-border);
border-radius:var(--radius-sm);
padding:10px 20px;
font-family:inherit;
font-weight:600;
font-size:0.9rem;
cursor:pointer;
transition:border-color 0.15s, color 0.15s;
}
            button[type="reset"]:hover, .btn-outline:hover {
border-color:var(--c-muted);
color:var(--c-text);
}
            .btn-row {
display:flex;
gap:10px;
flex-wrap:wrap;
}
            .btn-sm {
padding:7px 14px;
font-size:0.82rem;
border-radius:var(--radius-xs);
}
            .btn-danger {
background:var(--c-danger);
color:#fff;
border:none;
}
            .btn-danger:hover {
background:#DC2626;
}
            input[type="submit"] {
appearance:none;
font-family:inherit;
font-weight:600;
font-size:0.88rem;
padding:10px 20px;
border-radius:var(--radius-sm);
cursor:pointer;
border:2px solid var(--c-border);
background:var(--c-surface);
color:var(--c-text);
transition:all 0.15s;
}
            input[type="submit"]:hover {
border-color:var(--c-primary);
color:var(--c-primary);
}

            /* ---- Links ---- */
            a {
color:var(--c-primary);
text-decoration:none;
font-weight:500;
}
            a:hover {
color:var(--c-primary-dark);
text-decoration:underline;
}

            /* ---- Danger zone ---- */
            .danger-zone {
border:2px solid var(--c-danger);
border-radius:var(--radius);
padding:24px;
margin-top:24px;
background:var(--c-danger-bg);
}
            .danger-zone h3 {
color:var(--c-danger);
font-size:1rem;
margin-bottom:8px;
display:flex;
align-items:center;
gap:8px;
}
            .danger-zone p {
font-size:0.88rem;
color:#991B1B;
margin-bottom:16px;
}
            .danger-zone .del-row {
display:flex;
gap:10px;
align-items:center;
flex-wrap:wrap;
}
            .danger-zone input[type="password"] {
max-width:280px;
}
            .danger-zone input[type="submit"] {
color:var(--c-danger);
border-color:var(--c-danger);
font-weight:700;
}
            .danger-zone input[type="submit"]:hover {
background:var(--c-danger);
color:#fff;
}

            /* ---- Section sep ---- */
            .section-sep {
margin:28px 0;
border:none;
border-top:1px solid var(--c-border);
}

            /* ---- Footer ---- */
            .footer {
text-align:center;
padding:16px;
font-size:0.78rem;
color:var(--c-muted);
}

            /* ---- Code ---- */
            code {
background:var(--c-bg);
padding:3px 8px;
border-radius:6px;
font-size:0.85rem;
font-family:"Fira Code",monospace;
border:1px solid var(--c-border);
}

            /* ---- Responsive ---- */
            @media (max-width:640px) {
                .topbar {
padding:0 16px;
}
                .nav-bar {
padding:0 16px;
}
                .page-wrap {
padding:20px 16px 48px;
}
                .card {
padding:20px 16px;
}
                .stats-grid {
grid-template-columns:1fr;
}
                .danger-zone .del-row {
flex-direction:column;
}
            }
        </style>
    </head>
    <body>
        <div class="topbar">
            <a class="brand" href="v-dashboard.jsp"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg> E-Sukarelawan</a>
            <span class="user-badge">Student Portal</span>
        </div>
        <div class="nav-bar">
            <a href="v-dashboard.jsp">Dashboard</a>
            <a href="v-profile.jsp">My Profile</a>
            <a href="v-browse-events.jsp" class="active">Browse Events</a>
            <a href="v-verify-attendance.jsp">Verify Attendance</a>
            <a href="v-leaderboard.jsp">Leaderboard</a>
            <a href="../LogoutServlet" class="nav-logout">Logout</a>
        </div>
        <div class="page-wrap">
            <div class="page-header">
                <h1>Active University Volunteer Calls</h1>
                <p>Review and join ongoing CSR campaigns to earn social service hour credits.</p>
            </div>

            <%
                String status = request.getParameter("status");
                String error = request.getParameter("error");
                if ("event_not_found".equals(error)) {
                    out.println("<div class=\"alert alert-error\"><strong>&#10007; Event Not Found!</strong></div>");
                } else if ("registered".equals(status)) {
                    out.println("<div class=\"alert alert-success\"><strong>&#10003; Successfully registered for the event!</strong></div>");
                } else if ("registration_failed_or_already_registered".equals(status)) {
                    out.println("<div class=\"alert alert-error\"><strong>&#10007; Already registered or registration failed.</strong></div>");
                } else if ("event_full".equals(error)) {
                    out.println("<div class=\"alert alert-error\"><strong>&#10007; Sorry, this event is already full!</strong></div>");
                }
            %>

            <div class="card">
                <div class="card-title"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg> Available Campaigns</div>
                <div class="card-subtitle">Click "Join" to register for an event</div>
                <div class="table-wrap">
                    <table><thead><tr><th>Event Name</th><th>Date</th><th>Location</th><th>Task</th><th>Vacancies</th><th>Credits</th><th>Action</th></tr></thead>
                        <tbody>
                            <%
                                try {
                                    EventDAO eventDAO = new EventDAO();
                                    List<event> activeEvents = eventDAO.getUnregisteredEventsForVolunteer(currentVolunteer.getVolunteerId());
                                    if (activeEvents == null || activeEvents.isEmpty()) {
                            %>
                            <tr><td colspan="7" style="text-align:center;padding:24px;color:var(--c-muted);">No new campaigns open right now. Check back later!</td></tr>
                            <% } else {
                            for (event ev : activeEvents) {%>
                            <tr>
                                <td><strong><%= escapeHtml(ev.getEventName())%></strong></td>
                                <td><%= escapeHtml(ev.getEventDate())%></td>
                                <td><%= escapeHtml(ev.getLocation())%></td>
                                <td><%= escapeHtml(ev.getTaskDesc())%></td>
                                <td><span class="badge badge-info"><%= ev.getNumOfVolunteer()%> seats</span></td>
                                <td><span class="badge badge-success"><%= ev.getEventHour()%> Hrs</span></td>
                                <td>
                                    <form action="../RegisterEventServlet" method="POST" style="display:inline;">
                                        <input type="hidden" name="eventId" value="<%= ev.getEventId()%>">
                                        <button type="submit" class="btn btn-primary btn-sm">Join</button>
                                    </form>
                                </td>
                            </tr>
                            <% }
                            }
                        } catch (Exception ex) {%>
                            <tr><td colspan="7" style="color:var(--c-danger);">Error: <%= ex.getMessage()%></td></tr>
                            <% }%>
                        </tbody></table>
                </div>
            </div>
            <div class="footer">E-Sukarelawan System &copy; 2026</div>
        </div>
    </body>
</html>
