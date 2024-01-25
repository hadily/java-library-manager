package com.example.miniprojetjava.controller;


import com.example.miniprojetjava.service.BookPrintService;
import com.example.miniprojetjava.service.BookService;
import com.example.miniprojetjava.service.ReaderService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;


import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddBookPrintController {
    @FXML private TextField bookNameField;
    @FXML private TextField readerUsernameField;


    @FXML
    private void printBook(ActionEvent event) {
        try {
            String bookTitle = bookNameField.getText();
            String readerUsername = readerUsernameField.getText();

            if (bookTitle.isEmpty() && readerUsername.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Empty Fields", "Please fill in both book title and reader username.");
            } else if (BookService.getBookByTitle(bookTitle) == null){
                showAlert(Alert.AlertType.WARNING, "Unknown Book", "Book '" + bookTitle + "' does not exist.");
            } else if (ReaderService.getReaderByUsername(readerUsername) == null ) {
                showAlert(Alert.AlertType.WARNING, "Unknown Reader", "Reader with username '" + readerUsername + "' does not exist.");
            } else {
                int bookId = BookService.getIdByTitle(bookTitle);
                int readerId = ReaderService.getIdByUsername(readerUsername);
                BookPrintService.addBookPrint(bookId, readerId);
                showAlert(Alert.AlertType.INFORMATION, "Book Printed", "The book has been printed successfully.");
                backToViewBookPrints(event);
            }
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

    @FXML
    private void backToViewBookPrints(ActionEvent event) {
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

}
