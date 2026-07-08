<%-- 
    Document   : org-history-events
    Created on : Jul 7, 2026, 10:19:39 AM
    Author     : Asus
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.organization" %>
<%@ page import="model.event" %>
<%@ page import="dao.EventDAO" %>
<%@ page import="dao.AttendanceDAO" %>
<%@ page import="java.util.List" %>

<%
    // 1. Session Guard Check: Redirect unauthorized users back to login
    organization currentOrg = (organization) session.getAttribute("currentOrg");
    if (currentOrg == null) {
        response.sendRedirect(request.getContextPath() + "/public/org-login.html");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>E-Sukarelawan - Organization Campaign History</title>
    </head>
    <style>
            @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap');
            :root {
                --c-primary: #4F46E5;
                --c-primary-dark: #3730A3;
                --c-primary-light: #EEF0FF;
                --c-gradient: linear-gradient(135deg, #4F46E5 0%, #7C6BF0 100%);
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
                font-family: "Poppins", system-ui, -apple-system, sans-serif;
                background: var(--c-bg);
                color: var(--c-text);
                line-height: 1.6;
            }

            /* ---- Top bar ---- */
            .topbar {
                background: var(--c-gradient);
                padding: 0 24px;
                display: flex;
                align-items: center;
                justify-content: space-between;
                min-height: 56px;
                position: sticky;
                top: 0;
                z-index: 100;
                box-shadow: 0 2px 12px rgba(0,0,0,0.1);
            }
            .topbar .brand {
                color: #fff;
                font-weight: 700;
                font-size: 1.05rem;
                text-decoration: none;
                display: flex;
                align-items: center;
                gap: 8px;
            }
            .topbar .brand svg {
                flex-shrink: 0;
            }
            .topbar .user-badge {
                background: rgba(255,255,255,0.15);
                backdrop-filter: blur(8px);
                color: #fff;
                font-size: 0.82rem;
                font-weight: 500;
                padding: 6px 14px;
                border-radius: 20px;
                border: 1px solid rgba(255,255,255,0.2);
            }

            /* ---- Nav ---- */
            .nav-bar {
                background: var(--c-surface);
                border-bottom: 1px solid var(--c-border);
                padding: 0 24px;
                overflow-x: auto;
                white-space: nowrap;
                box-shadow: 0 1px 4px rgba(0,0,0,0.03);
            }
            .nav-bar a {
                display: inline-block;
                padding: 14px 16px;
                font-size: 0.85rem;
                font-weight: 500;
                color: var(--c-muted);
                text-decoration: none;
                border-bottom: 2px solid transparent;
                transition: color 0.15s, border-color 0.15s;
            }
            .nav-bar a:hover {
                color: var(--c-primary);
                border-bottom-color: var(--c-primary);
            }
            .nav-bar a.active {
                color: var(--c-primary-dark);
                border-bottom-color: var(--c-primary-dark);
                font-weight: 600;
            }
            .nav-bar a.nav-logout {
                color: var(--c-danger);
            }
            .nav-bar a.nav-logout:hover {
                border-bottom-color: var(--c-danger);
            }

            /* ---- Page shell ---- */
            .page-wrap {
                max-width: 1100px;
                margin: 0 auto;
                padding: 28px 24px 64px;
            }
            .page-header {
                margin-bottom: 24px;
            }
            .page-header h1 {
                font-size: 1.5rem;
                font-weight: 700;
                margin-bottom: 4px;
                color: var(--c-text);
            }
            .page-header p {
                color: var(--c-muted);
                font-size: 0.9rem;
            }

            /* ---- Cards ---- */
            .card {
                background: var(--c-surface);
                border-radius: var(--radius);
                box-shadow: var(--shadow);
                padding: 28px;
                margin-bottom: 24px;
                border: 1px solid var(--c-border);
                transition: box-shadow 0.2s;
                overflow: hidden;
            }
            .card:hover {
                box-shadow: var(--shadow-hover);
            }
            .card-title {
                font-size: 1.1rem;
                font-weight: 700;
                margin-bottom: 20px;
                display: flex;
                align-items: center;
                gap: 8px;
                color: var(--c-text);
            }
            .card-title svg {
                color: var(--c-primary);
                flex-shrink: 0;
            }

            /* ---- Stat cards grid ---- */
            .stats-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
                gap: 16px;
                margin-bottom: 24px;
            }
            .stat-card {
                background: var(--c-surface);
                border-radius: var(--radius);
                box-shadow: var(--shadow);
                padding: 24px;
                border: 1px solid var(--c-border);
                text-align: center;
                transition: transform 0.2s, box-shadow 0.2s;
            }
            .stat-card:hover {
                transform: translateY(-3px);
                box-shadow: var(--shadow-hover);
            }
            .stat-value {
                font-size: 2rem;
                font-weight: 800;
                color: var(--c-primary);
                line-height: 1.2;
            }
            .stat-label {
                font-size: 0.8rem;
                font-weight: 600;
                color: var(--c-muted);
                text-transform: uppercase;
                letter-spacing: 0.5px;
                margin-top: 4px;
            }

            /* ---- Tables ---- */
            .table-wrap {
                overflow-x: auto;
                border-radius: var(--radius-sm);
                border: 1px solid var(--c-border);
            }
            table {
                width: 100%;
                border-collapse: collapse;
                font-size: 0.88rem;
            }
            table thead {
                background: var(--c-primary-light);
            }
            table thead th {
                padding: 12px 16px;
                text-align: left;
                font-weight: 600;
                color: var(--c-primary-dark);
                font-size: 0.8rem;
                text-transform: uppercase;
                letter-spacing: 0.5px;
                white-space: nowrap;
            }
            table tbody td {
                padding: 12px 16px;
                border-top: 1px solid var(--c-border);
                vertical-align: middle;
            }
            table tbody tr {
                transition: background 0.1s;
            }
            table tbody tr:hover {
                background: var(--c-primary-light);
            }

            /* ---- Badges ---- */
            .badge {
                display: inline-flex;
                align-items: center;
                gap: 4px;
                padding: 4px 12px;
                border-radius: 20px;
                font-size: 0.78rem;
                font-weight: 600;
                white-space: nowrap;
            }
            .badge-finished {
                background: var(--c-primary-light);
                color: var(--c-primary-dark);
            }

            /* ---- Footer ---- */
            .footer {
                text-align: center;
                padding: 16px;
                font-size: 0.78rem;
                color: var(--c-muted);
                margin-top: 24px;
                border-top: 1px solid var(--c-border);
            }

            /* ---- Responsive ---- */
            @media (max-width: 640px) {
                .topbar {
                    padding: 0 16px;
                }
                .nav-bar {
                    padding: 0 16px;
                }
                .page-wrap {
                    padding: 20px 16px 48px;
                }
                .card {
                    padding: 20px 16px;
                }
                .stats-grid {
                    grid-template-columns: 1fr;
                }
            }
    </style>
    <body>
        <div class="topbar">
            <a class="brand" href="org-dashboard.jsp">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
                </svg> 
                E-Sukarelawan
            </a>
            <span class="user-badge">Organization Portal</span>
        </div>

        <div class="nav-bar">
            <a href="org-dashboard.jsp">Dashboard</a>
            <a href="org-profile.jsp">Profile</a>
            <a href="org-manage-events.jsp">Campaigns</a>
            <a href="org-create-event.jsp">Create Event</a>
            <a href="org-history.jsp" class="active">History</a> <!-- New Active Tab -->
            <a href="org-view-attendance.jsp">Attendance</a>
            <a href="../LogoutServlet" class="nav-logout">Logout</a>
        </div>

        <div class="page-wrap">
            <div class="page-header">
                <h1>Campaign History Archive</h1>
                <p>Audited records of completed volunteer achievements and verified student hours</p>
            </div>

            <%
                int finishedCount = 0;
                double historicalHoursSum = 0.0;
                List<event> finishedEvents = null;

                try {
                    EventDAO eventDAO = new EventDAO();
                    AttendanceDAO attendanceDAO = new AttendanceDAO();

                    // Call the dedicated new finished events method
                    finishedEvents = eventDAO.getFinishedEventsByOrgId(currentOrg.getOrgId());

                    if (finishedEvents != null) {
                        finishedCount = finishedEvents.size();
                        for (event ev : finishedEvents) {
                            int verifiedCount = 0;
                            List<model.registration> regs = attendanceDAO.getAttendanceByEventId(ev.getEventId());
                            if (regs != null) {
                                for (model.registration r : regs) {
                                    if ("Verified".equalsIgnoreCase(r.getAttendanceStatus())) {
                                        verifiedCount++;
                                    }
                                }
                            }
                            historicalHoursSum += (verifiedCount * ev.getEventHour());
                        }
                    }
                } catch (Exception ex) {
                    out.println("<div class='alert-error'>Error compiling campaign statistics: " + ex.getMessage() + "</div>");
                }
            %>

            <!-- History Metrics Dashboard Cards -->
            <div class="stats-grid">
                <div class="stat-card" style="border-left: 4px solid var(--c-muted);">
                    <div class="stat-value"><%= finishedCount%></div>
                    <div class="stat-label">Total Completed Campaigns</div>
                </div>
                <div class="stat-card" style="border-left: 4px solid var(--c-success);">
                    <div class="stat-value" style="color: var(--c-success);"><%= String.format("%.2f", historicalHoursSum)%></div>
                    <div class="stat-label">Completed Verification Impact (Hrs)</div>
                </div>
            </div>

            <!-- Historical Campaigns Database Table -->
            <div class="card">
                <div class="card-title">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="margin-right: 8px;">
                    <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                    <polyline points="22 4 12 14.01 9 11.01"/>
                    </svg>
                    Archived Finished Campaigns Log
                </div>
                <div class="table-wrap">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Campaign Name</th>
                                <th>Date Completed</th>
                                <th>Location</th>
                                <th>Hours Offered</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (finishedEvents == null || finishedEvents.isEmpty()) {
                            %>
                            <tr>
                                <td colspan="6" style="text-align:center;padding:32px;color:var(--c-muted);">
                                    No historical completed campaigns found for your organization.
                                </td>
                            </tr>
                            <%
                            } else {
                                for (event ev : finishedEvents) {
                            %>
                            <tr>
                                <td><%= ev.getEventId()%></td>
                                <td><strong><%= ev.getEventName()%></strong></td>
                                <td><%= ev.getEventDate()%></td>
                                <td><%= ev.getLocation()%></td>
                                <td><strong><%= String.format("%.2f", ev.getEventHour())%> hrs</strong></td>
                                <td><span class="badge badge-finished">Finished</span></td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="footer">E-Sukarelawan Organization Space &copy; 2026</div>
        </div>
    </body>
</html>
