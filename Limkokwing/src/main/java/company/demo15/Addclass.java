package company.demo15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Addclass {

    @FXML
    private Label classid;

    @FXML
    private Label classnameid;

    @FXML
    private TextField classnametxtid;

    @FXML
    private TextField classtxtid;

    @FXML
    private Button deleteid;

    @FXML
    private Button saveid;

    @FXML
    private Button updateid;

    @FXML
    private Button viewid;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/limkokwing_academic_reporting";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "@Kurata2002"; // Update with your actual password

    // Method to establish a connection to the database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Helper method to show alerts
    private void showAlert(String message, String title, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void Save(ActionEvent event) {
        String classId = classtxtid.getText();
        String className = classnametxtid.getText();

        if (classId.isEmpty() || className.isEmpty()) {
            showAlert("Please enter both Class ID and Class Name.", "Input Error", AlertType.ERROR);
            return;
        }

        String sql = "INSERT INTO classes (class_id, class_name) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, classId);
            stmt.setString(2, className);

            stmt.executeUpdate();
            showAlert("Class saved successfully!", "Save Success", AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error saving class: " + e.getMessage(), "Save Error", AlertType.ERROR);
        }
    }

    @FXML
    void Update(ActionEvent event) {
        String classId = classtxtid.getText();
        String className = classnametxtid.getText();

        if (classId.isEmpty() || className.isEmpty()) {
            showAlert("Please enter both Class ID and Class Name.", "Input Error", AlertType.ERROR);
            return;
        }

        String sql = "UPDATE classes SET class_name = ? WHERE class_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, className);
            stmt.setString(2, classId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Class updated successfully!", "Update Success", AlertType.INFORMATION);
            } else {
                showAlert("No class found with the given Class ID.", "Update Error", AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error updating class: " + e.getMessage(), "Update Error", AlertType.ERROR);
        }
    }

    @FXML
    void Delete(ActionEvent event) {
        String classId = classtxtid.getText();

        if (classId.isEmpty()) {
            showAlert("Please enter a Class ID to delete.", "Input Error", AlertType.ERROR);
            return;
        }

        String sql = "DELETE FROM classes WHERE class_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, classId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Class deleted successfully!", "Delete Success", AlertType.INFORMATION);
            } else {
                showAlert("No class found with the given Class ID.", "Delete Error", AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error deleting class: " + e.getMessage(), "Delete Error", AlertType.ERROR);
        }
    }

    @FXML
    void View(ActionEvent event) {
        String classId = classtxtid.getText();

        if (classId.isEmpty()) {
            showAlert("Please enter a Class ID to view.", "Input Error", AlertType.ERROR);
            return;
        }

        String sql = "SELECT * FROM classes WHERE class_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, classId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String className = rs.getString("class_name");

                // Populate the class name field with the result
                classnametxtid.setText(className);
                showAlert("Class found: " + className, "View Success", AlertType.INFORMATION);
            } else {
                showAlert("No class found with the given Class ID.", "View Error", AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error retrieving class: " + e.getMessage(), "View Error", AlertType.ERROR);
        }
    }

    // Method triggered when class ID is entered
    @FXML
    public void ClassID(ActionEvent actionEvent) {
        String classId = classtxtid.getText();

        // Check if the class ID is empty or invalid
        if (classId.isEmpty()) {
            showAlert("Class ID cannot be empty.", "Input Error", AlertType.ERROR);
        } else {
            // Optionally, validate class ID format or trigger other actions
            System.out.println("Class ID entered: " + classId); // For debugging
        }
    }

    // Method to fetch and populate class name based on entered Class ID
    @FXML
    public void Getclassname(ActionEvent actionEvent) {
        String classId = classtxtid.getText();

        if (classId.isEmpty()) {
            showAlert("Please enter a Class ID to fetch the class name.", "Input Error", AlertType.ERROR);
            return;
        }

        String sql = "SELECT class_name FROM classes WHERE class_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, classId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Populate the class name field if found
                String className = rs.getString("class_name");
                classnametxtid.setText(className);
                showAlert("Class name found: " + className, "View Success", AlertType.INFORMATION);
            } else {
                showAlert("No class found with the provided Class ID.", "View Error", AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error fetching class name: " + e.getMessage(), "View Error", AlertType.ERROR);
        }
    }
}
