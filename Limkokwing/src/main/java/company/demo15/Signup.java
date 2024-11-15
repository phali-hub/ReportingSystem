package company.demo15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Signup {
    private final Functions fn = new Functions();
    @FXML
    private Button loginid;
    @FXML
    private Button SignUpButton;
    @FXML
    private Label PasswordID;
    @FXML
    private Label UserNameID;
    @FXML
    private Label RoleID;
    @FXML
    private Label EmployeeNumberID;
    @FXML
    private PasswordField PasswordtxtID;
    @FXML
    private TextField FullNameTxtID;
    @FXML
    private TextField RoleTxtID;
    @FXML
    private TextField EmployeeNumberTxtID;

    @FXML
    void signUp(ActionEvent event) {
        // Retrieve input values
        String fullName = FullNameTxtID.getText();
        String password = PasswordtxtID.getText();
        String role = RoleTxtID.getText();
        String employeeNumber = EmployeeNumberTxtID.getText();

        // Validate input and sign up
        if (isInputValid(fullName, password, role, employeeNumber)) {
            try {
                fn.signUp(fullName, password, role, employeeNumber);  // Insert user into database
               // HelloApplication.showAlert(Alert.AlertType.INFORMATION, "SUCCESS", "Sign up successful!");
               // loadLoginScreen();  // Navigate to login screen
            } catch (SQLException | IOException e) {
                HelloApplication.showAlert(Alert.AlertType.ERROR, "SIGN UP ERROR", "Error during sign up: " + e.getMessage());
            }
        }
    }

    // Helper method to validate input
    private boolean isInputValid(String fullName, String password, String role, String employeeNumber) {
        if (fullName.isEmpty() || password.isEmpty() || role.isEmpty() || employeeNumber.isEmpty()) {
            HelloApplication.showAlert(Alert.AlertType.WARNING, "SIGN UP ERROR", "Please fill in all fields.");
            return false;
        }
        return true;
    }
    @FXML
    void login(ActionEvent event)
    {
        try {
            Stage stage = new Stage();
            HelloApplication.page(stage, "Login.fxml", "Login", 600, 400);
        } catch (Exception e) {
            HelloApplication.showAlert(Alert.AlertType.ERROR, "ERROR", "Error loading the login screen: " + e.getMessage());
        }
    }

    // Method to load the login screen
    private void loadLoginScreen() {
        try {
            Stage stage = new Stage();
            HelloApplication.page(stage, "Login.fxml", "Login", 600, 400);
        } catch (Exception e) {
            HelloApplication.showAlert(Alert.AlertType.ERROR, "ERROR", "Error loading the login screen: " + e.getMessage());
        }
    }
}
