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
import java.io.IOException;
import java.sql.SQLException;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.base.MFXCombo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class CSVReaderController {
  @FXML MFXButton backButton;
  @FXML MFXButton fileButton;
  @FXML MFXButton pathButton;
  @FXML MFXButton submitImport;
  @FXML MFXButton submitExport;
  @FXML Text fileText;
  @FXML Text pathText;

  static File selectedFile;
  static File selectedDirectory;
  @FXML
  MFXComboBox<String> importMenu;
  @FXML
  MFXComboBox<String> exportMenu;
  ObservableList<String> DAOType =
          FXCollections.observableArrayList("Node", "Edge", "LocationName", "Moves");
  FileChooser fileChooser;

  DirectoryChooser directoryChooser;

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
    importMenu.setText("Choose DAO Type");
    importMenu.setItems(DAOType);
    exportMenu.setText("Choose DAO Type");
    exportMenu.setItems(DAOType);
    submitImport.setOnMouseClicked(event -> {
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
    pathButton.setOnMouseClicked(event -> chooseDirectory());
    submitExport.setOnMouseClicked(event -> {
      try {
        export();
      } catch (ClassNotFoundException | SQLException | IOException e) {
        e.printStackTrace();
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
    String choice = importMenu.getValue();
    switch (choice) {
      case "Node":
        edges.deleteAllEdges();
        moves.deleteMove(null, null, null);
        nodes.readCSV(selectedFile.getPath());
        break;
      case "Edge":
        edges.readCSV(selectedFile.getPath());
        break;
      case "LocationName":
        moves.deleteMove(null, null, null);
        locationNames.readCSV(selectedFile.getPath());
        break;
      case "Moves":
        moves.readCSV(selectedFile.getPath());
        break;
    }
    selectedFile = null;
    fileText.setText("...");
  }

  public void chooseDirectory() {
    directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Choose Directory");
    selectedDirectory = directoryChooser.showDialog(pathButton.getScene().getWindow());
    pathText.setText(selectedDirectory.getAbsolutePath());
  }

  public void export() throws SQLException, IOException, ClassNotFoundException {
    String choice = exportMenu.getValue();
    switch (choice) {
      case "Node":
        nodes.writeCSV(selectedDirectory.getAbsolutePath() + "/nodes.csv");
        break;
      case "Edge":
        edges.writeCSV(selectedDirectory.getAbsolutePath() + "/edges.csv");
        break;
      case "LocationName":
        locationNames.writeCSV(selectedDirectory.getAbsolutePath() + "/locationNames.csv");
        break;
      case "Moves":
        moves.writeCSV(selectedDirectory.getAbsolutePath() + "/moves.csv");
        break;
    }
    selectedDirectory = null;
    pathText.setText("...");
  }
}
