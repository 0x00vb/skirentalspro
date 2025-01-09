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
    @FXML
    private Pane mainPane;

    @FXML
    private Button rentalsBtn;

    @FXML
    private Button productsBtn;

    @FXML
    public void initialize(){
        try {
            loadSection("../resources/rentalsPane.fxml"); // Load the default section
        } catch (Exception e) {
            e.printStackTrace();
        }
        rentalsBtn.setOnAction(e -> loadSection("../resources/rentalsPane.fxml"));
        productsBtn.setOnAction( e -> loadSection("../resources/productsPane.fxml") );
    }

    public void loadSection(String fxml){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Node node = loader.load();
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
