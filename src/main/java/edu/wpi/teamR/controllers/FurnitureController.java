package edu.wpi.teamR.controllers;

import edu.wpi.teamR.database.*;
import edu.wpi.teamR.fields.FurnitureFields;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FurnitureController {

  @FXML MFXButton cancelButton;
  @FXML MFXButton resetButton;
  @FXML MFXButton submitButton;
  @FXML MFXTextField nameField;
  @FXML MFXTextField locationField;
  @FXML MFXTextField staffMemberField;
  @FXML MFXTextField notesField;
  @FXML ChoiceBox furnitureTypeBox;

  private FurnitureFields furnitureFields;

  ObservableList<String> furnitureTypeList =
      FXCollections.observableArrayList("Desk", "Sofa", "Wardrobe");

  @FXML
  public void initialize() {
    cancelButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    resetButton.setOnMouseClicked(event -> clear());
    submitButton.setOnMouseClicked(event -> submit());

    furnitureTypeBox.setValue("Select Furniture");
    furnitureTypeBox.setItems(furnitureTypeList);
  }

  @FXML
  public void clear() {
    nameField.clear();
    locationField.clear();
    staffMemberField.clear();
    notesField.clear();

    furnitureTypeBox.setValue("Select Furniture");
  }
  public Timestamp CurrentDateTime(){
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    System.out.println(dtf.format(now));
    return new Timestamp(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(),now.getMinute(),now.getSecond(),now.getNano());
  }
  @FXML
  public void submit() {
    String furnitureType = furnitureTypeBox.getValue().toString();

    if (furnitureType == "Select Furniture") {
      furnitureType = "";
    }

    Navigation.navigate(Screen.HOME);
    int id = 0;
    ArrayList<FoodRequest> foodList = FoodRequestDAO.getInstance().getFoodRequests();
    for(FoodRequest foodRequest: foodList){
      if(id < foodRequest.getRequestID()){
        id = foodRequest.getRequestID();
      }
    }
    ArrayList<FurnitureRequest> furnList = FurnitureRequestDAO.getInstance().getFurnitureRequests();
    for(FurnitureRequest furnitureRequest: furnList){
      if(id < furnitureRequest.getRequestID()){
        id = furnitureRequest.getRequestID();
      }
    }
    id++;
    try {
      FurnitureRequestDAO.getInstance().addFurnitureRequest(id, nameField.getText(), locationField.getText(), furnitureType, staffMemberField.getText(), notesField.getText(), CurrentDateTime(), RequestStatus.Unstarted);
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }
}
