package edu.wpi.teamR.controllers;

import edu.wpi.teamR.fields.MealFields;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SortOrdersController {

  @FXML TableView requestTable;
  @FXML TableColumn nameColumn;
  @FXML TableColumn locationColumn;
  @FXML TableColumn requestTypeColumn;
  @FXML TableColumn notesColumn;
  @FXML TableColumn staffMemberColumn;
  @FXML TableColumn timeColumn;
  @FXML TableColumn statusColumn;
  @FXML MFXButton backButton;

  // for un-used choice box
  // ObservableList<String> orderList = FXCollections.observableArrayList("Name", "Location", "Request Type", "Notes", "Staff Member", "Time", "Status");

  @FXML
  public void initialize() {
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.EMPLOYEE));
    nameColumn.setCellValueFactory(new PropertyValueFactory<MealFields, String>("Name"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<MealFields, String>("Location"));
    requestTypeColumn.setCellValueFactory(
        new PropertyValueFactory<MealFields, String>("requestType"));
    notesColumn.setCellValueFactory(new PropertyValueFactory<MealFields, String>("Notes"));
    staffMemberColumn.setCellValueFactory(
        new PropertyValueFactory<MealFields, String>("staffMember"));
    timeColumn.setCellValueFactory(new PropertyValueFactory<MealFields, String>("Time"));
    statusColumn.setCellValueFactory(new PropertyValueFactory<MealFields, String>("Status"));

    requestTable.setItems(getRequests());
  }

  public ObservableList<MealFields> getRequests() {
    ObservableList<MealFields> meals = FXCollections.observableArrayList();

    // test list
    meals.add(new MealFields("me", "here", "staff", "hi", "chicken"));
    meals.add(new MealFields("you", "there", "staff2", "bye", "beef"));
    meals.add(new MealFields("a", "a", "a", "a", "a"));
    meals.add(new MealFields("z", "z", "z", "z", "z"));

    return meals;
  }
}
