package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class EmployeeHomeController {

  @FXML MFXButton backButton;
  @FXML MFXButton requestButton;
  @FXML MFXButton updateNodesButton;
  @FXML MFXButton updateMovesButton;
  @FXML MFXButton updateEdgesButton;
  @FXML MFXButton updateLocationNamesButton;
  @FXML MFXButton CSVReaderButton;

  @FXML
  public void initialize() {
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    requestButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SortOrders));
    updateNodesButton.setOnMouseClicked(event -> Navigation.navigate(Screen.NODES));
    updateMovesButton.setOnMouseClicked(event -> Navigation.navigate(Screen.MOVES));
    updateEdgesButton.setOnMouseClicked(event -> Navigation.navigate(Screen.EDGES));
    updateLocationNamesButton.setOnMouseClicked(event -> Navigation.navigate(Screen.LOCATIONNAMES));
    CSVReaderButton.setOnMouseClicked(event -> Navigation.navigate(Screen.READCSV));
  }
}
