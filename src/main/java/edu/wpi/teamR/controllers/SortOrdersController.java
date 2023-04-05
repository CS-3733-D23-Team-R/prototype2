
package edu.wpi.teamR.controllers;

import edu.wpi.teamR.database.*;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.sql.SQLException;
import java.util.ArrayList;


public class SortOrdersController {

  @FXML TableView<ServiceRequest> requestTable;
  @FXML TableColumn<ServiceRequest, Integer> idColumn;
  @FXML TableColumn<ServiceRequest, String> nameColumn;
  @FXML TableColumn<ServiceRequest, String> locationColumn;
  @FXML TableColumn<ServiceRequest, String> requestTypeColumn;
  @FXML TableColumn<ServiceRequest, String> notesColumn;
  @FXML TableColumn<ServiceRequest, String> staffMemberColumn;
  @FXML TableColumn<ServiceRequest, String> timeColumn;
  @FXML TableColumn<ServiceRequest, String> statusColumn;
  @FXML MFXButton backButton;
  FoodRequestDAO dao;
  ObservableList<RequestStatus> statusList = FXCollections.observableArrayList(RequestStatus.values());


  @FXML
  public void initialize() {
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.EMPLOYEE));

    idColumn.setCellValueFactory(new PropertyValueFactory<>("requestID"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("requesterName"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    staffMemberColumn.setCellValueFactory(new PropertyValueFactory<>("staffMember"));
    staffMemberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    notesColumn.setCellValueFactory(new PropertyValueFactory<>("additionalNotes"));
    timeColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
    //statusColumn.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
    requestTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));
    requestTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());


    for (ServiceRequest foodRequest : FoodRequestDAO.getInstance().getFoodRequests()) {
      requestTable.getItems().add(foodRequest);
    }

    for (ServiceRequest furnRequest : FurnitureRequestDAO.getInstance().getFurnitureRequests()) {
      requestTable.getItems().add(furnRequest);
    }


    staffMemberColumn.setOnEditCommit(event -> {
      ServiceRequest request = event.getRowValue();
      request.setStaffMember(event.getNewValue());
      try {
        FoodRequestDAO.getInstance().modifyFoodRequestByID(request.getRequestID(), request.getRequesterName(),request.getLocation(),request.getItemType(), event.getNewValue(), request.getAdditionalNotes(), request.getRequestDate(), request.getRequestStatus());
      } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }

    });

    //dao = FoodRequestDAO.createInstance(""

    statusColumn.setCellFactory(column -> new TableCell<>() {
      private final MFXComboBox<RequestStatus> changeStatusButton = new MFXComboBox<RequestStatus>(statusList);
      {
        //changeStatusButton.setText();
        changeStatusButton.setOnAction(event -> {
          FoodRequest request = (FoodRequest) getTableView().getItems().get(getIndex());
          try {
            RequestStatus status = changeStatusButton.getSelectionModel().getSelectedItem();
            request.setRequestStatus(status);
            FoodRequestDAO.getInstance().modifyFoodRequestByID(request.getRequestID(), request.getRequesterName(), request.getLocation(), request.getMealType(), request.getStaffMember(), request.getAdditionalNotes(), request.getRequestDate(), status);
          } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
          }
        });
      }
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setGraphic(null);
        } else {
          setGraphic(changeStatusButton);
        }
      }
    });

  }
}
