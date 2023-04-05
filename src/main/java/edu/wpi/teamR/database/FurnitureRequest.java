package edu.wpi.teamR.database;

import java.sql.Timestamp;

public class FurnitureRequest extends ServiceRequest{
    String furnitureType;
    public FurnitureRequest(Integer requestID, String requesterName, String location, String furnitureType, String staffMember,
                            String additionalNotes, Timestamp requestDate, RequestStatus requestStatus) {
            super(requestID,requesterName,location,furnitureType,staffMember, additionalNotes,requestDate,requestStatus);
            this.furnitureType =furnitureType;
    }


    public String getFurnitureType() {
        return furnitureType;
    }

    public void setFurnitureType(String furnitureType) {
        this.furnitureType = furnitureType;
    }
}
