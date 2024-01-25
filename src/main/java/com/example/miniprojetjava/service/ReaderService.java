package com.example.miniprojetjava.service;

import com.example.miniprojetjava.model.Reader;
import com.example.miniprojetjava.model.Membership;
import com.example.miniprojetjava.util.DatabaseConnection;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class ReaderService {

    public static void addReader(String username, String firstName, String lastName, LocalDate birthDate, String picture, int cin) throws SQLException {
        Connection connection = DatabaseConnection.StartConnection();
        String query = "INSERT INTO reader(username, fullName, birthDate, picture, CIN) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, firstName + " " + lastName);
            preparedStatement.setDate(3, java.sql.Date.valueOf(birthDate));
            preparedStatement.setString(4, picture);
            preparedStatement.setInt(5, cin);
            preparedStatement.executeUpdate();

        } catch ( Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }
    }

    public static Reader getReaderByUsername(String username) throws SQLException {
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT * FROM reader WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int readerId = resultSet.getInt("readerId");
                    String storedUsername = resultSet.getString("username");
                    String fullName = resultSet.getString("fullName");
                    Date birthDateSQL = resultSet.getDate("birthDate");
                    LocalDate birthDate = birthDateSQL.toLocalDate();
                    String picturePath = resultSet.getString("picture");
                    int cin = resultSet.getInt("CIN");
                    String password = resultSet.getString("password");

                    Reader reader = new Reader(readerId, storedUsername, fullName, birthDate, picturePath, cin);
                    reader.setPassword(password);

                    return reader;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }
        return null;
    }

    public static boolean checkPassword(String loginPwd, String registeredPwd) {
        return loginPwd.equals(registeredPwd);
    }

    public static List<Reader> getAllReaders() throws SQLException {
        List<Reader> readers = new ArrayList<>();
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT readerId, username, fullName, picture, cin, birthdate FROM reader WHERE username != 'admin'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int readerId = resultSet.getInt("readerId");
                String username = resultSet.getString("username");
                String fullName = resultSet.getString("fullName");
                LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();
                String picture = resultSet.getString("picture");
                int cin = resultSet.getInt("cin");

                Reader reader = new Reader(readerId, username, fullName, birthdate, picture, cin);
                readers.add(reader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }

        return readers;
    }

    public static void deleteSelectedReader(int readerId) {
        String query = "DELETE FROM reader WHERE readerId = ?";

        try (Connection connection = DatabaseConnection.StartConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, readerId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.ERROR, "Reader deleted!", "Reader deleted successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to delete", "Failed to delete the reader. Reader ID not found or an error occurred.");
            }

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

    public static int getIdByUsername(String username) throws SQLException {
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT readerId FROM reader WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("readerId");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }
        return 0;
    }

    public static Reader getReaderById(int readerId) throws SQLException {
        Reader reader;
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT * FROM reader WHERE readerId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, readerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String fullName = resultSet.getString("fullName");
                    String username = resultSet.getString("username");
                    LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();
                    String picture = resultSet.getString("picture");
                    int cin = resultSet.getInt("cin");

                    reader = new Reader(readerId, username, fullName, birthdate, picture, cin);
                    return reader;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }
        return null;
    }

    public static Reader getReaderByFullName(String fullName) throws SQLException {
        Reader reader;
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT * FROM reader WHERE fullName = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, fullName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int readerId = resultSet.getInt("readerId");
                    String username = resultSet.getString("username");
                    LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();
                    String picture = resultSet.getString("picture");
                    int cin = resultSet.getInt("cin");

                    reader = new Reader(readerId, username, fullName, birthdate, picture, cin);
                    return reader;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }
        return null;
    }

    public static List<Reader> searchReaders(String searchText) throws SQLException {
        List<Reader> readers = new ArrayList<>();
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT * FROM reader WHERE (username LIKE ? OR fullName LIKE ? OR  cin LIKE ?) AND username != 'admin'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + searchText + "%");
            preparedStatement.setString(2, "%" + searchText + "%");
            preparedStatement.setString(3, "%" + searchText + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int readerId = resultSet.getInt("readerId");
                String username = resultSet.getString("username");
                LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();
                String picture = resultSet.getString("picture");
                int cin = resultSet.getInt("cin");
                String fullName = resultSet.getString("fullName");

                Reader reader = new Reader(readerId, username, fullName, birthdate, picture, cin);
                readers.add(reader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }

        return readers;
    }
}
