package company.demo15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Login {

    @FXML
    private Button cancelID;

    @FXML
    private Button submitID;

    @FXML
    private TextField usernametxtID;

    @FXML
    private PasswordField passwordtxtID;

    @FXML
    private ComboBox<String> roleComboBox;  // ComboBox for role selection

    private String username;
    private String password;
    private String role;

    // Method to connect to the database
    private Connection connect() {
        String url = "jdbc:mysql://localhost:3306/limkokwing_academic_reporting";
        String user = "root";
        String pass = "@Kurata2002";

        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Handle cancel button click to clear fields
    @FXML
    void CancelProcess(ActionEvent event) {
        usernametxtID.clear();
        passwordtxtID.clear();
        roleComboBox.getSelectionModel().clearSelection();
        System.out.println("Inputs cleared.");
    }

    // Handle login button click
    @FXML
    void SubmitProcess(ActionEvent event) {
        username = usernametxtID.getText();
        password = passwordtxtID.getText();
        role = roleComboBox.getValue();

        // Validate that all fields are filled
        if (username.isEmpty() || password.isEmpty() || role == null) {
            HelloApplication.showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter all details.");
            return;
        }

        // Authenticate user
        if (authenticateUser(username, password, role)) {
            // Log the login event
            logLoginEvent(username);

            // Load appropriate scene based on the role
            loadRoleScene(role);
        } else {
            HelloApplication.showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid username or password.");
        }
    }

    // Authenticate user based on the entered username, password, and role
    private boolean authenticateUser(String username, String password, String role) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // If a matching record is found, login is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Load the appropriate scene based on the role
    private void loadRoleScene(String role) {
        try {
            Stage stage = new Stage();
            String fxmlFile = "";

            // Determine the scene to load based on the role
            switch (role) {
                case "PRL":
                    fxmlFile = "PRL.fxml";
                    break;
                case "Lecturer":
                    fxmlFile = "lectureROLE.fxml";
                    break;
                case "Faculty Admin":
                    fxmlFile = "Profile.fxml";
                    break;
                default:
                    HelloApplication.showAlert(Alert.AlertType.ERROR, "Role Error", "Invalid role selected.");
                    return;
            }

            // Load the appropriate scene
            HelloApplication.page(stage, fxmlFile, role + " Dashboard", 600, 400);
            clearFields();

        } catch (IOException e) {
            e.printStackTrace();
            HelloApplication.showAlert(Alert.AlertType.ERROR, "Scene Error", "Error loading the role's dashboard.");
        }
    }

    // Clear all text fields
    private void clearFields() {
        usernametxtID.clear();
        passwordtxtID.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }

    // Capture username input (this method will be triggered by an event in FXML)
    public void TakeUsername(ActionEvent actionEvent) {
        username = usernametxtID.getText();  // Capture the username text from the TextField
        System.out.println("Username: " + username);  // Debugging line to check the value
    }

    // Capture password input (this method will be triggered by an event in FXML)
    public void TakePassword(ActionEvent actionEvent) {
        password = passwordtxtID.getText();  // Capture the password text from the PasswordField
        System.out.println("Password: " + password);  // Debugging line to check the value
    }

    // Method to log the login event to a file
    private void logLoginEvent(String username) {
        File logFile = new File("log.txt");
        try {
            // Create the file if it does not exist
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            // Append log entry to the file
            try (FileWriter fw = new FileWriter(logFile, true);
                 PrintWriter pw = new PrintWriter(fw)) {

                // Get the current time
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                // Log the event (username and timestamp)
                pw.println("User: " + username + " logged in at: " + timestamp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
