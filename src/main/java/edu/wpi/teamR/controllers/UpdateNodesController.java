package edu.wpi.teamR.controllers;

import edu.wpi.teamR.database.Node;
import edu.wpi.teamR.database.NodeDAO;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.ArrayList;

public class UpdateNodesController {

  static NodeDAO dao;
  @FXML MFXButton backButton;
  @FXML TableView nodesTable;
  @FXML TableColumn nodeIDColumn;
  @FXML TableColumn xCoordinationColumn;
  @FXML TableColumn yCoordinationColumn;
  @FXML TableColumn floorColumn;
  @FXML TableColumn buildingColumn;

  private ObservableList<Node> nodeTypeList =
          FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.EMPLOYEE));

  }
  @FXML
  public ArrayList<Node> getNodes(){
    dao = NodeDAO.getInstance();
    return dao.getNodes();
  }
}
