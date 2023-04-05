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
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class UpdateNodesController {

  NodeDAO dao;
  @FXML MFXButton backButton;
  @FXML TableView<Node> nodesTable;
  @FXML TableColumn nodeIDColumn;
  @FXML TableColumn xCoordinateColumn;
  @FXML TableColumn yCoordinateColumn;
  @FXML TableColumn floorColumn;
  @FXML TableColumn buildingColumn;

  @FXML
  public void initialize() throws SQLException, ClassNotFoundException {
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.EMPLOYEE));
    setTableColumns();
  }

  public void setTableColumns() throws SQLException, ClassNotFoundException {
    dao = NodeDAO.createInstance("teamr", "teamr150", "node",
            "prototype2", "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb");

    ArrayList<Node> nodeList = dao.getNodes();
    ObservableList<Node> tableData = FXCollections.observableArrayList(nodeList);
    nodesTable.setItems(tableData);

    nodeIDColumn.setCellValueFactory(new PropertyValueFactory<Node, Integer>("nodeID"));
    xCoordinateColumn.setCellValueFactory(new PropertyValueFactory<Node, Integer>("xCoord"));
    yCoordinateColumn.setCellValueFactory(new PropertyValueFactory<Node, Integer>("yCoord"));
    floorColumn.setCellValueFactory(new PropertyValueFactory<Node, String>("floorNum"));
    buildingColumn.setCellValueFactory(new PropertyValueFactory<Node, String>("building"));
  }
}
