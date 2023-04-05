package edu.wpi.teamR.fields;

import edu.wpi.teamR.database.FoodRequest;
import edu.wpi.teamR.database.FoodRequestDAO;
import edu.wpi.teamR.database.ServiceRequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.*;

public class ServiceRequestData {
  private ArrayList<MealFields> mealData;
  private ArrayList<FurnitureFields> furnitureData;

  public ServiceRequestData() {
    this.mealData = new ArrayList<MealFields>();
    this.furnitureData = new ArrayList<FurnitureFields>();
}



  public void addMealData(MealFields newField) {
    mealData.add(newField);
  }

  public void addFurnitureData(FurnitureFields newField) {
    furnitureData.add(newField);
  }
}
