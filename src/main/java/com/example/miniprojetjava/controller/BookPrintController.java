package com.example.miniprojetjava.controller;

import com.example.miniprojetjava.model.Book;
import com.example.miniprojetjava.model.BookPrints;
import com.example.miniprojetjava.model.Reader;
import com.example.miniprojetjava.service.BookPrintService;
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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookPrintController implements Initializable {
    @FXML private TableView<BookPrints> bookPrintTable;
    @FXML private TableColumn<BookPrints, String> bookTitleColumn;
    @FXML private TableColumn<BookPrints, String> readerNameColumn;
    @FXML private TableColumn<BookPrints, Date> printDateColumn;
    @FXML private TableColumn<BookPrints, Date> returnDateColumn;
    @FXML private TextField searchField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookTitleColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue().getBook();
            if (book != null) {
                return new SimpleStringProperty(book.getTitle());
            } else {
                return new SimpleStringProperty("");
            }
        });
        readerNameColumn.setCellValueFactory(cellData -> {
            Reader reader = cellData.getValue().getReader();
            if (reader != null) {
                return new SimpleStringProperty(reader.getFullName());
            } else {
                return new SimpleStringProperty("");
            }
        });
        printDateColumn.setCellValueFactory(new PropertyValueFactory<>("printDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        try {
            loadBookPrints();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBookPrints() throws SQLException {
        List<BookPrints> bookPrints = BookPrintService.getAllBookPrints();
        ObservableList<BookPrints> bookPrintsObservableList = FXCollections.observableArrayList(bookPrints);
        bookPrintTable.setItems((bookPrintsObservableList));
    }


    public void searchBookPrint() {
        try {
            String searchText = searchField.getText();
            List<BookPrints> searchResults = BookPrintService.searchBookPrints(searchText);
            ObservableList<BookPrints> bookPrintObservableList = FXCollections.observableArrayList(searchResults);
            bookPrintTable.setItems(bookPrintObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void goToPrintBook(ActionEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/miniprojetjava/printBook.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred. Please contact support.");
            Logger.getLogger(BookPrintController.class.getName()).log(Level.SEVERE, "Unexpected error", e);
        }
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

    @FXML
    public void returnBook() throws SQLException {
        BookPrints selectedBookPrint = bookPrintTable.getSelectionModel().getSelectedItem();

        if (selectedBookPrint != null) {
            BookPrintService.returnBook(selectedBookPrint);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selected Book", "Please select a book to return!");
        }

        loadBookPrints();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
