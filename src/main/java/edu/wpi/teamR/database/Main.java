package edu.wpi.teamR.database;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    static NodeDAO nodeDAO;
    static EdgeDAO edgeDAO;
    static LocationNameDAO locationNameDAO;
    static MoveDAO moveDAO;
    static FoodRequestDAO foodRequestDAO;
    static FurnitureRequestDAO furnitureRequestDAO;

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        String username = "teamr";
        String password = "teamr150";
        String schemaName = "prototype2";
        String url = "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb";
        nodeDAO = NodeDAO.createInstance(username, password, "node", schemaName, url);
        edgeDAO = EdgeDAO.createInstance(username, password, "edge", schemaName, url);
        moveDAO = MoveDAO.createInstance(username, password, "move", schemaName, url);
        locationNameDAO = LocationNameDAO.createInstance(username, password, "locationname", schemaName, url);
        foodRequestDAO = FoodRequestDAO.createInstance(username, password, "foodRequestview", schemaName, url);
        furnitureRequestDAO = FurnitureRequestDAO.createInstance(username, password, "furnitureRequestview", schemaName, url);

        edgeDAO.deleteAllEdges();
        moveDAO.deleteMove(null, null, null);

        nodeDAO.readCSV("C:/Users/Nath/Desktop/School/Soft Eng/Prototype 2/databaseData/Node.csv");
        edgeDAO.readCSV("C:/Users/Nath/Desktop/School/Soft Eng/Prototype 2/databaseData/Edge.csv");
        locationNameDAO.readCSV("C:/Users/Nath/Desktop/School/Soft Eng/Prototype 2/databaseData/LocationName.csv");
        moveDAO.readCSV("C:/Users/Nath/Desktop/School/Soft Eng/Prototype 2/databaseData/Move.csv");

        nodeDAO.writeCSV("C:/Users/Nath/Desktop/School/Soft Eng/Prototype 2/databaseData/WrittenNode.csv");
        edgeDAO.writeCSV("C:/Users/Nath/Desktop/School/Soft Eng/Prototype 2/databaseData/WrittenEdge.csv");
        locationNameDAO.writeCSV("C:/Users/Nath/Desktop/School/Soft Eng/Prototype 2/databaseData/WrittenLocationName.csv");
        moveDAO.writeCSV("C:/Users/Nath/Desktop/School/Soft Eng/Prototype 2/databaseData/WrittenMove.csv");
    }
}