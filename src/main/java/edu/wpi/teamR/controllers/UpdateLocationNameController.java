package edu.wpi.teamR.controllers;

import edu.wpi.teamR.database.*;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.sql.SQLException;
import java.util.ArrayList;

public class UpdateLocationNameController{
    @FXML
    MFXButton backButton;
    @FXML
    TableView<LocationName> nodesTable;
    @FXML TableColumn<LocationName, String> longNameColumn;
    @FXML TableColumn<LocationName, String> shortNameColumn;
    @FXML TableColumn<LocationName, String> nodeTypeColumn;
    @FXML TableColumn<LocationName, Void> deleteColumn;
    LocationNameDAO dao;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        this.getLocationNames();
        backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.EMPLOYEE));

        longNameColumn.setOnEditCommit(event -> {
            LocationName locationName = event.getRowValue();
            locationName.setLongName(event.getNewValue());

            try {
                dao.modifyLocationNameByLongName(event.getNewValue(), locationName.getShortName(), locationName.getNodeType());
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        shortNameColumn.setOnEditCommit(event -> {
            LocationName locationName = event.getRowValue();
            locationName.setShortName(event.getNewValue());

            try {
                dao.modifyLocationNameByLongName(locationName.getLongName(), event.getNewValue(), locationName.getNodeType());
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        nodeTypeColumn.setOnEditCommit(event -> {
            LocationName locationName = event.getRowValue();
            locationName.setNodeType(event.getNewValue());

            try {
                dao.modifyLocationNameByLongName(locationName.getLongName(), locationName.getShortName(), event.getNewValue());
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void getLocationNames() throws SQLException, ClassNotFoundException {
        dao = LocationNameDAO.createInstance("teamr", "teamr150", "locationName",
                "prototype2", "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb");

        ArrayList<LocationName> lNList = dao.getLocationNames();

        ObservableList<LocationName> tableData =
                FXCollections.observableArrayList(lNList);

        nodesTable.setItems(tableData);

        longNameColumn.setCellValueFactory(new PropertyValueFactory<>("longName"));
        longNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        shortNameColumn.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        shortNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nodeTypeColumn.setCellValueFactory(new PropertyValueFactory<>("nodeType"));
        nodeTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        deleteColumn.setCellFactory(column -> new TableCell<>() {
            private final MFXButton deleteButton = new MFXButton("Delete");
            {
                deleteButton.setOnAction(event -> {
                    LocationName locationName = getTableView().getItems().get(getIndex());
                    try {
                        dao.deleteLocationNames(locationName.getLongName(), locationName.getShortName(), locationName.getNodeType());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    getTableView().getItems().remove(locationName);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

    }
}
