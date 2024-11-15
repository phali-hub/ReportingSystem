package company.demo15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import java.sql.*;

public class Addacademic {

    @FXML
    private TextField academicname;

    @FXML
    private TextField academictxtid;

    @FXML
    private Label academicyearname;

    @FXML
    private Label ademicid;

    @FXML
    private Button deleteid;

    @FXML
    private Button saveid;

    @FXML
    private Button updateid;

    @FXML
    private Button viewid;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/limkokwing_academic_reporting"; // Replace with your database name
    private static final String DB_USER = "root"; // Your MySQL username
    private static final String DB_PASSWORD = "@Kurata2002"; // Replace with your actual password

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
    void Save(ActionEvent event) {
        String academicYearId = academictxtid.getText();
        String academicYearName = academicname.getText();

        String sql = "INSERT INTO academic_years (academic_year_id, year_name) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, academicYearId);
            stmt.setString(2, academicYearName);

            stmt.executeUpdate();
            showAlert("Academic year saved successfully!", "Save Success", AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error saving academic year: " + e.getMessage(), "Save Error", AlertType.ERROR);
        }
    }

    @FXML
    void View(ActionEvent event) {
        String academicYearId = academictxtid.getText();

        String sql = "SELECT * FROM academic_years WHERE academic_year_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, academicYearId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String academicYearName = rs.getString("year_name");
                academicname.setText(academicYearName);
                showAlert("Academic year found: " + academicYearName, "View Success", AlertType.INFORMATION);
            } else {
                showAlert("No academic year found with id: " + academicYearId, "View Error", AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error retrieving academic year: " + e.getMessage(), "View Error", AlertType.ERROR);
        }
    }

    @FXML
    void Update(ActionEvent event) {
        String academicYearId = academictxtid.getText();
        String academicYearName = academicname.getText();

        String sql = "UPDATE academic_years SET year_name = ? WHERE academic_year_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, academicYearName);
            stmt.setString(2, academicYearId);

            stmt.executeUpdate();
            showAlert("Academic year updated successfully!", "Update Success", AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error updating academic year: " + e.getMessage(), "Update Error", AlertType.ERROR);
        }
    }

    @FXML
    void Delete(ActionEvent event) {
        String academicYearId = academictxtid.getText();

        String sql = "DELETE FROM academic_years WHERE academic_year_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, academicYearId);

            stmt.executeUpdate();
            showAlert("Academic year deleted successfully!", "Delete Success", AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error deleting academic year: " + e.getMessage(), "Delete Error", AlertType.ERROR);
        }
    }

    // Get the academic year name based on the user input academic year ID
    @FXML
    void GetAcademicname(ActionEvent event) {
        String academicYearId = academictxtid.getText();

        String sql = "SELECT year_name FROM academic_years WHERE academic_year_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, academicYearId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String academicYearName = rs.getString("year_name");
                academicname.setText(academicYearName);
                showAlert("Academic year name: " + academicYearName, "Get Academic Name", AlertType.INFORMATION);
            } else {
                showAlert("No academic year found with id: " + academicYearId, "Get Academic Name Error", AlertType.ERROR);
                academicname.clear();
            }
        } catch (SQLException e) {
            showAlert("Error retrieving academic year name: " + e.getMessage(), "Get Academic Name Error", AlertType.ERROR);
        }
    }

    // Get the academic year ID based on the user input academic year name
    @FXML
    void Getacademicyear(ActionEvent event) {
        String academicYearName = academicname.getText();

        String sql = "SELECT academic_year_id FROM academic_years WHERE year_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, academicYearName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String academicYearId = rs.getString("academic_year_id");
                academictxtid.setText(academicYearId);
                showAlert("Academic year ID: " + academicYearId, "Get Academic ID", AlertType.INFORMATION);
            } else {
                showAlert("No academic year found with name: " + academicYearName, "Get Academic ID Error", AlertType.ERROR);
                academictxtid.clear();
            }
        } catch (SQLException e) {
            showAlert("Error retrieving academic year ID: " + e.getMessage(), "Get Academic ID Error", AlertType.ERROR);
        }
    }
}
