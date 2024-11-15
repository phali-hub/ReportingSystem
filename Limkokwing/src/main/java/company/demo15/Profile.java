package company.demo15;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Profile {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/limkokwing_academic_reporting";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "@Kurata2002";

    // Create an instance of the Functions class
    private final Functions fn = new Functions();

    @FXML
    private Label nameLabel;  // Label to display username

    @FXML
    private Button continueButton;  // Continue Button

    private String facultyAdminUsername; // Username of the logged-in Faculty Admin

    // Method to set the Faculty Admin's username, called by the Login class
    public void setFacultyAdminUsername(String username) {
        this.facultyAdminUsername = username;
        loadProfileData();
    }

    // Method to load profile data from the database
    private void loadProfileData() {
        DBConnection db = DBConnection.getInstance(); // Get the singleton DBConnection instance

        // SQL query to fetch the username based on the logged-in username and role
        String sql = "SELECT username FROM users WHERE username = ? AND role = 'Faculty Admin'";

        try {
            db.setStm(sql);  // Prepare the SQL statement
            db.getStm().setString(1, facultyAdminUsername);  // Set the username parameter
            db.setRes();  // Execute the query

            ResultSet rs = db.getRes();
            if (rs.next()) {
                String username = rs.getString("username");
                System.out.println("Fetched username: " + username);  // Debugging line
                nameLabel.setText(username);  // Update the nameLabel with the retrieved username

                // Success alert
                showAlert(Alert.AlertType.INFORMATION, "Profile Loaded", "Username successfully loaded: " + username);

            } else {
                nameLabel.setText("No data found");
                System.out.println("No data found for username: " + facultyAdminUsername);  // Debugging line

                // Error alert when no data is found
                showAlert(Alert.AlertType.WARNING, "No Data Found", "No profile data found for the username: " + facultyAdminUsername);
            }

        } catch (SQLException e) {
            // Show error alert in case of an exception
            HelloApplication.showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading Faculty Admin profile: " + e.getMessage());
        } finally {
            db.closeConnection();  // Close the connection and resources
        }
    }

    // Method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Handle Continue Button click
    @FXML
    private void handleContinueButtonClick() {
        try {
            // Load the adminrole.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminRoles.fxml"));
            AnchorPane adminRolePane = loader.load();

            // Set up the new stage (window)
            Stage stage = new Stage();
            Scene scene = new Scene(adminRolePane);
            stage.setScene(scene);
            stage.setTitle("Faculty Admin Dashboard");
            stage.show();

            // Optionally close the current profile window
            ((Stage) continueButton.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the Faculty Admin Dashboard.");
        }
    }
}
