/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model;

/**
 *
 * @author syahmi 18/6/2026
 */
public class registration {

    private int registerId;
    private int id;
    private int volunteerId;
    private int eventId;
    private String registerDate;
    private String attendanceDate;
    private String verificationDate;
    private String attendanceStatus;

    public registration() {
    }

    public registration(int registerId, int id, int volunteerId, int eventId, String registerDate, String attendanceDate, String verificationDate, String attendanceStatus) {
        this.registerId = registerId;
        this.id = id;
        this.volunteerId = volunteerId;
        this.eventId = eventId;
        this.registerDate = registerDate;
        this.attendanceDate = attendanceDate;
        this.verificationDate = verificationDate;
        this.attendanceStatus = attendanceStatus;
    }

    public int getId() {
        return id;
    }

    public int getVolunteerId() {
        return volunteerId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVolunteerId(int volunteerId) {
        this.volunteerId = volunteerId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getRegisterId() {
        return registerId;
    }

    public void setRegisterId(int registerId) {
        this.registerId = registerId;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

}
