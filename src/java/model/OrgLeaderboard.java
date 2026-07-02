/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Asus
 */
public class OrgLeaderboard {

    private int orgId;
    private String orgName;
    private String orgType;
    private double totalHoursGenerated;

    public OrgLeaderboard() {
    }

    public OrgLeaderboard(int orgId, String orgName, String orgType, double totalHoursGenerated) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgType = orgType;
        this.totalHoursGenerated = totalHoursGenerated;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public void setTotalHoursGenerated(double totalHoursGenerated) {
        this.totalHoursGenerated = totalHoursGenerated;
    }

    public int getOrgId() {
        return orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getOrgType() {
        return orgType;
    }

    public double getTotalHoursGenerated() {
        return totalHoursGenerated;
    }

}
