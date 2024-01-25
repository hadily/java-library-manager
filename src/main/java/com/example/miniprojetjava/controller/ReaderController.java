package com.example.miniprojetjava.controller;

import com.example.miniprojetjava.model.Reader;
import com.example.miniprojetjava.service.ReaderService;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReaderController implements Initializable {

    @FXML private TableView<Reader> readerTable;
    @FXML private TableColumn<Reader, String> pictureColumn;
    @FXML private TableColumn<Reader, String> usernameColumn;
    @FXML private TableColumn<Reader, String> fullNameColumn;
    @FXML private TableColumn<Reader, String> CINColumn;
    @FXML private TableColumn<Reader, Integer> ageColumn;
    @FXML private TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pictureColumn.setCellValueFactory(new PropertyValueFactory<>("picture"));
        pictureColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    try {
                        File file = new File(imagePath);
                        if (file.exists()) {
                            Image image = new Image(file.toURI().toString());
                            imageView.setImage(image);
                            imageView.setFitWidth(90);
                            imageView.setFitHeight(80);
                            setGraphic(imageView);
                        } else {
                            setGraphic(null);
                        }
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        CINColumn.setCellValueFactory(new PropertyValueFactory<>("CIN"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        try {
            loadReaders();
        } catch (Exception e) {
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

    private void loadReaders() throws SQLException {
        List<Reader> readers = ReaderService.getAllReaders();
        ObservableList<Reader> readerObservableList = FXCollections.observableArrayList(readers);
        readerTable.setItems(readerObservableList);
    }

    @FXML
    public void deleteReader() throws SQLException {
        Reader selectedReader = readerTable.getSelectionModel().getSelectedItem();

        if (selectedReader != null) {
            showConfirmationAlert(selectedReader);
        }
    }

    private void showConfirmationAlert(Reader selectedReader) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Reader");
        alert.setHeaderText("Delete '" + selectedReader.getUsername() + "' from readers?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            ReaderService.deleteSelectedReader(selectedReader.getReaderId());
            loadReaders();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selected Book", "Please select a book to return!");
        }
    }


    @FXML
    public void searchReader() {
        try {
            String searchText = searchField.getText();
            List<Reader> searchResults = ReaderService.searchReaders(searchText);
            ObservableList<Reader> readerObservableList = FXCollections.observableArrayList(searchResults);
            readerTable.setItems(readerObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
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

    @FXML
    public void addReader(ActionEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/miniprojetjava/addReader.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToMemberships(ActionEvent event) {
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

}
