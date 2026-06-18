/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package event;

/**
 *
 * @author syahmi
 * 18/6/2026 
 */
public class event {
    private String eventName;
    private String eventDate;
    private String location;
    private String status;
    private int numOfVolunteer;
    private String taskDesc;
    private double eventHour;
    private String secretCode;

    public event() {
    }

    public event(String eventName, String eventDate, String location, String status, int numOfVolunteer, String taskDesc, double eventHour, String secretCode) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
        this.status = status;
        this.numOfVolunteer = numOfVolunteer;
        this.taskDesc = taskDesc;
        this.eventHour = eventHour;
        this.secretCode = secretCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumOfVolunteer() {
        return numOfVolunteer;
    }

    public void setNumOfVolunteer(int numOfVolunteer) {
        this.numOfVolunteer = numOfVolunteer;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public double getEventHour() {
        return eventHour;
    }

    public void setEventHour(double eventHour) {
        this.eventHour = eventHour;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }
    
}
