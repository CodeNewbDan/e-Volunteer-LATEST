package dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hansz
 */
import model.organization;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.OrgLeaderboard;

public class OrganizationDAO {
    private static final String URL = "jdbc:derby://localhost:1527/eVolunteer";
    private static final String USER = "app";
    private static final String PASS = "app";

    public OrganizationDAO() {
    }
    
    
    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public boolean registerOrg(organization org) {
        // Fully populates all structural fields required for an organization account
        String sql = "INSERT INTO Organization (OrgName, RegistrationNumber, OrgEmail, ContactPerson, OrgAddress, OrgType, Password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, org.getOrgName());
            ps.setInt(2, org.getRegistrationNum());
            ps.setString(3, org.getOrgEmail());
            ps.setInt(4, org.getContactPerson());
            ps.setString(5, org.getOrgAddress());
            ps.setString(6, org.getOrgType());
            ps.setString(7, org.getOrgPassword());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public organization loginOrg(String email, String pwd) {
        String sql = "SELECT * FROM Organization WHERE OrgEmail = ? AND Password = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, pwd);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapOrganization(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public organization getOrgById(int id) {
        String sql = "SELECT * FROM Organization WHERE OrganizationID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapOrganization(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateOrgProfile(organization org) {
        String sql = "UPDATE Organization SET OrgName = ?, RegistrationNumber = ?, OrgEmail = ?, ContactPerson = ?, OrgAddress = ?, OrgType = ?, Password = ? WHERE OrganizationID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, org.getOrgName());
            ps.setInt(2, org.getRegistrationNum());
            ps.setString(3, org.getOrgEmail());
            ps.setInt(4, org.getContactPerson());
            ps.setString(5, org.getOrgAddress());
            ps.setString(6, org.getOrgType());
            ps.setString(7, org.getOrgPassword());
            ps.setInt(8, org.getOrgId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteOrganization(int orgId) {
        String sql = "DELETE FROM Organization WHERE OrganizationID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orgId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[E-Sukarelawan OrganizationDAO Exception]: Deletion failed for ID " + orgId);
            e.printStackTrace();
            return false;
        }
    }

    private organization mapOrganization(ResultSet rs) throws SQLException {
        organization org = new organization();
        org.setOrgId(rs.getInt("OrganizationID"));
        org.setOrgName(rs.getString("OrgName"));
        org.setRegistrationNum(rs.getInt("RegistrationNumber"));
        org.setOrgEmail(rs.getString("OrgEmail"));
        org.setContactPerson(rs.getInt("ContactPerson"));
        org.setOrgAddress(rs.getString("OrgAddress"));
        org.setOrgType(rs.getString("OrgType"));
        org.setOrgPassword(rs.getString("Password"));
        return org;
    }
    
    public double getTotalUniversityHours() {
        double total = 0.0;
        String sql = "SELECT SUM(e.EventVolunteerHour) "
               + "FROM Registration r "
               + "JOIN Event e ON r.EventID = e.EventID "
               + "JOIN Organization o ON e.OrganizationID = o.OrganizationID "
               + "WHERE r.AttendanceStatus = 'Verified' AND o.OrgType = 'Club'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
        
    }
    
    public List<OrgLeaderboard> getClubLeaderboard() {
        List<OrgLeaderboard> list = new ArrayList<>();
        String sql = "SELECT o.OrganizationID, o.OrgName, o.OrgType, SUM(e.EventVolunteerHour) AS TotalHoursGenerated " +
                     "FROM Organization o " +
                     "JOIN Event e ON o.OrganizationID = e.OrganizationID " +
                     "JOIN Registration r ON e.EventID = r.EventID " +
                     "WHERE o.OrgType = 'Club' AND r.AttendanceStatus = 'Verified' " +
                     "GROUP BY o.OrganizationID, o.OrgName, o.OrgType " +
                     "ORDER BY TotalHoursGenerated DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                OrgLeaderboard row = new OrgLeaderboard();
                row.setOrgId(rs.getInt("OrganizationID"));
                row.setOrgName(rs.getString("OrgName"));
                row.setOrgType(rs.getString("OrgType"));
                row.setTotalHoursGenerated(rs.getDouble("TotalHoursGenerated"));
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
