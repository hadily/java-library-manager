package com.example.miniprojetjava.controller;

import com.example.miniprojetjava.model.Membership;
import com.example.miniprojetjava.model.Reader;
import com.example.miniprojetjava.service.MembershipService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MembershipController implements Initializable {

    @FXML private TableView<Membership> membershipTable;
    @FXML private TableColumn<Membership, String> readerColumn;
    @FXML private TableColumn<Membership, String> statusColumn;
    @FXML private TableColumn<Membership, Date> creationDateColumn;
    @FXML private TableColumn<Membership, Date> endDateColumn;
    @FXML private TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        readerColumn.setCellValueFactory(cellData -> {
            Reader reader = cellData.getValue().getReader();
            if (reader != null) {
                return new SimpleStringProperty(reader.getFullName());
            } else {
                return new SimpleStringProperty("");
            }
        });
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        try {
            loadMemberships();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMemberships() throws SQLException {
        List<Membership> memberships = MembershipService.getAllMemberships();
        ObservableList<Membership> bookPrintsObservableList = FXCollections.observableArrayList(memberships);
        membershipTable.setItems((bookPrintsObservableList));
    }


    public void searchMembership() {
        try {
            String searchText = searchField.getText();
            List<Membership> searchResults = MembershipService.searchMemberships(searchText);
            ObservableList<Membership> membershipObservableList = FXCollections.observableArrayList(searchResults);
            membershipTable.setItems(membershipObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addMembership(ActionEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/miniprojetjava/addMembership.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteMembership() throws SQLException {
        Membership selectedMembership = membershipTable.getSelectionModel().getSelectedItem();
        if (selectedMembership != null) {
            showConfirmationAlert(selectedMembership);
        }
    }

    private void showConfirmationAlert(Membership selectedMembership) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Membership");
        alert.setHeaderText("Delete '" + selectedMembership.getReader().getFullName() + "' membership?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            int readerId = selectedMembership.getReader().getReaderId();
            MembershipService.deleteSelectedMembership(readerId);
            loadMemberships();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selected Membership", "Please select a membership to delete!");
        }
    }

    public void updateStatus() {
        Membership selectedMembership = membershipTable.getSelectionModel().getSelectedItem();
        if (selectedMembership != null) {
            showUpdateAlert(selectedMembership);
        }
    }


    public void showUpdateAlert(Membership selectedMembership) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Membership Status");
        alert.setHeaderText("Update '" + selectedMembership.getReader().getFullName() + "' membership status?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                MembershipService.updateMembershipStatus(selectedMembership);
                loadMemberships();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selected Membership", "Please select a membership to update!");
        }
    }

    public void goToReader(ActionEvent event) {
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


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
