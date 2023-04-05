package edu.wpi.teamR.controllers;

import edu.wpi.teamR.database.EdgeDAO;
import edu.wpi.teamR.database.LocationNameDAO;
import edu.wpi.teamR.database.MoveDAO;
import edu.wpi.teamR.database.NodeDAO;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.base.MFXCombo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class CSVReaderController {
  @FXML MFXButton backButton;
  @FXML MFXButton fileButton;
  @FXML MFXButton submitButton;
  @FXML Text fileText;
  static File selectedFile;
  @FXML
  MFXComboBox<String> dropdown;
  ObservableList<String> DAOType =
          FXCollections.observableArrayList("Node", "Edge", "LocationName", "Move");
  FileChooser fileChooser;

  private NodeDAO nodes;
  private EdgeDAO edges;
  private LocationNameDAO locationNames;
  private static MoveDAO moves;

  String username = "teamr";
  String password = "teamr150";
  String schemaName = "prototype2";
  String url = "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb";

  @FXML
  public void initialize() throws SQLException, ClassNotFoundException {
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.EMPLOYEE));
    fileButton.setOnAction(event -> openFile());
    dropdown.setText("Choose DAO Type");
    dropdown.setItems(DAOType);
    submitButton.setOnMouseClicked(event -> {
      try {
        submit();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    });

    nodes = NodeDAO.createInstance(username, password, "node", schemaName, url);
    edges = EdgeDAO.createInstance(username, password, "edge", schemaName, url);
    locationNames = LocationNameDAO.createInstance(username, password, "locationName", schemaName, url);
    moves = MoveDAO.createInstance(username, password, "move", schemaName, url);
  }

  public void openFile() {
    fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    fileChooser.setTitle("Open File");
    selectedFile = fileChooser.showOpenDialog(fileButton.getScene().getWindow());
    fileText.setText(selectedFile.getName());
  }

  public void submit() throws SQLException, FileNotFoundException, ClassNotFoundException {
    String choice = dropdown.getValue();
    switch (choice) {
      case "Node":
        nodes.readCSV(selectedFile.getPath());
        break;
      case "Edge":
        edges.readCSV(selectedFile.getPath());
        break;
      case "LocationName":
        locationNames.readCSV(selectedFile.getPath());
        break;
      case "Moves":
        moves.readCSV(selectedFile.getPath());
        break;
    }
    selectedFile = null;
    fileText.setText("...");
  }
}
