package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientInfoController implements Initializable {

    @FXML
    private TableView<ClientInfo> table ;
    @FXML
    private TableColumn<ClientInfo , String> username ;

    @FXML
    private Button button_list ;

    public ObservableList<ClientInfo> data = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
          button_list.setOnAction(new EventHandler<ActionEvent>() {
              @Override
              public void handle(ActionEvent actionEvent) {
                 data = connectionToDB.viewClient(data);
                 username.setCellValueFactory(new PropertyValueFactory<ClientInfo , String>("username"));
                 table.setItems(data);
              }
          });
    }
}
