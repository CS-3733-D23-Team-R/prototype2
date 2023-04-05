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
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

public class UpdateEdgeController{
    @FXML
    MFXButton backButton;
    @FXML
    TableView<Edge> nodesTable;
    @FXML TableColumn<Edge, Integer> startNodeColumn;
    @FXML TableColumn<Edge, Integer> endNodeColumn;

    static EdgeDAO eDAO;


    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        eDAO = EdgeDAO.createInstance("teamr", "teamr150", "edge", "prototype2", "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb");
        ObservableList<Edge> edgeList = FXCollections.observableArrayList(EdgeDAO.getInstance().getEdges());
        backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.EMPLOYEE));
        nodesTable.setItems(edgeList);
        startNodeColumn.setCellValueFactory(new PropertyValueFactory<>("startNode"));
        endNodeColumn.setCellValueFactory(new PropertyValueFactory<>("endNode"));
        nodesTable.getColumns().setAll(startNodeColumn, endNodeColumn);
    }
}
