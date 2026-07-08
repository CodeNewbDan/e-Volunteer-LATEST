package dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author hansz
 */
import model.event;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    private static final String URL = "jdbc:derby://localhost:1527/eVolunteer";
    private static final String USER = "app";
    private static final String PASS = "app";

    public EventDAO() {

    }

    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public boolean createEvent(event event) {
        String sql = "INSERT INTO Event (OrganizationID, EventName, EventDate, Location, EventStatus, RequiredVolunteers, TaskDescription, EventVolunteerHour, SecretCode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, event.getOrgId());
            ps.setString(2, event.getEventName());

            // Safe conversion of HTML Datepicker String (YYYY-MM-DD) to java.sql.Date for Derby
            ps.setDate(3, java.sql.Date.valueOf(event.getEventDate()));

            ps.setString(4, event.getLocation());
            ps.setString(5, event.getStatus());
            ps.setInt(6, event.getNumOfVolunteer());
            ps.setString(7, event.getTaskDesc());
            ps.setDouble(8, event.getEventHour());
            ps.setString(9, event.getSecretCode());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public event getEventById(int id) {
        String sql = "SELECT * FROM Event WHERE EventID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            autoUpdateEventStatuses(conn);
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapEvent(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<event> getAllEvents() {
        List<event> event = new ArrayList<>();
        String sql = "SELECT * FROM Event";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            autoUpdateEventStatuses(conn);
            while (rs.next()) {
                event.add(mapEvent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return event;
    }

    public List<event> getEventsByOrgId(int orgId) {
        List<event> event = new ArrayList<>();
        String sql = "SELECT * FROM Event WHERE OrganizationId = ? AND EventStatus = 'Active' ORDER BY EventDate ASC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            // Clean up status first
            autoUpdateEventStatuses(conn);

            ps.setInt(1, orgId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                event.add(mapEvent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return event;
    }

    public List<event> getFinishedEventsByOrgId(int orgId) {
        List<event> events = new ArrayList<>();
        // Run date check self-healing logic

        String sql = "SELECT * FROM Event WHERE OrganizationID = ? AND EventStatus = 'Finished' ORDER BY EventDate DESC";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            autoUpdateEventStatuses(conn);
            ps.setInt(1, orgId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    event event = new event();
                    event.setEventId(rs.getInt("EventID"));
                    event.setOrgId(rs.getInt("OrganizationID"));
                    event.setEventName(rs.getString("EventName"));
                    event.setEventDate(rs.getString("EventDate"));
                    event.setLocation(rs.getString("Location"));
                    event.setStatus(rs.getString("EventStatus"));
                    event.setNumOfVolunteer(rs.getInt("RequiredVolunteers"));
                    event.setTaskDesc(rs.getString("TaskDescription"));
                    event.setEventHour(rs.getDouble("EventVolunteerHour"));
                    event.setSecretCode(rs.getString("SecretCode"));

                    events.add(event);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public boolean updateEvent(event event) {
        String sql = "UPDATE Event SET EventName = ?, EventDate = ?, Location = ?, RequiredVolunteers = ?, TaskDescription = ?, EventVolunteerHour = ?, SecretCode = ?, EventStatus = ? WHERE EventID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, event.getEventName());
            ps.setDate(2, java.sql.Date.valueOf(event.getEventDate()));
            ps.setString(3, event.getLocation());
            ps.setInt(4, event.getNumOfVolunteer());
            ps.setString(5, event.getTaskDesc());
            ps.setDouble(6, event.getEventHour());
            ps.setString(7, event.getSecretCode());
            ps.setString(8, event.getStatus());
            ps.setInt(9, event.getEventId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void autoUpdateEventStatuses(Connection conn) {
        String sql = "UPDATE Event SET EventStatus = 'Finished' "
                + "WHERE EventDate < CURRENT_DATE AND EventStatus = 'Active'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            // Log quietly to prevent console pollution while saving system safety
            System.err.println("[E-Sukarelawan EventDAO Exception]: Status Auto-update failed.");
            e.printStackTrace();
        }
    }

    public boolean deleteEvent(int eventId) {
        String sql = "DELETE FROM Event WHERE EventID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private event mapEvent(ResultSet rs) throws SQLException {
        event ev = new event();
        ev.setEventId(rs.getInt("EventID"));
        ev.setOrgId(rs.getInt("OrganizationID"));
        ev.setEventName(rs.getString("EventName"));

        // Converts SQL Date dynamically into java.lang.String property format (YYYY-MM-DD)
        if (rs.getDate("EventDate") != null) {
            ev.setEventDate(rs.getDate("EventDate").toString());
        }

        ev.setLocation(rs.getString("Location"));
        ev.setStatus(rs.getString("EventStatus"));
        ev.setNumOfVolunteer(rs.getInt("RequiredVolunteers"));
        ev.setTaskDesc(rs.getString("TaskDescription"));
        ev.setEventHour(rs.getDouble("EventVolunteerHour"));
        ev.setSecretCode(rs.getString("SecretCode"));
        return ev;
    }

    public List<event> getUnregisteredEventsForVolunteer(int volunteerId) {
        List<event> list = new ArrayList<>();
        String sql = "SELECT * FROM Event WHERE EventStatus = 'Active' AND EventID NOT IN ("
                + "  SELECT EventID FROM Registration WHERE VolunteerID = ?"
                + ") ORDER BY EventDate ASC";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            autoUpdateEventStatuses(conn);
            ps.setInt(1, volunteerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapEvent(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
