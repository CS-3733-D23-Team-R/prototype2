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
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.*;
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

public class MapController {
  @FXML MFXButton resetButton;
  @FXML MFXButton searchButton;
  @FXML MFXButton floorUpButton;
  @FXML MFXButton floorDownButton;
  @FXML MFXCheckbox accessibleCheckbox;

  @FXML MFXTextField startField;
  @FXML MFXTextField endField;

  @FXML MFXButton homeButton;
  @FXML BorderPane borderPane;
  @FXML AnchorPane anchorPane;

  @FXML GesturePane gesturePane;

  @FXML Text floorText;

  URL groundFloorLink = Main.class.getResource("images/00_thegroundfloor.png");
  URL firstFloorLink = Main.class.getResource("images/01_thefirstfloor.png");
  URL secondFloorLink = Main.class.getResource("images/02_thesecondfloor.png");
  URL thirdFloorLink = Main.class.getResource("images/03_thethirdfloor.png");
  URL LLOneLink = Main.class.getResource("images/00_thelowerlevel1.png");
  URL LLTwoLink = Main.class.getResource("images/00_thelowerlevel2.png");

  ImageView imageView;
  int currentFloor = 3;

  URL[] linkArray = {
    LLTwoLink, LLOneLink, groundFloorLink, firstFloorLink, secondFloorLink, thirdFloorLink,
  };

  String[] floorNames = {
    "Lower Level Two",
    "Lower Level One",
    "Ground Floor",
    "First Floor",
    "Second Floor",
    "Third Floor"
  };

  private AnchorPane mapPane = new AnchorPane();
  private AnchorPane pathPane = new AnchorPane();

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
    imageView = new ImageView(linkArray[currentFloor].toExternalForm());
    gesturePane.setContent(mapPane); // set to groundfloor
    mapPane.getChildren().add(imageView);
    gesturePane.setMinScale(0.25);
    gesturePane.setMaxScale(2);
    resetButton.setOnMouseClicked(event -> reset());
    floorDownButton.setOnMouseClicked(event -> displayFloorDown());
    floorUpButton.setOnMouseClicked(event -> displayFloorUp());
    homeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    searchButton.setOnMouseClicked(event -> {
      try {
        search();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    floorText.setText(floorNames[currentFloor]);
    reset();

    nodes = NodeDAO.createInstance(username, password, "node", schemaName, url);
    edges = EdgeDAO.createInstance(username, password, "edge", schemaName, url);
    locationNames = LocationNameDAO.createInstance(username, password, "locationName", schemaName, url);
    moves = MoveDAO.createInstance(username, password, "move", schemaName, url);

    //displayPath();
  }

  // Reset to original zoom
  public void reset() {
    // zoom to 1x
    gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
    gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
    gesturePane.centreOn(new Point2D(2500, 1700));
    clearPath();
  }

  public void clearPath() {
    pathPane.getChildren().clear();
    mapPane.getChildren().remove(pathPane);
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
    String start = startField.getText();
    String end = endField.getText();
    Boolean isAccessible = accessibleCheckbox.isPressed();
    System.out.println("X:" + gesturePane.getCurrentX());
    System.out.println("Y:" + gesturePane.getCurrentY());
    System.out.println("XScale:" + gesturePane.getCurrentScaleX());
    System.out.println("YScale:" + gesturePane.getCurrentScaleY());
    displayPath(start, end, isAccessible);
  }

  public void displayFloorUp() {
    if (currentFloor < 5) {
      currentFloor++;
      imageView = new ImageView(linkArray[currentFloor].toExternalForm());
      mapPane.getChildren().clear();
      mapPane.getChildren().add(imageView);
      // gesturePane.setContent(floorArray[currentFloor]); // set to ground floor
      floorText.setText(floorNames[currentFloor]);
      reset();
    }
  }

  public void displayFloorDown() {
    if (currentFloor > 0) {
      currentFloor--;
      imageView = new ImageView(linkArray[currentFloor].toExternalForm());
      mapPane.getChildren().clear();
      mapPane.getChildren().add(imageView);
      // gesturePane.setContent(floorArray[currentFloor]); // set to ground floor
      floorText.setText(floorNames[currentFloor]);
      reset();
    }
  }

  public void displayPath(String startLocation, String endLocation, Boolean accessible) throws Exception {
    clearPath();
    mapPane.getChildren().add(pathPane);

    int startID = idFromName(startLocation);
    int endID = idFromName(endLocation);

    Pathfinder pathfinder = new Pathfinder(nodes, edges, moves, locationNames);
    Path mapPath = pathfinder.aStarPath(startID, endID, accessible);
    ArrayList<Integer> currentPath = mapPath.getPath();

    Node startNode = nodes.selectNodeByID(startID);
    Node endNode = nodes.selectNodeByID(endID);

    System.out.println(endNode.getxCoord());
    System.out.println(startNode.getxCoord());

    Circle start = new Circle(startNode.getxCoord(), startNode.getyCoord(), 5, Color.RED);
    Circle end = new Circle(endNode.getxCoord(), endNode.getyCoord(), 5, Color.RED);

    pathPane.getChildren().add(start);
    pathPane.getChildren().add(end);

    for (int i = 0; i < mapPath.getPath().size() - 1; i++) {
      Node n1 = nodes.selectNodeByID(mapPath.getPath().get(i));
      Node n2 = nodes.selectNodeByID(mapPath.getPath().get(i + 1));
      Line l1 = new Line(n1.getxCoord(), n1.getyCoord(), n2.getxCoord(), n2.getyCoord());
      pathPane.getChildren().add(l1);
    }
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
}
