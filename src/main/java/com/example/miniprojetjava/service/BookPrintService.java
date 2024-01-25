package com.example.miniprojetjava.service;

import com.example.miniprojetjava.controller.LibraryController;
import com.example.miniprojetjava.model.Book;
import com.example.miniprojetjava.model.Reader;
import com.example.miniprojetjava.model.BookPrints;
import com.example.miniprojetjava.util.DatabaseConnection;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookPrintService {

    public static void addBookPrint(int bookId, int readerId) throws SQLException {
        Connection connection = DatabaseConnection.StartConnection();
        String query = "INSERT INTO printDetails(bookId, readerId, printDate) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.setInt(2, readerId);
            LocalDate currentDate = LocalDate.now();
            Date printDate = Date.valueOf(currentDate);
            preparedStatement.setDate(3, printDate);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }
    }

    public static List<BookPrints> getAllBookPrints() throws SQLException {
        List<BookPrints> bookPrints = new ArrayList<>();
        Connection connection = DatabaseConnection.StartConnection();
        String query ="SELECT bookId, readerId, printDate, returnDate FROM printDetails";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int bookId = resultSet.getInt("bookId");
                int readerId = resultSet.getInt("readerId");
                Date printDate = resultSet.getDate("printDate");
                Date returnDate = resultSet.getDate("returnDate");

                Book book = BookService.getBookById(bookId);
                Reader reader = ReaderService.getReaderById(readerId);
                BookPrints bookPrint = new BookPrints(book, reader, printDate, returnDate);
                bookPrints.add(bookPrint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }

        return bookPrints;
    }

    public static List<BookPrints> searchBookPrints (String searchText) throws SQLException {
        List<BookPrints> bookPrints = new ArrayList<>();
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT b.title, r.fullName, d.printDate, d.returnDate FROM book b, reader r, printDetails d WHERE b.bookId = d.bookId AND r.readerId = d.readerId AND (b.title LIKE ? OR r.fullName LIKE ? OR d.printDate LIKE ? OR d.returnDate LIKE ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 1; i <= 4; i++) {
                preparedStatement.setString(i, "%" + searchText + "%");
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String fullName = resultSet.getString("fullName");
                Date printDate = resultSet.getDate("printDate");
                Date returnDate = resultSet.getDate("returnDate");

                Book book = BookService.getBookByTitle(title);
                Reader reader = ReaderService.getReaderByFullName(fullName);

                BookPrints bookPrint = new BookPrints(book, reader, printDate, returnDate);
                bookPrints.add(bookPrint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }

        return bookPrints;
    }

    public static void returnBook(BookPrints selectedBookPrint) {
        LocalDate currentDate = LocalDate.now();
        Date returnDate = Date.valueOf(currentDate);

        int bookId = selectedBookPrint.getBook().getBookId();
        int readerId = selectedBookPrint.getReader().getReaderId();

        String query = "UPDATE printDetails SET returnDate = ? WHERE bookId = ? AND readerId = ?";

        try (Connection connection = DatabaseConnection.StartConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDate(1, returnDate);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setInt(3, readerId);

            int rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
