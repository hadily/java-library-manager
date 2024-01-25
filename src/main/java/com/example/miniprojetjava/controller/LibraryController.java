package com.example.miniprojetjava.controller;

import com.example.miniprojetjava.model.Author;
import com.example.miniprojetjava.model.Book;
import com.example.miniprojetjava.service.BookService;

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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LibraryController implements Initializable {

    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> isbnCodeColumn;
    @FXML private TableColumn<Book, String> coverColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> categoryColumn;
    @FXML private TableColumn<Book, String> descriptionColumn;
    @FXML private TextField searchField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isbnCodeColumn.setCellValueFactory(new PropertyValueFactory<>("isbnCode"));
        coverColumn.setCellValueFactory(new PropertyValueFactory<>("bookCover"));
        coverColumn.setCellFactory(column -> new TableCell<>() {
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
                            imageView.setFitWidth(75);
                            imageView.setFitHeight(90);
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
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(cellData -> {
            Author author = cellData.getValue().getAuthor();
            if (author != null) {
                return new SimpleStringProperty(author.getFirstName() + " " + author.getLastName());
            } else {
                return new SimpleStringProperty("");
            }
        });
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        try {
            loadBooks();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBooks() throws SQLException {
        List<Book> books = BookService.getAllBooks();
        ObservableList<Book> bookObservableList = FXCollections.observableArrayList(books);
        bookTable.setItems(bookObservableList);
    }

    @FXML
    public void goToAddBook(ActionEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/miniprojetjava/addBook.fxml");
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
    public void deleteBook() throws SQLException {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            showConfirmationAlert(selectedBook);
        }
    }

    private void showConfirmationAlert(Book selectedBook) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Book");
        alert.setHeaderText("Delete '" + selectedBook.getTitle() + "' from library?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            BookService.deleteSelectedBook(selectedBook.getBookId());
            loadBooks();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selected Book", "Please select a book to return!");
        }
    }

    public void logOut(ActionEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/miniprojetjava/Login.fxml");
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
    public void searchBook() {
        try {
            String searchText = searchField.getText();
            List<Book> searchResults = BookService.searchBooks(searchText);
            ObservableList<Book> bookObservableList = FXCollections.observableArrayList(searchResults);
            bookTable.setItems(bookObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void goToViewBookPrints(ActionEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/miniprojetjava/viewBookPrints.fxml");
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