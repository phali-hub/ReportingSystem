package company.demo15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Addsemester {

    @FXML
    private Label academicyearid;

    @FXML
    private TextField academicyeartxtid;

    @FXML
    private Button deleteid;

    @FXML
    private ImageView imageid;

    @FXML
    private Button saveid;

    @FXML
    private Label semesterid;

    @FXML
    private Label semesternameID;

    @FXML
    private TextField semesternametxtID;

    @FXML
    private TextField semestertxtid;

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

    // Helper method to show an alert
    private void showAlert(String message, String title, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void SaveSemester(ActionEvent event) {
        String semesterId = semestertxtid.getText();
        String academicYear = academicyeartxtid.getText();
        String semesterName = semesternametxtID.getText();

        String sql = "INSERT INTO semesters (semester_id, academic_year_id, semester_name) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, semesterId);
            stmt.setString(2, academicYear);
            stmt.setString(3, semesterName);

            stmt.executeUpdate();
            showAlert("Semester saved successfully!", "Save Success", AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error saving semester: " + e.getMessage(), "Save Error", AlertType.ERROR);
        }
    }

    @FXML
    void UpdateSemester(ActionEvent event) {
        String semesterId = semestertxtid.getText();
        String academicYear = academicyeartxtid.getText();
        String semesterName = semesternametxtID.getText();

        String sql = "UPDATE semesters SET academic_year_id = ?, semester_name = ? WHERE semester_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, academicYear);
            stmt.setString(2, semesterName);
            stmt.setString(3, semesterId);

            stmt.executeUpdate();
            showAlert("Semester updated successfully!", "Update Success", AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error updating semester: " + e.getMessage(), "Update Error", AlertType.ERROR);
        }
    }

    @FXML
    void DeleteSemester(ActionEvent event) {
        String semesterId = semestertxtid.getText();

        String sql = "DELETE FROM semesters WHERE semester_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, semesterId);

            stmt.executeUpdate();
            showAlert("Semester deleted successfully!", "Delete Success", AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error deleting semester: " + e.getMessage(), "Delete Error", AlertType.ERROR);
        }
    }

    @FXML
    void ViewSemester(ActionEvent event) {
        String semesterId = semestertxtid.getText();

        String sql = "SELECT * FROM semesters WHERE semester_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, semesterId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String academicYear = rs.getString("academic_year_id");
                String semesterName = rs.getString("semester_name");

                // Populate the fields with the result
                academicyeartxtid.setText(academicYear);
                semesternametxtID.setText(semesterName);

                showAlert("Semester found: " + semesterName, "View Success", AlertType.INFORMATION);
            } else {
                showAlert("No semester found with id: " + semesterId, "View Error", AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error retrieving semester: " + e.getMessage(), "View Error", AlertType.ERROR);
        }
    }

    @FXML
    public void AddsemesterID(ActionEvent actionEvent) {
        // You can perform any necessary logic or validation on the semester ID
        String semesterId = semestertxtid.getText();
        if (semesterId != null && !semesterId.isEmpty()) {
            showAlert("Semester ID: " + semesterId + " added!", "Semester ID Added", AlertType.INFORMATION);
        } else {
            showAlert("Please enter a valid Semester ID.", "Invalid Input", AlertType.ERROR);
        }
    }

    @FXML
    public void Addacademicyearid(ActionEvent actionEvent) {
        // Perform any necessary logic or validation for academic year ID
        String academicYear = academicyeartxtid.getText();
        if (academicYear != null && !academicYear.isEmpty()) {
            showAlert("Academic Year ID: " + academicYear + " added!", "Academic Year ID Added", AlertType.INFORMATION);
        } else {
            showAlert("Please enter a valid Academic Year ID.", "Invalid Input", AlertType.ERROR);
        }
    }

    @FXML
    public void AddSemestername(ActionEvent actionEvent) {
        // Handle the semester name input
        String semesterName = semesternametxtID.getText();
        if (semesterName != null && !semesterName.isEmpty()) {
            showAlert("Semester Name: " + semesterName + " added!", "Semester Name Added", AlertType.INFORMATION);
        } else {
            showAlert("Please enter a valid Semester Name.", "Invalid Input", AlertType.ERROR);
        }
    }
}
