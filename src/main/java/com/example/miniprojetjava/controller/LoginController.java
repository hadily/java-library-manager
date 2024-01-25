package com.example.miniprojetjava.controller;

import com.example.miniprojetjava.model.Reader;
import com.example.miniprojetjava.service.ReaderService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    public void login(ActionEvent event) {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Empty Fields", "Please fill in both username and password.");
                return;
            }

            Reader reader = ReaderService.getReaderByUsername(username);

            if (reader != null && ReaderService.checkPassword(password, reader.getPassword())) {
                goToLibrary(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Credentials", "Incorrect username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void goToLibrary(ActionEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/miniprojetjava/Library.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
