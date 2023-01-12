package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;

public class loggedInController implements Initializable {
    private final int portS = 8000 ;
    @FXML
    private TableView<ClientInfo> table ;
    @FXML
    private TableColumn<ClientInfo , String> username ;

    @FXML
    private TextArea area_msg ;

    private static final int PORTS = 8000 ;
    @FXML
    private TableColumn colEdit ;
    @FXML
    private Button button_list ;

    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;

    @FXML
    private Button button_log_out ;

    private ClientInfo c ;

    public ObservableList<ClientInfo> data = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
                data = connectionToDB.viewClient(data);
                username.setCellValueFactory(new PropertyValueFactory<ClientInfo , String>("username"));

               Callback<TableColumn<ClientInfo, String>, TableCell<ClientInfo, String>> cellfactory = (param) -> {
                      final TableCell<ClientInfo,String> cell = new TableCell<>(){
                          @Override
                          protected void updateItem(String s, boolean empty) {
                              super.updateItem(s, empty);

                              if(empty){
                                  setGraphic(null);
                                  setText(null);
                              }else {
                                  final Button show_button = new Button("SHOW");
                                  show_button.setOnAction(new EventHandler<ActionEvent>() {
                                      @Override
                                      public void handle(ActionEvent actionEvent) {
                                        c = getTableView().getItems().get(getIndex());
//                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                                        alert.setContentText("tu as clique sur le client id : "+c.getId()+" son username: "+c.getUsername());
//                                        alert.show();


                                      }
                                  });
                                  setGraphic(show_button);
                                  setText(null);
                              }
                          };
                      };
                      return cell ;
               };
                colEdit.setCellFactory(cellfactory);
                table.setItems(data);
        button_log_out.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               connectionToDB.changeView(actionEvent , "hello-view.fxml" , "Log in" , null);
            }
        });

        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String messageToSend = tf_message.getText();
                if (!messageToSend.isBlank()) {
                    String msg = c.getUsername() + "-" + messageToSend ;
                    byte[] array = msg.getBytes();
                    try {
                        DatagramSocket socket = new DatagramSocket();
                        DatagramPacket packet = new DatagramPacket(array , array.length , InetAddress.getByName("127.0.0.1") , PORTS);
                        socket.send(packet);
                        area_msg.appendText("Me : "+messageToSend+"\n");
                        tf_message.setText(" ");

                    } catch (SocketException e) {
                        throw new RuntimeException(e);
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                } } });
    }
}
