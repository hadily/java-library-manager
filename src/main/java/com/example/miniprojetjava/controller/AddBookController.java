package com.example.miniprojetjava.controller;

import com.example.miniprojetjava.service.BookService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AddBookController {
    @FXML private TextField isbnField;
    @FXML private TextField titleField;
    @FXML private TextField authorFirstNameField;
    @FXML private TextField authorLastNameField;
    @FXML private TextField descriptionField;
    @FXML private Label coverPathLabel;
    @FXML private TextField categoryField;

    @FXML
    private void chooseCover() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Book Cover");
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            coverPathLabel.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void addBook(ActionEvent event) {
        try {
            String title = titleField.getText();
            String authorFirstName = authorFirstNameField.getText();
            String authorLastName = authorLastNameField.getText();
            String description = descriptionField.getText();
            String coverPath = coverPathLabel.getText();
            String category = categoryField.getText();
            String isbnText = isbnField.getText();

            if (!title.isEmpty() && !authorFirstName.isEmpty() && !authorLastName.isEmpty() && !coverPath.isEmpty() && !isbnText.isEmpty()) {
                int isbnCode = Integer.parseInt(isbnText);

                if (isbnCode > 0) {
                    BookService.insertBook(title, isbnCode, coverPath, description, authorFirstName, authorLastName, category);
                    showAlert(Alert.AlertType.INFORMATION, "Book Added", "The book " + title + " has been added successfully.");
                    backToLibrary(event);
                } else {
                    showAlert(Alert.AlertType.WARNING, "Invalid ISBN Code", "Please enter a valid ISBN Code for your book.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Empty Fields", "Please fill in all required fields!");
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

    @FXML
    private void backToLibrary(ActionEvent event) {
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
