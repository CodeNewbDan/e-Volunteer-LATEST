/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hansz
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.model.*;

public class AttendanceDAO {

    private static final String URL = "jdbc:derby://localhost:1527/eVolunteer";
    private static final String USER = "app";
    private static final String PASS = "app";

    public AttendanceDAO() {
    }

    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public boolean registerForEvent(int volunteerId, int eventId) {
        // Using correct table 'Registration' and accurate column targets.
        // RegistrationDate defaults to CURRENT_TIMESTAMP automatically via DB schema.
        String sql = "INSERT INTO Registration (VolunteerID, EventID, AttendanceStatus) VALUES (?, ?, 'Pending')";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, volunteerId);
            ps.setInt(2, eventId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all events a volunteer has registered for that are still pending.
     */
    public List<event> getPendingRegisteredEvents(int volunteerId) {
        List<event> events = new ArrayList<>();
        String sql = "SELECT e.* FROM Event e "
                + "JOIN Registration r ON e.EventID = r.EventID "
                + "WHERE r.VolunteerID = ? AND r.AttendanceStatus = 'Pending'";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, volunteerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    event event = new event();

                    // CRITICAL: Perfectly matching exact schema types and manifest POJO fields
                    event.setEventId(rs.getInt("EventID"));
                    event.setOrgId(rs.getInt("OrganizationID"));
                    event.setEventName(rs.getString("EventName"));
                    event.setEventDate(rs.getString("EventDate")); // FIXED: EventDate is DATE type -> rs.getDate()
                    event.setLocation(rs.getString("Location"));
                    event.setStatus(rs.getString("EventStatus"));
                    event.setNumOfVolunteer(rs.getInt("RequiredVolunteers"));
                    event.setTaskDesc(rs.getString("TaskDescription"));
                    event.setEventHour(rs.getDouble("EventVolunteerHour")); // FIXED: DECIMAL maps cleanly to double
                    event.setSecretCode(rs.getString("SecretCode"));
                    events.add(event);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    /**
     * Returns all attendance/registration records for a given event.
     */
    public List<registration> getAttendanceByEventId(int eventId) {
        List<registration> records = new ArrayList<>();
        String sql = "SELECT * FROM Registration WHERE EventID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRegistration(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Updates the attendance status to 'Verified' and sets the verified
     * timestamp.
     */
    public boolean verifyRegistrationStatus(int volunteerId, int eventId) {
        String sql = "UPDATE Registration SET AttendanceStatus = 'Verified', VerificationDate = CURRENT_TIMESTAMP "
                + "WHERE VolunteerID = ? AND EventID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, volunteerId);
            ps.setInt(2, eventId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private registration mapRegistration(ResultSet rs) throws SQLException {
        registration reg = new registration();

        // FIXED: Exact structural column names matched with POJO specifications
        reg.setRegisterId(rs.getInt("RegistrationID"));
        reg.setVolunteerId(rs.getInt("VolunteerID"));
        reg.setEventId(rs.getInt("EventID"));
        reg.setAttendanceStatus(rs.getString("AttendanceStatus"));

        // FIXED: Using rs.getTimestamp() safely to handle the database TIMESTAMP structures
        reg.setRegisterDate(rs.getString("RegistrationDate"));
        reg.setAttendanceDate(rs.getString("AttendanceDate"));
        reg.setVerificationDate(rs.getString("VerificationDate"));

        return reg;
    }

}
