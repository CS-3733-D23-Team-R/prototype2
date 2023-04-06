package edu.wpi.teamR.controllers;

import edu.wpi.teamR.database.Node;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.database.Edge;
import edu.wpi.teamR.database.EdgeDAO;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.sql.SQLException;
import java.util.ArrayList;

public class UpdateEdgeController{
    @FXML
    MFXButton backButton;
    @FXML
    TableView<Edge> nodesTable;
    @FXML TableColumn<Edge, Integer> startNodeColumn;
    @FXML TableColumn<Edge, Integer> endNodeColumn;

    EdgeDAO eDAO;


    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        eDAO = EdgeDAO.createInstance("teamr", "teamr150", "edge", "prototype2", "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb");
        ObservableList<Edge> edgeList = FXCollections.observableArrayList(EdgeDAO.getInstance().getEdges());
        backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.EMPLOYEE));
        nodesTable.setItems(edgeList);
        startNodeColumn.setCellValueFactory(new PropertyValueFactory<>("startNode"));
        startNodeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        endNodeColumn.setCellValueFactory(new PropertyValueFactory<>("endNode"));
        endNodeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        nodesTable.getColumns().setAll(startNodeColumn, endNodeColumn);

        startNodeColumn.setOnEditCommit(event -> {
            Edge edge = event.getRowValue();
            Integer oldStart = edge.getStartNode();
            edge.setStartNode(event.getNewValue());
            try {
                eDAO.deleteConnectingEdge(oldStart, edge.getEndNode());
                eDAO.addEdge(edge.getStartNode(), edge.getEndNode());
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        endNodeColumn.setOnEditCommit(event -> {
            Edge edge = event.getRowValue();
            Integer oldEnd = edge.getEndNode();
            edge.setStartNode(event.getNewValue());
            try {
                eDAO.deleteConnectingEdge(edge.getStartNode(), oldEnd);
                eDAO.addEdge(edge.getStartNode(), edge.getEndNode());
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
