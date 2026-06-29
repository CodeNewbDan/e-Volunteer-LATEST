package dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.model.event;

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
        String sql = "INSERT INTO Event (eventName, date, location, numOfVolunteer, desc, eventHour, secretCode) VALUES (?, ?, ?, ? ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, event.getEventName());
            ps.setString(2, event.getEventDate());
            ps.setString(3, event.getLocation());
            ps.setInt(5, event.getNumOfVolunteer());
            ps.setString(6, event.getTaskDesc());
            ps.setDouble(7, event.getEventHour());
            ps.setString(8, event.getSecretCode());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public event getEventById(int id) {
        String sql = "SELECT * FROM Event WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapEvent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<event> getAllEvents() {
        List<event> event = new ArrayList<>();
        String sql = "SELECT * FROM Events";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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
        String sql = "SELECT * FROM Events WHERE orgId = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public boolean updateEvent(event event) {
        String sql = "UPDATE Events SET title = ?, description = ?, orgId = ?, eventDate = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, event.getEventName());
            ps.setString(2, event.getEventDate());
            ps.setString(3, event.getLocation());
            ps.setInt(5, event.getNumOfVolunteer());
            ps.setString(6, event.getTaskDesc());
            ps.setDouble(7, event.getEventHour());
            ps.setString(8, event.getSecretCode());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEvent(int eventId) {
        String sql = "DELETE FROM Events WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private event mapEvent(ResultSet rs) throws SQLException {
        event event = new event();
        event.setEventName(rs.getString("eventName"));
        event.setEventDate(rs.getString("date"));
        event.setLocation(rs.getString("location"));
        event.setStatus(rs.getString("status"));
        event.setNumOfVolunteer(rs.getInt("numOfVolunteer"));
        event.setTaskDesc(rs.getString("desc"));
        event.setEventHour(rs.getInt("hour"));
        event.setSecretCode(rs.getString("code"));

        return event;
    }
}
