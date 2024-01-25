package com.example.miniprojetjava.controller;

import com.example.miniprojetjava.service.ReaderService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddReaderController {
    @FXML private TextField usernameField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField CINField;
    @FXML private DatePicker birthDatePicker;
    @FXML private Label picturePathField;

    @FXML
    public void choosePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Picture");
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            picturePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    public void addReader(ActionEvent event) {
        try {
            String username = usernameField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String cin = CINField.getText();
            LocalDate birthDate = birthDatePicker.getValue();
            String picturePath = picturePathField.getText();
            String cinText = CINField.getText();

            if (!cinText.isEmpty()) {
                int cinField = Integer.parseInt(cinText);

                if (!username.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !cin.isEmpty() && birthDate != null && !picturePath.isEmpty()) {
                    if (cinText.length() != 8) {
                        showAlert(Alert.AlertType.WARNING, "Invalid CIN Number", "CIN Number should be equal to 8.");
                    } else {
                        ReaderService.addReader(username, firstName, lastName, birthDate, picturePath, cinField);
                        showAlert(Alert.AlertType.WARNING, "Reader Added", "Reader Successfully Added!");
                        backToReader(event);
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Empty Fields", "Please fill in all required fields!");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Empty CIN", "Please enter a CIN for the reader.");
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

    public void backToReader(ActionEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/miniprojetjava/Reader.fxml");
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
