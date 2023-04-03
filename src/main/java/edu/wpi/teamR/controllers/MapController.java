package edu.wpi.teamR.controllers;

import edu.wpi.teamR.Main;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import net.kurobako.gesturefx.*;

public class MapController {
  @FXML MFXButton resetButton;
  @FXML MFXButton searchButton;
  @FXML MFXButton floorUpButton;
  @FXML MFXButton floorDownButton;
  @FXML MFXCheckbox checkbox;

  @FXML MFXTextField startField;
  @FXML MFXTextField endField;

  @FXML MFXButton homeButton;
  @FXML BorderPane borderPane;
  @FXML AnchorPane anchorPane;

  @FXML GesturePane gesturePane;

  @FXML Text floorText;

  ImageView groundFloorImage =
      new ImageView(Main.class.getResource("images/00_thegroundfloor.png").toExternalForm());
  ImageView firstFloorImage =
      new ImageView(Main.class.getResource("images/01_thefirstfloor.png").toExternalForm());
  ImageView secondFloorImage =
      new ImageView(Main.class.getResource("images/02_thesecondfloor.png").toExternalForm());
  ImageView thirdFloorImage =
      new ImageView(Main.class.getResource("images/03_thethirdfloor.png").toExternalForm());
  ImageView lowerOneImage =
      new ImageView(Main.class.getResource("images/00_thelowerlevel1.png").toExternalForm());
  ImageView lowerTwoImage =
      new ImageView(Main.class.getResource("images/00_thelowerlevel2.png").toExternalForm());

  URL groundFloorLink = Main.class.getResource("images/00_thegroundfloor.png");
  URL firstFloorLink = Main.class.getResource("images/01_thefirstfloor.png");
  URL secondFloorLink = Main.class.getResource("images/02_thesecondfloor.png");
  URL thirdFloorLink = Main.class.getResource("images/03_thethirdfloor.png");
  URL LLOneLink = Main.class.getResource("images/00_thelowerlevel1.png");
  URL LLTwoLink = Main.class.getResource("images/00_thelowerlevel2.png");

  ImageView imageView;
  int currentFloor = 2;
  ImageView[] floorArray = {
    lowerTwoImage,
    lowerOneImage,
    groundFloorImage,
    firstFloorImage,
    secondFloorImage,
    thirdFloorImage
  };

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

  @FXML
  public void initialize() {
    imageView = new ImageView(linkArray[currentFloor].toExternalForm());
    gesturePane.setContent(imageView); // set to groundfloor
    reset();
    gesturePane.setMinScale(0.25);
    gesturePane.setMaxScale(2);
    resetButton.setOnMouseClicked(event -> reset());
    searchButton.setOnMouseClicked(event -> search());
    floorDownButton.setOnMouseClicked(event -> displayFloorDown());
    floorUpButton.setOnMouseClicked(event -> displayFloorUp());
    homeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    floorText.setText(floorNames[currentFloor]);
  }

  // Reset to original zoom
  public void reset() {
    // zoom to 1x
    gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
    gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
    gesturePane.centreOn(new Point2D(2500, 1700));
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

  public void search() {
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
    Boolean isAccessible = checkbox.isPressed();
    System.out.println("X:" + gesturePane.getCurrentX());
    System.out.println("Y:" + gesturePane.getCurrentY());
    System.out.println("XScale:" + gesturePane.getCurrentScaleX());
    System.out.println("YScale:" + gesturePane.getCurrentScaleY());
  }

  public void displayFloorUp() {
    if (currentFloor < 5) {
      currentFloor++;
      imageView = new ImageView(linkArray[currentFloor].toExternalForm());
      gesturePane.setContent(imageView);
      // gesturePane.setContent(floorArray[currentFloor]); // set to ground floor
      floorText.setText(floorNames[currentFloor]);
      reset();
    }
  }

  public void displayFloorDown() {
    if (currentFloor > 0) {
      currentFloor--;
      imageView = new ImageView(linkArray[currentFloor].toExternalForm());
      gesturePane.setContent(imageView);
      // gesturePane.setContent(floorArray[currentFloor]); // set to ground floor
      floorText.setText(floorNames[currentFloor]);
      reset();
    }
  }
}
