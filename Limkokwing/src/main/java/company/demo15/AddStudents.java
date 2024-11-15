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

public class AddStudents {

    @FXML
    private Label classid;

    @FXML
    private TextField classtxtID;

    @FXML
    private Button deleteid;

    @FXML
    private Label fullnameid;

    @FXML
    private TextField fullnametxtID;

    @FXML
    private ImageView imgid;

    @FXML
    private Button saveid;

    @FXML
    private TextField studentNOID;

    @FXML
    private Label studentnoID;

    @FXML
    private Label studentsid;

    @FXML
    private TextField studentxtid;

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

    // Save student method
    @FXML
    void SaveStudent(ActionEvent event) {
        String studentId = studentxtid.getText();
        String fullName = fullnametxtID.getText();
        String studentNo = studentNOID.getText();
        String classId = classtxtID.getText();

        if (studentId.isEmpty() || fullName.isEmpty() || studentNo.isEmpty() || classId.isEmpty()) {
            showAlert("Please fill in all fields.", "Input Error", AlertType.ERROR);
            return;
        }

        String sql = "INSERT INTO students (student_id, full_name, student_number, class_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            stmt.setString(2, fullName);
            stmt.setString(3, studentNo);
            stmt.setString(4, classId);

            stmt.executeUpdate();
            showAlert("Student saved successfully!", "Save Success", AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error saving student: " + e.getMessage(), "Save Error", AlertType.ERROR);
        }
    }

    // Update student method
    @FXML
    void Updatestudent(ActionEvent event) {
        String studentId = studentxtid.getText();
        String fullName = fullnametxtID.getText();
        String studentNo = studentNOID.getText();
        String classId = classtxtID.getText();

        if (studentId.isEmpty() || fullName.isEmpty() || studentNo.isEmpty() || classId.isEmpty()) {
            showAlert("Please fill in all fields.", "Input Error", AlertType.ERROR);
            return;
        }

        String sql = "UPDATE students SET full_name = ?, student_number = ?, class_id = ? WHERE student_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullName);
            stmt.setString(2, studentNo);
            stmt.setString(3, classId);
            stmt.setString(4, studentId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Student updated successfully!", "Update Success", AlertType.INFORMATION);
            } else {
                showAlert("No student found with the given Student ID.", "Update Error", AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error updating student: " + e.getMessage(), "Update Error", AlertType.ERROR);
        }
    }

    // Delete student method
    @FXML
    void Deletestudent(ActionEvent event) {
        String studentId = studentxtid.getText();

        if (studentId.isEmpty()) {
            showAlert("Please enter a Student ID to delete.", "Input Error", AlertType.ERROR);
            return;
        }

        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Student deleted successfully!", "Delete Success", AlertType.INFORMATION);
            } else {
                showAlert("No student found with the given Student ID.", "Delete Error", AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error deleting student: " + e.getMessage(), "Delete Error", AlertType.ERROR);
        }
    }

    // View student method
    @FXML
    void Viewstudent(ActionEvent event) {
        String studentId = studentxtid.getText();

        if (studentId.isEmpty()) {
            showAlert("Please enter a Student ID to view.", "Input Error", AlertType.ERROR);
            return;
        }

        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("full_name");
                String studentNo = rs.getString("student_number");
                String classId = rs.getString("class_id");

                // Populate fields with retrieved data
                fullnametxtID.setText(fullName);
                studentNOID.setText(studentNo);
                classtxtID.setText(classId);

                showAlert("Student found: " + fullName, "View Success", AlertType.INFORMATION);
            } else {
                showAlert("No student found with the given Student ID.", "View Error", AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error retrieving student: " + e.getMessage(), "View Error", AlertType.ERROR);
        }
    }

    // Getters for fields (optional)
    @FXML
    void GetFullname(ActionEvent event) {
        String fullName = fullnametxtID.getText();
        System.out.println("Full name entered: " + fullName);
    }

    @FXML
    void GetStudentNO(ActionEvent event) {
        String studentNo = studentNOID.getText();
        System.out.println("Student number entered: " + studentNo);
    }

    @FXML
    void GetclassID(ActionEvent event) {
        String classId = classtxtID.getText();
        System.out.println("Class ID entered: " + classId);
    }

    @FXML
    void GetstudentID(ActionEvent event) {
        String studentId = studentxtid.getText();
        System.out.println("Student ID entered: " + studentId);
    }
}
