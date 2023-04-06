package edu.wpi.teamR.database;

import java.sql.Timestamp;

public class ServiceRequest {
    private String requesterName, location, itemType, staffMember, additionalNotes;
    private Timestamp requestDate; //TODO: test localdatetime vs timestamp
    private RequestStatus requestStatus;
    private Integer requestID;

    public ServiceRequest(Integer requestID, String requesterName, String location, String itemType, String staffMember,
                          String additionalNotes, Timestamp requestDate, RequestStatus requestStatus) {
        this.requesterName = requesterName;
        this.location = location;
        this.itemType = itemType;
        this.staffMember = staffMember;
        this.additionalNotes = additionalNotes;
        this.requestDate = requestDate;
        this.requestStatus = requestStatus;
        this.requestID = requestID;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setStaffMember(String staffMember) {
        this.staffMember = staffMember;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public void setRequestID(Integer requestID) {
        this.requestID = requestID;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public String getLocation() {
        return location;
    }

    public String getItemType() {
        return itemType;
    }

    public String getStaffMember() {
        return staffMember;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public Integer getRequestID() {
        return requestID;
    }
}
