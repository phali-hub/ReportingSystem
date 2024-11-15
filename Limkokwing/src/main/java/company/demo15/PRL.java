package company.demo15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.*;

public class PRL {

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
    private ComboBox<String> lecturerComboBox;

    @FXML
    private Button submitButton;

    @FXML
    private TextField weekNumberField;

    @FXML
    private Label submissionDateLabel;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/limkokwing_academic_reporting";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "@Kurata2002"; // Update with your actual password

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        try (Connection conn = getConnection()) {
            loadLecturers(conn); // Load lecturer ids, not names
            loadClasses(conn);
            loadModules(conn);
        } catch (SQLException e) {
            showAlert("Error loading data: " + e.getMessage(), "Data Load Error", Alert.AlertType.ERROR);
        }
    }

    private void loadLecturers(Connection conn) throws SQLException {
        String lecturerQuery = "SELECT lecturer_id FROM lecturers";  // Only fetch lecturer_id
        PreparedStatement stmt = conn.prepareStatement(lecturerQuery);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lecturerComboBox.getItems().add(rs.getString("lecturer_id"));  // Add lecturer_id to ComboBox
        }
    }

    private void loadClasses(Connection conn) throws SQLException {
        String classQuery = "SELECT class_id, class_name FROM classes";
        PreparedStatement stmt = conn.prepareStatement(classQuery);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            classComboBox.getItems().add(rs.getString("class_name"));
        }
    }

    private void loadModules(Connection conn) throws SQLException {
        String moduleQuery = "SELECT module_id, module_name FROM modules";
        PreparedStatement stmt = conn.prepareStatement(moduleQuery);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            moduleComboBox.getItems().add(rs.getString("module_name"));
        }
    }

    @FXML
    void handleSubmit(ActionEvent event) {
        String selectedLecturer = lecturerComboBox.getValue();
        String selectedClass = classComboBox.getValue();
        String selectedModule = moduleComboBox.getValue();
        String weekNumberText = weekNumberField.getText();
        String challenges = challengesArea.getText();
        String recommendations = recommendationsArea.getText();

        if (selectedLecturer == null || selectedClass == null || selectedModule == null || weekNumberText.isEmpty() || challenges.isEmpty() || recommendations.isEmpty()) {
            showAlert("Please fill in all fields.", "Input Error", Alert.AlertType.ERROR);
            return;
        }

        try (Connection conn = getConnection()) {
            int lecturerId = Integer.parseInt(selectedLecturer);  // Directly use the lecturer_id from ComboBox
            int classId = getId(conn, "classes", "class_id", "class_name", selectedClass);
            int moduleId = getId(conn, "modules", "module_id", "module_name", selectedModule);

            int weekNumber = Integer.parseInt(weekNumberText);

            String sql = "INSERT INTO reports (lecturer_id, class_id, module_id, week_number, challenges, recommendations) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, lecturerId);  // Use lecturer_id
                stmt.setInt(2, classId);
                stmt.setInt(3, moduleId);
                stmt.setInt(4, weekNumber);
                stmt.setString(5, challenges);
                stmt.setString(6, recommendations);
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    showAlert("Report submitted successfully!", "Submit Success", Alert.AlertType.INFORMATION);
                }
            }
        } catch (SQLException e) {
            showAlert("Error submitting report: " + e.getMessage(), "Submit Error", Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            showAlert("Week number must be a valid integer.", "Input Error", Alert.AlertType.ERROR);
        }
    }

    private int getId(Connection conn, String tableName, String idColumn, String nameColumn, String selectedName) throws SQLException {
        String query = "SELECT " + idColumn + " FROM " + tableName + " WHERE " + nameColumn + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, selectedName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(idColumn);
            }
        }
        throw new SQLException("Unable to retrieve ID for " + selectedName);
    }

    @FXML
    public void handleClear(ActionEvent actionEvent) {
        // Clear ComboBoxes
        lecturerComboBox.setValue(null);
        classComboBox.setValue(null);
        moduleComboBox.setValue(null);

        // Clear TextField for week number
        weekNumberField.clear();

        // Clear TextAreas for challenges and recommendations
        challengesArea.clear();
        recommendationsArea.clear();

        // Clear any submission date label (if it's meant to display a date upon submission)
        submissionDateLabel.setText("Submission Date (auto)");

        // Optional: Show a confirmation alert that the form has been cleared
        showAlert("Form cleared successfully.", "Clear Form", Alert.AlertType.INFORMATION);
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
            showAlert("Error loading login screen: " + e.getMessage(), "Logout Error", Alert.AlertType.ERROR);
        }
    }
}
