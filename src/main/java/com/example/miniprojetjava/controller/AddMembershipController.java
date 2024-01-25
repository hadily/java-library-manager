package com.example.miniprojetjava.controller;

import com.example.miniprojetjava.service.MembershipService;
import com.example.miniprojetjava.service.ReaderService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddMembershipController {

    @FXML
    private TextField readerNameField;
    @FXML private TextField periodField;

    public void goToMembershipView(ActionEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/miniprojetjava/Memberships.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMembership(ActionEvent event) {
        try {
            String readerName = readerNameField.getText();
            String period = periodField.getText();

            if (!readerName.isEmpty() && !period.isEmpty()) {
                if (Integer.parseInt(period) <= 0) {
                    showAlert(Alert.AlertType.WARNING, "Invalid Period", "Membership period should be greater than 0.");
                } else if (ReaderService.getReaderByUsername(readerName) == null) {
                    showAlert(Alert.AlertType.WARNING, "Unknown Reader", "Reader with username '" + readerName + "' does not exist.");
                } else {
                    MembershipService.insertMembership(readerName, period);
                    showAlert(Alert.AlertType.INFORMATION, "Membership Created", "The membership has been created successfully.");
                    goToMembershipView(event);
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Empty Fields", "Please fill in all required fields");
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
}
