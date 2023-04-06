package edu.wpi.teamR.controllers;

import edu.wpi.teamR.database.Move;
import edu.wpi.teamR.database.MoveDAO;
import edu.wpi.teamR.database.Node;
import edu.wpi.teamR.database.NotFoundException;
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
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class UpdateMoveController{
    MoveDAO dao;
    @FXML
    MFXButton backButton;
    @FXML
    TableView<Move> nodesTable;
    @FXML
    TableColumn<Move, Integer> nodeIDColumn;
    @FXML TableColumn<Move, String> nameColumn;
    @FXML TableColumn<Move, Date> dateColumn;
    @FXML TableColumn<Move, Void> deleteColumn;


    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.EMPLOYEE));
        setTableColumns();
        
        dateColumn.setOnEditCommit(event -> {
            Move move = event.getRowValue();
            java.sql.Date sqlDate = new java.sql.Date(event.getNewValue().getTime());
            move.setMoveDate(sqlDate);
            try {
                dao.modifyMoveByID(move.getNodeID(), move.getLongName(), sqlDate);
            } catch (SQLException | ClassNotFoundException | NotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setTableColumns() throws SQLException, ClassNotFoundException {
        dao = MoveDAO.createInstance("teamr", "teamr150", "move",
                "prototype2", "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb");

        ArrayList<Move> moveList = dao.getMoves();
        ObservableList<Move> tableData = FXCollections.observableArrayList(moveList);
        nodesTable.setItems(tableData);

        nodeIDColumn.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
        nodeIDColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("longName"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("moveDate"));
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));

        deleteColumn.setCellFactory(column -> new TableCell<>() {
            private final MFXButton deleteButton = new MFXButton("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Move move = getTableView().getItems().get(getIndex());
                    java.sql.Date sqlDate = new java.sql.Date(move.getMoveDate().getTime());
                    try {
                        dao.deleteMove(move.getNodeID(), move.getLongName(), sqlDate);
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    getTableView().getItems().remove(move);
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
