package company.demo15;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;

public class Lecturer {
    public Lecturer() {}

    @FXML
    private Button deleteID; // Use JavaFX Button

    @FXML
    private TextField departmentxtid;

    @FXML
    private TextField lecturertxtid;

    @FXML
    private TextField usertxtid;

    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/limkokwing_academic_reporting";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "@Kurata2002";

    public Lecturer(String userId, String lecturerId, String department) {}

    // Helper method for database connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    // Show alert method
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Delete Lecturer
    @FXML
    void DeleteLecturer(ActionEvent event) {
        String lecturerId = lecturertxtid.getText();

        if (lecturerId.isEmpty()) {
            showAlert("Input Error", "Lecturer ID is required!", AlertType.ERROR);
            return;
        }

        String deleteSQL = "DELETE FROM lecturers WHERE lecturer_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setString(1, lecturerId);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                showAlert("Success", "Lecturer deleted successfully!", AlertType.INFORMATION);
                clearFields();
            } else {
                showAlert("Not Found", "No lecturer found with the given ID.", AlertType.WARNING);
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while deleting the lecturer.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Update Lecturer
    @FXML
    void UpdateLecturer(ActionEvent event) {
        String userId = usertxtid.getText();
        String lecturerId = lecturertxtid.getText();
        String department = departmentxtid.getText();

        if (userId.isEmpty() || lecturerId.isEmpty() || department.isEmpty()) {
            showAlert("Input Error", "Please fill in all fields!", AlertType.ERROR);
            return;
        }

        String updateSQL = "UPDATE lecturers SET user_id = ?, department = ? WHERE lecturer_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, department);
            pstmt.setString(3, lecturerId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Success", "Lecturer updated successfully!", AlertType.INFORMATION);
            } else {
                showAlert("Not Found", "No lecturer found with the given ID.", AlertType.WARNING);
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while updating the lecturer.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // View Lecturer
    @FXML
    void ViewLecturer(ActionEvent event) {
        String lecturerId = lecturertxtid.getText();

        if (lecturerId.isEmpty()) {
            showAlert("Input Error", "Lecturer ID is required!", AlertType.ERROR);
            return;
        }

        String selectSQL = "SELECT user_id, department FROM lecturers WHERE lecturer_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setString(1, lecturerId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                usertxtid.setText(rs.getString("user_id"));
                departmentxtid.setText(rs.getString("department"));
                showAlert("Success", "Lecturer details loaded!", AlertType.INFORMATION);
            } else {
                showAlert("Not Found", "No lecturer found with the given ID.", AlertType.WARNING);
                clearFields();
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while loading lecturer details.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Save Lecturer
    @FXML
    void SaveLecturer(ActionEvent event) {
        String userId = usertxtid.getText();
        String lecturerId = lecturertxtid.getText();
        String department = departmentxtid.getText();

        if (userId.isEmpty() || lecturerId.isEmpty() || department.isEmpty()) {
            showAlert("Input Error", "Please fill in all fields!", AlertType.ERROR);
            return;
        }

        // SQL query to insert a new lecturer
        String insertSQL = "INSERT INTO lecturers (lecturer_id, user_id, department) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            // Set the values from the text fields
            pstmt.setString(1, lecturerId);
            pstmt.setString(2, userId);
            pstmt.setString(3, department);

            // Execute the insert operation
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Success", "New lecturer saved successfully!", AlertType.INFORMATION);
                clearFields();  // Clear fields after saving
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while saving the lecturer.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Utility method to clear text fields
    private void clearFields() {
        usertxtid.clear();
        lecturertxtid.clear();
        departmentxtid.clear();
    }
}
