package company.demo15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LecturerROLE {

    @FXML
    private TextArea challengesArea;

    @FXML
    private ComboBox<String> classComboBox;

    @FXML
    private ImageView imgid;

    @FXML
    private ComboBox<String> moduleComboBox;

    @FXML
    private TextArea recommendationsArea;

    @FXML
    private TextField chapterField; // Change from TextArea to TextField


    @FXML
    private TextField learningOutcomesField; // Change from TextArea to TextField


    @FXML
    private Button submitButton;

    @FXML
    private Button attendanceButton;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/limkokwing_academic_reporting";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "@Kurata2002"; // Update with your actual password

    // Mappings of class names and module names to their respective IDs
    private Map<String, Integer> classMap = new HashMap<>();
    private Map<String, Integer> moduleMap = new HashMap<>();

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

    // Fetch classes from the database and populate ComboBox and classMap
    private void loadClasses() {
        String sql = "SELECT class_id, class_name FROM classes";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int classId = rs.getInt("class_id");
                String className = rs.getString("class_name");
                classComboBox.getItems().add(className);
                classMap.put(className, classId);
            }
        } catch (SQLException e) {
            showAlert("Error fetching classes: " + e.getMessage(), "Database Error", AlertType.ERROR);
        }
    }

    // Fetch modules from the database and populate ComboBox and moduleMap
    private void loadModules() {
        String sql = "SELECT module_id, module_name FROM modules";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int moduleId = rs.getInt("module_id");
                String moduleName = rs.getString("module_name");
                moduleComboBox.getItems().add(moduleName);
                moduleMap.put(moduleName, moduleId);
            }
        } catch (SQLException e) {
            showAlert("Error fetching modules: " + e.getMessage(), "Database Error", AlertType.ERROR);
        }
    }

    // Insert report into the database
    private void insertReport(String selectedClass, String selectedModule, String challenges, String recommendations, String chapter, String learningOutcomes) {
        String sql = "INSERT INTO lecturer_reports (class_id, module_id, challenges, recommendations, chapter, learning_outcomes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, classMap.get(selectedClass));
            stmt.setInt(2, moduleMap.get(selectedModule));
            stmt.setString(3, challenges);
            stmt.setString(4, recommendations);
            stmt.setString(5, chapter);
            stmt.setString(6, learningOutcomes);

            stmt.executeUpdate();
            showAlert("Report submitted successfully!", "Submit Success", AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error submitting report: " + e.getMessage(), "Submit Error", AlertType.ERROR);
        }
    }

    // Submit report method
    @FXML
    void handleSubmit(ActionEvent event) {
        String selectedClass = classComboBox.getValue();
        String selectedModule = moduleComboBox.getValue();
        String challenges = challengesArea.getText();
        String recommendations = recommendationsArea.getText();
        String chapter = chapterField.getText();
        String learningOutcomes = learningOutcomesField.getText();

        if (selectedClass == null || selectedModule == null || challenges.isEmpty() || recommendations.isEmpty() || chapter.isEmpty() || learningOutcomes.isEmpty()) {
            showAlert("Please fill in all fields.", "Input Error", AlertType.ERROR);
            return;
        }

        // Insert report into the database
        insertReport(selectedClass, selectedModule, challenges, recommendations, chapter, learningOutcomes);
    }

    // Handle attendance marking
    @FXML
    void handleAttendance(ActionEvent event) {
        String selectedClass = classComboBox.getValue();
        if (selectedClass == null) {
            showAlert("Please select a class first.", "Input Error", AlertType.ERROR);
            return;
        }

        // Simulate marking attendance (in a real app, you would have checkboxes for each student)
        String sql = "UPDATE students SET attendance = 'present' WHERE class_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classMap.get(selectedClass));
            int updatedRows = stmt.executeUpdate();

            if (updatedRows > 0) {
                showAlert("Attendance marked successfully.", "Attendance Success", AlertType.INFORMATION);
            } else {
                showAlert("No students found for this class.", "Attendance Error", AlertType.WARNING);
            }
        } catch (SQLException e) {
            showAlert("Error marking attendance: " + e.getMessage(), "Attendance Error", AlertType.ERROR);
        }
    }

    // Initialize ComboBoxes with data from the database
    @FXML
    public void initialize() {
        // Load classes and modules into ComboBoxes and maps
        loadClasses();
        loadModules();
    }

    public void handleLogout(ActionEvent actionEvent) {
        // Close the current window
        Stage currentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();

        // Optional: Redirect to login screen
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("Login.fxml")); // Update with your login FXML file
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(loginRoot));
            loginStage.show();
        } catch (Exception e) {
            showAlert("Error loading login screen: " + e.getMessage(), "Logout Error", AlertType.ERROR);
        }
    }
}
