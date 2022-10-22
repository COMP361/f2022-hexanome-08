package com.result.splendorguim3;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class SplendorController {
    @FXML
    private Button logInButton;
    @FXML
    private BorderPane gameBoardContent;
    @FXML
    private Pane purchaseContent;

    @FXML
    private Button quitGameButton;

    @FXML
    private TextField userName;

    @FXML
    private PasswordField userPassword;

    @FXML
    private Label logInPageErrorMessage;

    @FXML
    protected void onLogInButtonClick() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("splendor_game_board.fxml")));
        Stage window = (Stage) logInButton.getScene().getWindow();

        /*
        TODO: before set up and display the scene, should check logging condition correctness
         */

        String userNameStr = userName.getText();
        String userPasswordStr = userPassword.getText();


        // For the sake of simplicity, we only check if password and username exist
        if (Objects.equals(userNameStr, "") || Objects.equals(userPasswordStr, "")){
            logInPageErrorMessage.setText("Please enter a username and password");
        }

        else {
            window.setTitle("Splendor Game Board");
            window.setScene(new Scene(root, 600,400));
        }

    }

    @FXML
    protected void onLogOutMenuItemClick() throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("splendor.fxml")));
        // get the scene through gameBoardContent, which is at the same Scene as the MeueItem
        Stage window = (Stage) gameBoardContent.getScene().getWindow();
        window.setTitle("Welcome to Splendor!");
        window.setScene(new Scene(root, 600,400));
    }

    @FXML
    protected void onQuitGameButtonClick(){
        Stage window = (Stage) quitGameButton.getScene().getWindow();
        window.close();
    }

    @FXML
    protected void onDevelopmentCardShapeClick() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("splendor_purchase.fxml")));
        Stage stage = new Stage();
        stage.setTitle("Make your decision");
        stage.setScene(new Scene(root, 360, 170));
        stage.show();
    }


    @FXML
    protected void madePurchase(){
        Stage window = (Stage) purchaseContent.getScene().getWindow();

        /* TODO: Check condition if the purchase can be done successfully
            then close the window
         */
        window.close();
    }

    @FXML
    protected void cancelAction(){
        Stage window = (Stage) purchaseContent.getScene().getWindow();
        // Just close the window without doing anything
        window.close();

    }

    @FXML
    protected void madeReserve(){
        Stage window = (Stage) purchaseContent.getScene().getWindow();

        /* TODO: Check condition if the reserve can be done successfully
            then close the window
         */
        window.close();

    }
}