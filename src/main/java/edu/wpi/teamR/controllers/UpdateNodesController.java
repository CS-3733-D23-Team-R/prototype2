package edu.wpi.teamR.controllers;

import edu.wpi.teamR.database.Node;
import edu.wpi.teamR.database.NodeDAO;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.sql.SQLException;
import java.util.ArrayList;

public class UpdateNodesController {

  NodeDAO dao;
  @FXML MFXButton backButton;
  @FXML TableView<Node> nodesTable;
  @FXML TableColumn<Node, Integer> nodeIDColumn;
  @FXML TableColumn<Node, Integer> xCoordinateColumn;
  @FXML TableColumn<Node, Integer> yCoordinateColumn;
  @FXML TableColumn<Node, String> floorColumn;
  @FXML TableColumn<Node, String> buildingColumn;
  @FXML TableColumn<Node, Void> deleteColumn;

  @FXML
  public void initialize() throws SQLException, ClassNotFoundException {
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.EMPLOYEE));
    setTableColumns();

    xCoordinateColumn.setOnEditCommit(event -> {
      Node node = event.getRowValue();
      node.setxCoord(event.getNewValue());
      try {
        dao.modifyNodeByID(node.getNodeID(), event.getNewValue(), node.getYCoord(), node.getFloorNum(), node.getBuilding());
      } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    });
    yCoordinateColumn.setOnEditCommit(event -> {
      Node node = event.getRowValue();
      node.setyCoord(event.getNewValue());
      try {
        dao.modifyNodeByID(node.getNodeID(), node.getXCoord(), event.getNewValue(), node.getFloorNum(), node.getBuilding());
      } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    });
    floorColumn.setOnEditCommit(event -> {
      Node node = event.getRowValue();
      node.setFloorNum(event.getNewValue());
      try {
        dao.modifyNodeByID(node.getNodeID(), node.getXCoord(), node.getYCoord(), event.getNewValue(), node.getBuilding());
      } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    });
    buildingColumn.setOnEditCommit(event -> {
      Node node = event.getRowValue();
      node.setBuilding(event.getNewValue());
      try {
        dao.modifyNodeByID(node.getNodeID(), node.getXCoord(), node.getYCoord(), node.getFloorNum(), event.getNewValue());
      } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public void setTableColumns() throws SQLException, ClassNotFoundException {
    dao = NodeDAO.createInstance("teamr", "teamr150", "node",
            "prototype2", "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb");

    ArrayList<Node> nodeList = dao.getNodes();
    ObservableList<Node> tableData = FXCollections.observableArrayList(nodeList);
    nodesTable.setItems(tableData);

    nodeIDColumn.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    nodeIDColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    xCoordinateColumn.setCellValueFactory(new PropertyValueFactory<>("xCoord"));
    xCoordinateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    yCoordinateColumn.setCellValueFactory(new PropertyValueFactory<>("yCoord"));
    yCoordinateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    floorColumn.setCellValueFactory(new PropertyValueFactory<>("floorNum"));
    floorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    buildingColumn.setCellValueFactory(new PropertyValueFactory<>("building"));
    buildingColumn.setCellFactory(TextFieldTableCell.forTableColumn());

    deleteColumn.setCellFactory(column -> new TableCell<>() {
      private final MFXButton deleteButton = new MFXButton("Delete");
      {
        deleteButton.setOnAction(event -> {
          Node node = getTableView().getItems().get(getIndex());
          try {
            dao.deleteNodes(node.getNodeID(), node.getXCoord(), node.getYCoord(), node.getFloorNum(), node.getBuilding());
          } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
          }
          getTableView().getItems().remove(node);
        });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setGraphic(null);
        } else {
          setGraphic(deleteButton);
        }
      }
    });
  }
}
