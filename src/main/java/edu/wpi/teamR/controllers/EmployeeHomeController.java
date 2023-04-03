package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class EmployeeHomeController {

  @FXML MFXButton backButton;
  @FXML MFXButton requestButton;
  @FXML MFXButton updateNodesButton;
  @FXML MFXButton CSVReaderButton;

  @FXML
  public void initialize() {
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    requestButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SortOrders));
    updateNodesButton.setOnMouseClicked(event -> Navigation.navigate(Screen.NODES));
    CSVReaderButton.setOnMouseClicked(event -> Navigation.navigate(Screen.READCSV));
  }
}
