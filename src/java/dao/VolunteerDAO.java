package dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author hansz
 */
import model.volunteer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDAO {

    private static final String URL = "jdbc:derby://localhost:1527/eVolunteer";
    private static final String USER = "app";
    private static final String PASS = "app";

    public VolunteerDAO() {
    }

    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public boolean registerVolunteer(volunteer v) {
        // Included all schema fields to support full registration details
        String sql = "INSERT INTO Volunteer (StudentID, FullName, Volunteer_Email, PhoneNumber, Address, Password, Course, TotalHours) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getStudentId());
            ps.setString(2, v.getFullName());
            ps.setString(3, v.getVolunteerEmail());
            ps.setInt(4, v.getPhoneNum());
            ps.setString(5, v.getVolunteerAddress());
            ps.setString(6, v.getVolunteerPassword());
            ps.setString(7, v.getCourse());
            ps.setDouble(8, v.getTotalHours());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public volunteer loginVolunteer(String email, String pwd) {
        String sql = "SELECT * FROM Volunteer WHERE Volunteer_Email = ? AND Password = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, pwd);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapVolunteer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public volunteer getVolunteerById(int id) {
        String sql = "SELECT * FROM Volunteer WHERE VolunteerID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapVolunteer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateVolunteerProfile(volunteer v) {
        String sql = "UPDATE Volunteer SET StudentID = ?, FullName = ?, Volunteer_Email = ?, PhoneNumber = ?, Address = ?, Password = ?, Course = ? WHERE VolunteerID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getStudentId());
            ps.setString(2, v.getFullName());
            ps.setString(3, v.getVolunteerEmail());
            ps.setInt(4, v.getPhoneNum());
            ps.setString(5, v.getVolunteerAddress());
            ps.setString(6, v.getVolunteerPassword());
            ps.setString(7, v.getCourse());
            ps.setInt(8, v.getVolunteerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Permanently deletes a student's record from the Volunteer table. Cascade
     * constraints in Apache Derby will automatically wipe associated
     * registrations.
     */
    public boolean deleteVolunteer(int volunteerId) {
        String sql = "DELETE FROM Volunteer WHERE VolunteerID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, volunteerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[E-Sukarelawan VolunteerDAO Exception]: Deletion failed for ID " + volunteerId);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Used to modify cached hours
     */
    public boolean updateVolunteerHours(int id, double newHours) {
        String sql = "UPDATE Volunteer SET TotalHours = ? WHERE VolunteerID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newHours);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private volunteer mapVolunteer(ResultSet rs) throws SQLException {
        volunteer v = new volunteer();
        v.setVolunteerId(rs.getInt("VolunteerID"));
        v.setStudentId(rs.getString("StudentID"));
        v.setFullName(rs.getString("FullName"));
        v.setVolunteerEmail(rs.getString("Volunteer_Email"));
        v.setPhoneNum(rs.getInt("PhoneNumber"));
        v.setVolunteerAddress(rs.getString("Address"));
        v.setVolunteerPassword(rs.getString("Password"));
        v.setCourse(rs.getString("Course"));
        v.setTotalHours(rs.getDouble("TotalHours"));
        return v;
    }
    
    public List<volunteer> getAllVolunteersSortedByHours() {
        List<volunteer> list = new ArrayList<>();
        String sql = "SELECT * FROM Volunteer ORDER BY TotalHours DESC";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                volunteer v = new volunteer();
                v.setVolunteerId(rs.getInt("VolunteerID"));
                v.setStudentId(rs.getString("StudentID"));
                v.setFullName(rs.getString("FullName"));
                v.setVolunteerEmail(rs.getString("Volunteer_Email"));

                // Safe parsing: Database holds PhoneNumber as VARCHAR, POJO maps to Java int
                String phoneStr = rs.getString("PhoneNumber");
                int parsedPhone = 0;
                if (phoneStr != null && !phoneStr.trim().isEmpty()) {
                    try {
                        parsedPhone = Integer.parseInt(phoneStr.trim());
                    } catch (NumberFormatException e) {
                        parsedPhone = 0; // fallback if string contains formatting characters (+, -, space)
                    }
                }
                v.setPhoneNum(parsedPhone);

                v.setVolunteerAddress(rs.getString("Address"));
                v.setVolunteerPassword(rs.getString("Password"));
                v.setCourse(rs.getString("Course"));
                v.setTotalHours(rs.getDouble("TotalHours"));

                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    

}
