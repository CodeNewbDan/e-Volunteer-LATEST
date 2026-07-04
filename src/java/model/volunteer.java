/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author syahmi
 * 18/6/2026 
 */
public class volunteer  {
    private String studentId;
    private String fullName;
    private String volunteerEmail;
    private int phoneNum;
    private String volunteerAddress;
    private String volunteerPassword;
    private String course;
    private double totalHours;
    private int volunteerId;
    
    public volunteer(){
        
    }

    public volunteer(String studentId, String fullName, String volunteerEmail, int phoneNum, String volunteerAddress, String volunteerPassword, String course, double totalHours) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.volunteerEmail = volunteerEmail;
        this.phoneNum = phoneNum;
        this.volunteerAddress = volunteerAddress;
        this.volunteerPassword = volunteerPassword;
        this.course = course;
        this.totalHours = totalHours;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getVolunteerEmail() {
        return volunteerEmail;
    }

    public void setVolunteerEmail(String volunteerEmail) {
        this.volunteerEmail = volunteerEmail;
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getVolunteerAddress() {
        return volunteerAddress;
    }

    public void setVolunteerAddress(String volunteerAddress) {
        this.volunteerAddress = volunteerAddress;
    }

    public String getVolunteerPassword() {
        return volunteerPassword;
    }

    public void setVolunteerPassword(String volunteerPassword) {
        this.volunteerPassword = volunteerPassword;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public int getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(int volunteerId) {
        this.volunteerId = volunteerId;
    }

    
}
