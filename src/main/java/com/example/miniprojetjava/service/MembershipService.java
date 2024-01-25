package com.example.miniprojetjava.service;

import com.example.miniprojetjava.controller.ReaderController;
import com.example.miniprojetjava.model.Membership;
import com.example.miniprojetjava.model.Reader;
import com.example.miniprojetjava.util.DatabaseConnection;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MembershipService {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void startAutomaticStatusUpdate() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                List<Membership> memberships = getAllMemberships();
                for (Membership membership : memberships) {
                    membership.updateStatus();
                    updateMembershipStatus(membership);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.DAYS);
    }


    public static List<Membership> getAllMemberships() throws SQLException {
        List<Membership> memberships = new ArrayList<>();
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT * FROM membership";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int readerId = resultSet.getInt("readerId");
                String status = resultSet.getString("status");
                Date creationDate = resultSet.getDate("creationDate");
                Date endDate = resultSet.getDate("endDate");
                int membershipId = resultSet.getInt("membershipId");

                Reader reader = ReaderService.getReaderById(readerId);
                Membership membership = new Membership(membershipId, reader, creationDate, endDate, status);
                memberships.add(membership);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memberships;
    }


    public static List<Membership> searchMemberships(String searchText) throws SQLException {
        List<Membership> memberships = new ArrayList<>();
        Connection connection = DatabaseConnection.StartConnection();
        String query = "SELECT m.*, r.fullName FROM membership m JOIN reader r ON m.readerId = r.readerId WHERE m.status LIKE ? OR m.creationDate LIKE ? OR r.fullName LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + searchText + "%");
            preparedStatement.setString(2, "%" + searchText + "%");
            preparedStatement.setString(3, "%" + searchText + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int readerId = resultSet.getInt("readerId");
                String status = resultSet.getString("status");
                Date creationDate = resultSet.getDate("creationDate");
                Date endDate = resultSet.getDate("endDate");

                Reader reader = new Reader();
                reader.setReaderId(readerId);
                reader.setFullName(resultSet.getString("fullName"));

                Membership membership = new Membership(reader, creationDate, endDate, status);
                memberships.add(membership);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }

        return memberships;
    }

    public static void insertMembership(String readerName, String periodStr) throws SQLException {
        Connection connection = DatabaseConnection.StartConnection();
        String query = "INSERT INTO membership(readerId, creationDate, endDate, status) VALUES (?, ?, ?, ?)";

        int readerId = ReaderService.getIdByUsername(readerName);
        int period = Integer.parseInt(periodStr);
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusDays(period);
        String status = "Available";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, readerId);
            preparedStatement.setDate(2, Date.valueOf(currentDate));
            preparedStatement.setDate(3, Date.valueOf(endDate));
            preparedStatement.setString(4, status);
            preparedStatement.executeUpdate();
        } catch ( Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.CloseConnection();
        }
    }

    public static void deleteSelectedMembership(int readerId) {
        String query = "DELETE FROM membership WHERE readerId = ?";

        try (Connection connection = DatabaseConnection.StartConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, readerId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.WARNING, "Membership not found", "Please verify membership!");
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selected Membership", "Please select a membership to delete!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateMembershipStatus(Membership membership) throws SQLException {
        Connection connection = DatabaseConnection.StartConnection();
        String query = "UPDATE membership SET status = ? WHERE membershipId = ?";

        membership.updateStatus();
        String status = membership.getStatus();
        int membershipId = membership.getMembershipId();


        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, membershipId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Membership Status Updated", "Membership status has been updated!");

            } else {
                showAlert(Alert.AlertType.WARNING, "No Selected Membership", "Please select a membership to delete!");
            }
        } finally {
            DatabaseConnection.CloseConnection();
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
