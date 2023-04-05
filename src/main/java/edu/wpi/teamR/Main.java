package edu.wpi.teamR;

import edu.wpi.teamR.database.*;

import java.sql.SQLException;

public class Main {
  public Main() throws SQLException, ClassNotFoundException {
  }

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    String username = "teamr";
    String password = "teamr150";
    String schemaName = "prototype2";
    String url = "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb";
    NodeDAO nodeDAO = NodeDAO.createInstance(username, password, "node", schemaName, url);
    EdgeDAO edgeDAO = EdgeDAO.createInstance(username, password, "edge", schemaName, url);
    MoveDAO moveDAO = MoveDAO.createInstance(username, password, "move", schemaName, url);
    LocationNameDAO locationNameDAO = LocationNameDAO.createInstance(username, password, "locationname", schemaName, url);
    FoodRequestDAO foodRequestDAO = FoodRequestDAO.createInstance(username, password, "foodRequestview", schemaName, url);
    FurnitureRequestDAO furnitureRequestDAO = FurnitureRequestDAO.createInstance(username, password, "furnitureRequestview", schemaName, url);
    App.launch(App.class, args);
    // shortcut: psvm
  }
}
