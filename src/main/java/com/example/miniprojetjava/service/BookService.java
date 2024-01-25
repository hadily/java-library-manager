package com.example.miniprojetjava.service;

import com.example.miniprojetjava.model.Book;
import com.example.miniprojetjava.util.DatabaseConnection;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookService {
    public static void insertBook(String title, int isbn, String coverPath, String description, String authorFirstName, String authorLastName, String category) throws SQLException {
        Connection connection = DatabaseConnection.StartConnection();
        String queryAuthor = "INSERT INTO author(firstName, lastName) VALUES (?, ?)";
        String queryBook = "INSERT INTO book(title, isbnCode, authorId, bookCover, description, category) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            int authorId = getAuthorId(authorFirstName, authorLastName);

            if (authorId == 0) {
                try (PreparedStatement authorStatement = connection.prepareStatement(queryAuthor, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    authorStatement.setString(1, authorFirstName);
                    authorStatement.setString(2, authorLastName);
                    authorStatement.executeUpdate();

                    ResultSet generatedKeys = authorStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        authorId = generatedKeys.getInt(1);
                    }
                }
            }

            try (PreparedStatement bookStatement = connection.prepareStatement(queryBook)) {
                bookStatement.setString(1, title);
                bookStatement.setInt(2, isbn);
                bookStatement.setInt(3, authorId);
                bookStatement.setString(4, coverPath);
                bookStatement.setString(5, description);
                bookStatement.setString(6, category);
                bookStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }
    }

    private static int getAuthorId(String firstName, String lastName) throws SQLException {
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT authorId FROM author WHERE firstName = ? AND lastName = ?";
        int authorId = 0;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                authorId = resultSet.getInt("authorId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }

        return authorId;
    }

    public static List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT b.bookId, b.isbnCode, b.title, a.firstName, a.lastName, b.bookCover, b.description, b.category FROM book b JOIN author a ON b.authorId = a.authorId";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int bookId = resultSet.getInt("bookId");
                int isbnCode = resultSet.getInt("isbnCode");
                String title = resultSet.getString("title");
                String authorFirstName = resultSet.getString("firstName");
                String authorLastName = resultSet.getString("lastName");
                String bookCover = resultSet.getString("bookCover");
                String description = resultSet.getString("description");
                String category = resultSet.getString("category");

                Book book = new Book(bookId, isbnCode, title, bookCover, description, authorFirstName, authorLastName, category);
                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }

        return books;
    }

    public static void deleteSelectedBook(int bookId) {
        String printDetailsQuery = "DELETE FROM printdetails WHERE bookId = ?";
        try (Connection connection = DatabaseConnection.StartConnection();
             PreparedStatement printDetailsStatement = connection.prepareStatement(printDetailsQuery)) {
            printDetailsStatement.setInt(1, bookId);
            printDetailsStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Then delete the book
        String bookQuery = "DELETE FROM book WHERE bookId = ?";
        try (Connection connection = DatabaseConnection.StartConnection();
             PreparedStatement bookStatement = connection.prepareStatement(bookQuery)) {
            bookStatement.setInt(1, bookId);
            int rowsAffectedBook = bookStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static List<Book> searchBooks(String searchText) throws SQLException {
        List<Book> books = new ArrayList<>();
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT b.bookId, b.isbnCode, b.title, a.firstName, a.lastName, b.bookCover, b.description, b.category FROM book b JOIN author a ON b.authorId = a.authorId " +
                "WHERE b.title LIKE ? OR a.firstName LIKE ? OR a.lastName LIKE ? OR b.category LIKE ? OR isbnCode LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + searchText + "%");
            preparedStatement.setString(2, "%" + searchText + "%");
            preparedStatement.setString(3, "%" + searchText + "%");
            preparedStatement.setString(4, "%" + searchText + "%");
            preparedStatement.setString(5, "%" + searchText + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int bookId = resultSet.getInt("bookId");
                int isbnCode = resultSet.getInt("isbnCode");
                String title = resultSet.getString("title");
                String authorFirstName = resultSet.getString("firstName");
                String authorLastName = resultSet.getString("lastName");
                String bookCover = resultSet.getString("bookCover");
                String description = resultSet.getString("description");
                String category = resultSet.getString("category");

                Book book = new Book(bookId, isbnCode, title, bookCover, description, authorFirstName, authorLastName, category);
                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }

        return books;
    }

    public static int getIdByTitle(String title) throws SQLException {
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT bookId FROM book WHERE title = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("bookId");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }
        return 0;
    }

    public static Book getBookById(int bookId) throws SQLException {
        Book book;
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT * FROM book b, author a WHERE bookId = ? && b.authorId = a.authorId";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String title  = resultSet.getString("title");
                    int isbnCode  = resultSet.getInt("isbnCode");
                    String authorFirstName = resultSet.getString("firstName");
                    String authorLastName = resultSet.getString("lastName");
                    String bookCover = resultSet.getString("bookCover");
                    String description = resultSet.getString("description");
                    String category = resultSet.getString("category");

                    book = new Book(bookId, isbnCode, title, bookCover, description, authorFirstName, authorLastName, category);
                    return book;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }
        return null;
    }

    public static Book getBookByTitle(String title) throws SQLException {
        Book book;
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT * FROM book b, author a WHERE title = ? && b.authorId = a.authorId";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int bookId = resultSet.getInt("bookId");
                    int isbnCode = resultSet.getInt("isbnCode");
                    String authorFirstName = resultSet.getString("firstName");
                    String authorLastName = resultSet.getString("lastName");
                    String bookCover = resultSet.getString("bookCover");
                    String description = resultSet.getString("description");
                    String category = resultSet.getString("category");

                    book = new Book(bookId, isbnCode, title, bookCover, description, authorFirstName, authorLastName, category);
                    return book;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }
        return null;
    }

    private static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
