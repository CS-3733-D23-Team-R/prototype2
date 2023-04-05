package edu.wpi.teamR.controllers;

import edu.wpi.teamR.database.Node;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class UpdateMoveController{
    @FXML
    MFXButton backButton;
    @FXML
    TableView nodesTable;
    @FXML
    TableColumn nodeIDColumn;
    @FXML TableColumn nameColumn;
    @FXML TableColumn dateColumn;


    @FXML
    public void initialize() {
        backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.EMPLOYEE));

    }
}
