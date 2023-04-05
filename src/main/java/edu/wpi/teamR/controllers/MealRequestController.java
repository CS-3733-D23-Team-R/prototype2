package edu.wpi.teamR.controllers;

import edu.wpi.teamR.database.FoodRequestDAO;
import edu.wpi.teamR.database.RequestStatus;
import edu.wpi.teamR.fields.MealFields;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class MealRequestController {

  @FXML MFXButton cancelButton;
  @FXML MFXButton resetButton;
  @FXML MFXButton submitButton;
  @FXML MFXTextField nameField;
  @FXML MFXTextField locationField;
  @FXML MFXTextField staffMemberField;
  @FXML MFXTextField notesField;
  @FXML ChoiceBox mealTypeBox;

  private MealFields mealField;

  ObservableList<String> mealTypeList =
      FXCollections.observableArrayList("Chicken", "Beef", "Fish");

  @FXML
  public void initialize() {
    cancelButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    resetButton.setOnMouseClicked(event -> clear());
    submitButton.setOnMouseClicked(event -> submit());
    mealTypeBox.setValue("Select Meal");
    mealTypeBox.setItems(mealTypeList);
  }

  public Timestamp CurrentDateTime(){
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();
      System.out.println(dtf.format(now));
      return new Timestamp(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(),now.getMinute(),now.getSecond(),now.getNano());
  }

  @FXML
  public void submit() {
    String mealType = mealTypeBox.getValue().toString();
    if (mealType == "Select Meal") {
      mealType = "";
    }
    System.out.println(
        mealField.getName()
            + " "
            + mealField.getLocation()
            + " "
            + mealField.getStaffMember()
            + " "
            + mealField.getNotes()
            + " "
            + mealField.getMeal());
    Navigation.navigate(Screen.HOME);
    try{
      FoodRequestDAO.getInstance().addFoodRequest(nameField.getText(),locationField.getText(), mealType, "unassigned", notesField.getText(), CurrentDateTime(), RequestStatus.Unstarted);
    }
    catch(SQLException e){
      e.printStackTrace();
    }
    catch(ClassNotFoundException e){
      e.printStackTrace();
    }
  }

  @FXML
  public void clear() {
    nameField.clear();
    locationField.clear();
    staffMemberField.clear();
    notesField.clear();
    mealTypeBox.setValue("Select Meal");
  }
}
