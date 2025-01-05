package com;

import com.Controller.dao.ConnectionSQL;
import javafx.application.Application;
import javafx.stage.Stage;
import  javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application{

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("./resources/login.fxml"));
        root.getStylesheets().add(String.valueOf(getClass().getResource("./resources/styles.css")));
        primaryStage.setTitle("App");
        primaryStage.setScene(new Scene(root, 500, 300));

        primaryStage.show();
    }


    public void changeScene(String fxml) throws Exception{
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        pane.getStylesheets().add(String.valueOf(getClass().getResource("./resources/styles.css")));
        stage.setHeight(600);
        stage.setWidth(800);
        stage.getScene().setRoot(pane);
    }

    public static void main(String[] args) {
        launch(args);
    }


}
