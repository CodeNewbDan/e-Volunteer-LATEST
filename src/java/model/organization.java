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
public class organization implements java.io.Serializable{
    
    private static final long serialVersionUID = 1L;
    private int orgId;
    private String orgName;
    private String registrationNum;
    private String orgEmail;
    private int contactPerson;
    private String orgAddress;
    private String orgType;
    private String orgPassword;

    public organization(){
        
    }

    public organization(int orgId, String orgName, String registrationNum, String orgEmail, int contactPerson, String orgAddress, String orgType, String orgPassword) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.registrationNum = registrationNum;
        this.orgEmail = orgEmail;
        this.contactPerson = contactPerson;
        this.orgAddress = orgAddress;
        this.orgType = orgType;
        this.orgPassword = orgPassword;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }
   
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getRegistrationNum() {
        return registrationNum;
    }

    public void setRegistrationNum(String registrationNum) {
        this.registrationNum = registrationNum;
    }

    public String getOrgEmail() {
        return orgEmail;
    }

    public void setOrgEmail(String orgEmail) {
        this.orgEmail = orgEmail;
    }

    public int getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(int contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgPassword() {
        return orgPassword;
    }

    public void setOrgPassword(String orgPassword) {
        this.orgPassword = orgPassword;
    }
    
}
