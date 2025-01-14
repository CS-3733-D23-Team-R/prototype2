package edu.wpi.teamR.controllers;

import edu.wpi.teamR.Main;
import edu.wpi.teamR.database.EdgeDAO;
import edu.wpi.teamR.database.LocationNameDAO;
import edu.wpi.teamR.database.MoveDAO;
import edu.wpi.teamR.database.Node;
import edu.wpi.teamR.database.NodeDAO;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import net.kurobako.gesturefx.*;
import edu.wpi.teamR.pathfinding.*;
import edu.wpi.teamR.database.*;
import org.controlsfx.control.SearchableComboBox;

public class MapController {
  @FXML MFXButton resetButton;
  @FXML MFXButton searchButton;
  @FXML MFXButton floorUpButton;
  @FXML MFXButton floorDownButton;
  @FXML MFXCheckbox accessibleCheckbox;

  @FXML
  SearchableComboBox<String> startField;
  @FXML
  SearchableComboBox<String> endField;

  @FXML MFXButton homeButton;
  @FXML BorderPane borderPane;
  @FXML AnchorPane anchorPane;

  @FXML GesturePane gesturePane;

  @FXML Text floorText;
  @FXML MFXButton clearButton;
  @FXML MFXCheckbox textCheckbox;
  URL firstFloorLink = Main.class.getResource("images/01_thefirstfloor.png");
  URL secondFloorLink = Main.class.getResource("images/02_thesecondfloor.png");
  URL thirdFloorLink = Main.class.getResource("images/03_thethirdfloor.png");
  URL LLOneLink = Main.class.getResource("images/00_thelowerlevel1.png");
  URL LLTwoLink = Main.class.getResource("images/00_thelowerlevel2.png");

  ImageView imageView;
  int currentFloor = 2;

  URL[] linkArray = {
    LLTwoLink, LLOneLink, firstFloorLink, secondFloorLink, thirdFloorLink,
  };

  String[] floorNames = {
    "Lower Level Two",
    "Lower Level One",
    "First Floor",
    "Second Floor",
    "Third Floor"
  };

  String[] nodeFloorNames = {
          "L2",
          "L1",
          "1",
          "2",
          "3"
  };

  AnchorPane[] paths = new AnchorPane[5];

  private AnchorPane mapPane = new AnchorPane();

  HashMap<String, Integer> floorNamesMap = new HashMap<String, Integer>();

  private NodeDAO nodes;
  private EdgeDAO edges;
  private LocationNameDAO locationNames;
  private static MoveDAO moves;

  String username = "teamr";
  String password = "teamr150";
  String schemaName = "prototype2";
  String url = "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb";

  @FXML
  public void initialize() throws Exception {
    for (int i = 0; i < 5; i++) {
      paths[i] = new AnchorPane();
      floorNamesMap.put(nodeFloorNames[i], i);
    }

    imageView = new ImageView(linkArray[currentFloor].toExternalForm());
    gesturePane.setContent(mapPane); // set to groundfloor
    mapPane.getChildren().add(imageView);
    gesturePane.setMinScale(0.25);
    gesturePane.setMaxScale(2);
    resetButton.setOnMouseClicked(event -> reset());
    floorDownButton.setOnMouseClicked(event -> displayFloorDown());
    floorUpButton.setOnMouseClicked(event -> displayFloorUp());
    homeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    clearButton.setOnMouseClicked(event -> clearPath());
    searchButton.setOnMouseClicked(event -> {
      try {
        search();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    floorText.setText(floorNames[currentFloor]);
    startField.setValue("Select Start");
    endField.setValue("Select End");

    reset();

    nodes = NodeDAO.createInstance(username, password, "node", schemaName, url);
    edges = EdgeDAO.createInstance(username, password, "edge", schemaName, url);
    locationNames = LocationNameDAO.createInstance(username, password, "locationName", schemaName, url);
    moves = MoveDAO.createInstance(username, password, "move", schemaName, url);

    setChoiceboxes();
  }

  // Reset to original zoom
  public void reset() {
    // zoom to 1x
    gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
    gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
    gesturePane.centreOn(new Point2D(2500, 1700));
  }

  public void clearPath() {
    paths[currentFloor].getChildren().clear();
    mapPane.getChildren().remove(paths[currentFloor]);
    for (int i = 0; i < 5; i++) {
      paths[i] = new AnchorPane();
    }
  }

  // zoom into a desired location
  // TODO fix zoomTo error
  public void animateZoomTo(int x, int y, int time) {
    // gesturePane.animate(Duration.millis(time)).zoomTo(1);
    // animate with some options
    gesturePane
        .animate(Duration.millis(time))
        .interpolateWith(Interpolator.EASE_BOTH)
        .afterFinished(() -> System.out.println("Done!"))
        .centreOn(new Point2D(x, y));
  }

  public void search() throws Exception {
    /*TODO
    take info from fields
    calculate route
    find spread of nodes on current floor
    animateZoom to show all nodes on this floor
    create path between nodes on ALL floors
    create/display textual path? (would have to add spot to display)
     */
    String start = startField.getValue().toString();
    String end = endField.getValue().toString();
    Boolean isAccessible = accessibleCheckbox.isPressed();
    displayPath(start, end, isAccessible);
  }

  public void displayFloorUp() {
    if (currentFloor < 5) {
      currentFloor++;
      imageView = new ImageView(linkArray[currentFloor].toExternalForm());
      mapPane.getChildren().clear();
      mapPane.getChildren().add(imageView);
      mapPane.getChildren().add(paths[currentFloor]);
      floorText.setText(floorNames[currentFloor]);
      //reset();
    }
  }

  public void displayFloorDown() {
    if (currentFloor > 0) {
      currentFloor--;
      imageView = new ImageView(linkArray[currentFloor].toExternalForm());
      mapPane.getChildren().clear();
      mapPane.getChildren().add(imageView);
      mapPane.getChildren().add(paths[currentFloor]);
      floorText.setText(floorNames[currentFloor]);
      //reset();
    }
  }

  public void displayFloorNum(int floorNum) {
    if (floorNum < 5) {
      currentFloor = floorNum;
      imageView = new ImageView(linkArray[currentFloor].toExternalForm());
      mapPane.getChildren().clear();
      mapPane.getChildren().add(imageView);
      mapPane.getChildren().add(paths[currentFloor]);
      floorText.setText(floorNames[currentFloor]);
      //reset();
    }
  }

  public void displayPath(String startLocation, String endLocation, Boolean accessible) throws Exception {
    clearPath();
    mapPane.getChildren().add(paths[currentFloor]);

    int startID = idFromName(startLocation);
    int endID = idFromName(endLocation);

    Pathfinder pathfinder = new Pathfinder(nodes, edges, moves, locationNames);
    Path mapPath = pathfinder.aStarPath(startID, endID, accessible);
    ArrayList<Integer> currentPath = mapPath.getPath();

    Node startNode = nodes.selectNodeByID(startID);
    Node endNode = nodes.selectNodeByID(endID);

    if (startNode.getFloorNum() != nodeFloorNames[currentFloor]){
      displayFloorNum(floorNamesMap.get(startNode.getFloorNum()));
    }

    Circle start = new Circle(startNode.getXCoord(), startNode.getYCoord(), 5, Color.RED);
    Text startText = new Text(nameFromID(startNode.getNodeID()));
    startText.setX(startNode.getXCoord() + 10);
    startText.setY(startNode.getYCoord());
    startText.setFill(Color.RED);
    paths[currentFloor].getChildren().add(start);
    paths[currentFloor].getChildren().add(startText);

    int drawFloor = currentFloor;
    for (int i = 0; i < mapPath.getPath().size() - 1; i++) {
      Node n1 = nodes.selectNodeByID(mapPath.getPath().get(i));
      Node n2 = nodes.selectNodeByID(mapPath.getPath().get(i + 1));
      if (n1.getFloorNum().equals(nodeFloorNames[drawFloor]) && n2.getFloorNum().equals(nodeFloorNames[drawFloor])) {
        Line l1 = new Line(n1.getXCoord(), n1.getYCoord(), n2.getXCoord(), n2.getYCoord());
        paths[drawFloor].getChildren().add(l1);
      }
      else {
        drawFloor = floorNamesMap.get(n2.getFloorNum());
      }
    }
    Circle end = new Circle(endNode.getXCoord(), endNode.getYCoord(), 5, Color.RED);
    Text endText = new Text(nameFromID(endNode.getNodeID()));
    endText.setX(endNode.getXCoord() + 10);
    endText.setY(endNode.getYCoord());
    endText.setFill(Color.RED);
    paths[drawFloor].getChildren().add(end);
    paths[drawFloor].getChildren().add(endText);
  }

  private static int idFromName(String longname) throws NotFoundException {
    ArrayList<Move> matchingMoves = moves.selectMoves(null, longname, null);
    int newestMove = -1;
    long lowestDate = Long.MAX_VALUE;

    for (int i = 0; i < matchingMoves.size(); i++){
      long moveDate = matchingMoves.get(i).getMoveDate().getTime();
      if (moveDate < lowestDate){
        lowestDate = moveDate;
        newestMove = i;
      }
    }
    if (newestMove==-1)
      throw new NotFoundException();

    return matchingMoves.get(newestMove).getNodeID();
  }

  private static String nameFromID(int nodeID) throws NotFoundException {
    ArrayList<Move> matchingMoves = moves.selectMoves(nodeID, null, null);
    int newestMove = -1;
    long lowestDate = Long.MAX_VALUE;

    for (int i = 0; i < matchingMoves.size(); i++){
      long moveDate = matchingMoves.get(i).getMoveDate().getTime();
      if (moveDate < lowestDate){
        lowestDate = moveDate;
        newestMove = i;
      }
    }
    if (newestMove==-1)
      throw new NotFoundException();

    return matchingMoves.get(newestMove).getLongName();
  }

  void setChoiceboxes(){
    ArrayList<LocationName> locationNodes = locationNames.selectLocationNames(null, null, null);
    ArrayList<String> names = new ArrayList<String>();
    for (LocationName l: locationNodes) {
      if(!l.getLongName().contains("Hall")) {
        names.add(l.getLongName());
      }
    }
    ObservableList<String> choices = FXCollections.observableArrayList(names);
    startField.setItems(choices);
    endField.setItems(choices);
  }
}
