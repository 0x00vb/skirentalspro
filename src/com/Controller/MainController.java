package com.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

public class MainController {
    private static MainController instance;
    @FXML
    private Pane mainPane;

    @FXML
    private Button rentalsBtn;
    @FXML
    private Button productsBtn;
    @FXML
    private Button clientsBtn;


    @FXML
    public void initialize(){
        instance = this;
        try {
            loadSection("../resources/rentalsPane.fxml"); // Load the default section
        } catch (Exception e) {
            e.printStackTrace();
        }
        rentalsBtn.setOnAction(e -> loadSection("../resources/rentalsPane.fxml"));
        productsBtn.setOnAction( e -> loadSection("../resources/productsPane.fxml") );
        clientsBtn.setOnAction( e -> loadSection("../resources/clientsPane.fxml") );

    }
    public static MainController getInstance() {
        return instance;
    }
    public void loadSection(String fxml) {
        loadSection(fxml, null);
    }

    public void loadSection(String fxml, Object optionalData){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Node node = loader.load();

            if(optionalData != null){
                RentalsController.getInstance().showClientRentals((Integer) optionalData);
            }

            mainPane.getChildren().clear();
            mainPane.getChildren().add(node);
        }catch (Exception err){
            err.printStackTrace();
            // Add error alert to show user when loading fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to load section: " + err.getMessage());
            alert.showAndWait();
        }
    }
}
