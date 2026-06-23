/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model;
/**
 *
 * @author syahmi
 * 18/6/2026 
 */
public class registration {
    private int registerId;
    private String registerDate;
    private String attendanceDate;
    private String verificationDate;
    private String attendanceStatus;

    public registration() {
    }

    public registration(int registerId, String registerDate, String attendanceDate, String verificationDate, String attendanceStatus) {
        this.registerId = registerId;
        this.registerDate = registerDate;
        this.attendanceDate = attendanceDate;
        this.verificationDate = verificationDate;
        this.attendanceStatus = attendanceStatus;
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
