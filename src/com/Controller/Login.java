package com.Controller;

import com.Main;
import com.Controller.dao.ConnectionSQL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Login {
    private Connection conn = ConnectionSQL.getConnection();

    public Login(){}

    @FXML
    private Button button;
    @FXML
    private Label wrongLogin;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    public void userLogIn(ActionEvent actionEvent){
        Main m = new Main();
        try {
            if(username.getText().isEmpty() || password.getText().isEmpty()){
                wrongLogin.setText("Please enter your credentials!");
                return;
            }
            PreparedStatement stmt = conn.prepareCall("SELECT password FROM users WHERE username = ?");
            stmt.setString(1, username.getText());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                if(rs.getString("password").equals(password.getText())){
                    m.changeScene("./resources/main.fxml");
                    wrongLogin.setText("Loged In!");
                }else{
                    wrongLogin.setText("Wrong credentials!");
                }
            }else{
                wrongLogin.setText("Wrong credentials!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            wrongLogin.setText("[!] Database ERROR!");
        }
    }
}
