package edu.wpi.teamR.controllers;

import edu.wpi.teamR.database.*;
import edu.wpi.teamR.fields.MealFields;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.security.auth.callback.Callback;
import java.sql.Timestamp;
import java.util.ArrayList;

public class SortOrdersController {

  @FXML TableView requestTable;
  @FXML TableColumn idColumn;
  @FXML TableColumn nameColumn;
  @FXML TableColumn locationColumn;
  @FXML TableColumn requestTypeColumn;
  @FXML TableColumn notesColumn;
  @FXML TableColumn staffMemberColumn;
  @FXML TableColumn timeColumn;
  @FXML TableColumn statusColumn;
  @FXML MFXButton backButton;


  @FXML
  public void initialize() {
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.EMPLOYEE));

    idColumn.setCellValueFactory(new PropertyValueFactory<>("requestID"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("requesterName"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    staffMemberColumn.setCellValueFactory(new PropertyValueFactory<>("staffMember"));
    notesColumn.setCellValueFactory(new PropertyValueFactory<>("additionalNotes"));
    timeColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
    requestTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));

    for (FoodRequest foodRequest : FoodRequestDAO.getInstance().getFoodRequests()) {
      requestTable.getItems().add(foodRequest);
    }

    for (FurnitureRequest furnRequest : FurnitureRequestDAO.getInstance().getFurnitureRequests()) {
      requestTable.getItems().add(furnRequest);
    }


  }
}
