package company.demo15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;

public class AddModule {
    public AddModule () {}

    @FXML
    private Button deleteid;

    @FXML
    private ImageView imgid;

    @FXML
    private Label moduleID;

    @FXML
    private Label modulecode;

    @FXML
    private TextField modulecodetxtid;

    @FXML
    private Label modulenameID;

    @FXML
    private TextField modulenametxtID;

    @FXML
    private TextField moduletxtid;

    @FXML
    private Button saveid;

    @FXML
    private Button updateid;

    @FXML
    private Button viewid;

    @FXML
    private Connection conn;

    // Database connection parameters
    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/limkokwing_academic_reporting";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "@Kurata2002";

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

    // Save module to the database
    @FXML
    void SaveSmodule(ActionEvent event) {
        String moduleID = moduletxtid.getText();
        String moduleName = modulenametxtID.getText();
        String moduleCode = modulecodetxtid.getText();

        if (moduleID.isEmpty() || moduleName.isEmpty() || moduleCode.isEmpty()) {
            showAlert("Input Error", "Please fill in all fields!", AlertType.ERROR);
            return;
        }

        String insertSQL = "INSERT INTO modules (module_id, module_name, module_code) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, moduleID);
            pstmt.setString(2, moduleName);
            pstmt.setString(3, moduleCode);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Success", "Module saved successfully!", AlertType.INFORMATION);
                clearFields();
            } else {
                showAlert("Error", "Failed to save the module.", AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while saving the module.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Update module in the database
    @FXML
    void Updatemodule(ActionEvent event) {
        String moduleID = moduletxtid.getText();
        String moduleName = modulenametxtID.getText();
        String moduleCode = modulecodetxtid.getText();

        if (moduleID.isEmpty() || moduleName.isEmpty() || moduleCode.isEmpty()) {
            showAlert("Input Error", "Please fill in all fields!", AlertType.ERROR);
            return;
        }

        String updateSQL = "UPDATE modules SET module_name = ?, module_code = ? WHERE module_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, moduleName);
            pstmt.setString(2, moduleCode);
            pstmt.setString(3, moduleID);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Success", "Module updated successfully!", AlertType.INFORMATION);
            } else {
                showAlert("Error", "No module found with the given ID.", AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while updating the module.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // View module details from the database
    @FXML
    void Viewmodule(ActionEvent event) {
        String moduleID = moduletxtid.getText();

        if (moduleID.isEmpty()) {
            showAlert("Input Error", "Module ID is required!", AlertType.ERROR);
            return;
        }

        String selectSQL = "SELECT module_name, module_code FROM modules WHERE module_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setString(1, moduleID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                modulenametxtID.setText(rs.getString("module_name"));
                modulecodetxtid.setText(rs.getString("module_code"));
                showAlert("Success", "Module details loaded!", AlertType.INFORMATION);
            } else {
                showAlert("Not Found", "No module found with the given ID.", AlertType.WARNING);
                clearFields();
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while loading the module details.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Delete module from the database
    @FXML
    void Deletemodule(ActionEvent event) {
        String moduleID = moduletxtid.getText();

        if (moduleID.isEmpty()) {
            showAlert("Input Error", "Module ID is required!", AlertType.ERROR);
            return;
        }

        String deleteSQL = "DELETE FROM modules WHERE module_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setString(1, moduleID);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                showAlert("Success", "Module deleted successfully!", AlertType.INFORMATION);
                clearFields();
            } else {
                showAlert("Error", "No module found with the given ID.", AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while deleting the module.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Utility method to clear text fields
    private void clearFields() {
        moduletxtid.clear();
        modulenametxtID.clear();
        modulecodetxtid.clear();
    }

    // Get module name based on module ID
    @FXML
    public void Getmodulename(ActionEvent actionEvent) {
        String moduleID = moduletxtid.getText();

        if (moduleID.isEmpty()) {
            showAlert("Input Error", "Please enter a valid Module ID.", AlertType.ERROR);
            return;
        }

        String selectSQL = "SELECT module_name FROM modules WHERE module_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setString(1, moduleID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String moduleName = rs.getString("module_name");
                modulenametxtID.setText(moduleName);
                showAlert("Success", "Module name loaded successfully!", AlertType.INFORMATION);
            } else {
                showAlert("Not Found", "No module found with the given ID.", AlertType.WARNING);
                clearFields();
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while retrieving the module name.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Get module code based on module ID
    @FXML
    public void Getmodulecode(ActionEvent actionEvent) {
        String moduleID = moduletxtid.getText();

        if (moduleID.isEmpty()) {
            showAlert("Input Error", "Please enter a valid Module ID.", AlertType.ERROR);
            return;
        }

        String selectSQL = "SELECT module_code FROM modules WHERE module_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setString(1, moduleID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String moduleCode = rs.getString("module_code");
                modulecodetxtid.setText(moduleCode);
                showAlert("Success", "Module code loaded successfully!", AlertType.INFORMATION);
            } else {
                showAlert("Not Found", "No module found with the given ID.", AlertType.WARNING);
                clearFields();
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while retrieving the module code.", AlertType.ERROR);
            e.printStackTrace();
        }
    }
    // Get module ID based on module name or module code
    @FXML
    public void GetID(ActionEvent actionEvent) {
        String moduleName = modulenametxtID.getText();
        String moduleCode = modulecodetxtid.getText();

        // Check if both fields are empty
        if (moduleName.isEmpty() && moduleCode.isEmpty()) {
            showAlert("Input Error", "Please enter either a Module Name or Module Code.", AlertType.ERROR);
            return;
        }

        String selectSQL = "SELECT module_id FROM modules WHERE module_name = ? OR module_code = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            // Set parameters based on which field is not empty
            if (!moduleName.isEmpty()) {
                pstmt.setString(1, moduleName);
            } else {
                pstmt.setString(1, "%");  // Use wildcard for module name
            }

            if (!moduleCode.isEmpty()) {
                pstmt.setString(2, moduleCode);
            } else {
                pstmt.setString(2, "%");  // Use wildcard for module code
            }

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String moduleID = rs.getString("module_id");
                moduletxtid.setText(moduleID);
                showAlert("Success", "Module ID loaded successfully!", AlertType.INFORMATION);
            } else {
                showAlert("Not Found", "No module found with the given name or code.", AlertType.WARNING);
                clearFields();
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while retrieving the module ID.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

}
