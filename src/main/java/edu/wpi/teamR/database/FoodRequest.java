package edu.wpi.teamR.database;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FoodRequest extends ServiceRequest{
    String mealType;
    public FoodRequest(Integer requestID, String requesterName, String location, String mealType, String staffMember,
                       String additionalNotes, Timestamp requestDate, RequestStatus requestStatus) {
        super(requestID,requesterName,location,mealType,staffMember, additionalNotes,requestDate,requestStatus);
        this.mealType = mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getMealType() {
        return mealType;
    }
}
