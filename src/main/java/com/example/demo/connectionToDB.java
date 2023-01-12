package com.example.demo;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;

public class connectionToDB {

    // cette methode pour changer le contenu de la page apres log in par les informations du client qu'il vient de s'authentifier
    public static String client_log = null ;
    public static int client_log_id = 0 ;
    public static final int portServer = 8000 ;

    public static ArrayList<ClientInfo> clients ;

    public static void changeView(ActionEvent actionEvent , String FxmlFile , String title , String username){
        Parent root = null ;

        if(username != null){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(connectionToDB.class.getResource(FxmlFile));
                root = fxmlLoader.load();
                loggedInController logInController = fxmlLoader.getController();
               // logInController.setClientInformation(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                root = FXMLLoader.load(connectionToDB.class.getResource(FxmlFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root , 600 , 400));
        stage.show();
    }

    public static void signUpClient(ActionEvent actionEvent , String username , String password)  {
        Connection connection = null ;
        PreparedStatement preparedStatementInsert = null ;
        PreparedStatement preparedStatementExists = null ;
        PreparedStatement preparedStatementget = null ;
        ResultSet resultSet = null ;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_application", "root","system");
            preparedStatementExists = connection.prepareStatement("SELECT idClient , username from client where username = ?");
            preparedStatementExists.setString(1,username);
            resultSet = preparedStatementExists.executeQuery();

            if(resultSet.isBeforeFirst()){
                System.out.println("client deja existe");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Vous ne pouvez pas utiliser cet username , il est deja existe");
                alert.show();

            }else {
                preparedStatementInsert = connection.prepareStatement("INSERT INTO client (username , password) VALUES (? , ?)");
                preparedStatementInsert.setString(1,username);
                preparedStatementInsert.setString(2 , password);
                preparedStatementInsert.executeUpdate();
                preparedStatementget = connection.prepareStatement("SELECT idClient from client WHERE username = ?");
                preparedStatementget.setString(1,username);
                resultSet = preparedStatementget.executeQuery();
                //client_log = username ;
                while (resultSet.next()){
                    client_log_id = resultSet.getInt( "idClient");
                }

                changeView(actionEvent , "hello-view.fxml" , "Welcome" , username);
            }
        }catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if(preparedStatementInsert != null){
                try {
                    preparedStatementInsert.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if(preparedStatementExists != null){
                try {
                    preparedStatementExists.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static void loginClient(ActionEvent actionEvent , String username , String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_application", "root", "system");
            preparedStatement = connection.prepareStatement("SELECT * FROM client WHERE username = ?");
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("client n'existe pas dans la base de donnees");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("les informations incorrectes");
                alert.show();

            }else {

                while (resultSet.next()){
                    String passwordfordb = resultSet.getString("password");
                    client_log_id = resultSet.getInt("idClient");
                    if(passwordfordb.equals(password)){
                        client_log = username ;
                        String info = resultSet.getInt("idClient")+"/"+resultSet.getString("username");
                        Client client = new Client();
                        client.sendMessageToServer(info);
                        changeView(actionEvent , "Logged-in.fxml" , "Welcome" , username);

                    }else {
                        System.out.println("password incorrecte");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("les informations incorrectes");
                        alert.show();
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static void information(String username){
        client_log = username ;

    }

    public static void Session(ArrayList<ClientInfo> data){
        clients = data ;
    }

    public static ObservableList<ClientInfo> viewClient(ObservableList<ClientInfo> data){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_application", "root", "system");
            preparedStatement = connection.prepareStatement(" SELECT * FROM client , amitie  WHERE ((id_client1 = idClient ) or (id_client2 = idClient )) and ((id_client1 = ?) or (id_client2 = ?)) and username != ?");
            preparedStatement.setInt(1,client_log_id);
            preparedStatement.setInt(2,client_log_id);
            preparedStatement.setString(3,client_log);
            resultSet = preparedStatement.executeQuery();
            System.out.println(client_log_id);
            System.out.println(client_log);

            while(resultSet.next()){
                data.add(new ClientInfo(resultSet.getInt(1) , resultSet.getString(2)));
            }

            return data ;
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } }
