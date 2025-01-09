package com.Controller;

import com.model.User;
import com.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;

public class LoginController {
    private UsersController usersController = new UsersController();

    public LoginController(){}

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
            User user = usersController.getUser(username.getText());
            if(user != null){
                if(user.getPassword().equals(password.getText())){
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
            wrongLogin.setText("[!] Connection ERROR!");
        }
    }
}
