package company.demo15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminRoles {

    private final Functions fn = new Functions();

    @FXML
    private ImageView img;

    @FXML
    private ComboBox<String> lecturerComboBox;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private ComboBox<String> lecturer_idComboBox;

    @FXML
    private ComboBox<String> moduleComboBox;

    @FXML
    private void initialize() {
        lecturerComboBox.setItems(FXCollections.observableArrayList("sipho","..."));
        roleComboBox.setItems(FXCollections.observableArrayList("Lecturer", "Senior Lecturer", "Professor"));
        lecturer_idComboBox.setItems(FXCollections.observableArrayList("sipho","..."));
        moduleComboBox.setItems(FXCollections.observableArrayList("java programming","stats","..."));
    }

    private void loadScene(String fxmlFile, String title, int width, int height) throws IOException {
        Stage stage = new Stage();
        HelloApplication.page(stage, fxmlFile, title.toUpperCase(), width, height);
    }

    @FXML
    void AddLecturer(ActionEvent event) throws IOException {
        loadScene("Lecturer.fxml", "Lecturers", 600, 400);
    }

    @FXML
    void AddModule(ActionEvent event) throws IOException {
        loadScene("AddModule.fxml", "Modules", 600, 400);
    }

    @FXML
    void AddStudents(ActionEvent event) throws IOException {
        loadScene("AddStudents.fxml", "Students", 600, 400);
    }

    @FXML
    void AddYear(ActionEvent event) throws IOException {
        loadScene("Addacademic.fxml", "Academic Year", 600, 400);
    }

    @FXML
    void Addsemester(ActionEvent event) throws IOException {
        loadScene("addsemester.fxml", "Semesters", 600, 400);
    }

    @FXML
    void Addclasses(ActionEvent event) throws IOException {
        loadScene("addclass.fxml", "Classes", 600, 400);
    }

    @FXML
    void ViewLecturers(ActionEvent actionEvent) {
        List<String> lecturers = fn.viewLecturers();
        showAlert("Lecturers", String.join("\n", lecturers), Alert.AlertType.INFORMATION);
    }

    @FXML
    void ViewModules(ActionEvent actionEvent) {
        List<String> modules = fn.viewModules();
        showAlert("Modules", String.join("\n", modules), Alert.AlertType.INFORMATION);
    }

    @FXML
    void ViewSemesters(ActionEvent actionEvent) {
        List<String> semesters = fn.viewSemesters();
        showAlert("Semesters", String.join("\n", semesters), Alert.AlertType.INFORMATION);
    }

    @FXML
    public void AssignLecturerRole(ActionEvent actionEvent) {
        String selectedLecturer = lecturerComboBox.getValue();
        String selectedRole = roleComboBox.getValue();

        if (selectedLecturer == null || selectedRole == null) {
            showAlert("Error", "Please select both a lecturer and a role", Alert.AlertType.ERROR);
        } else {
            fn.assignLecturerRole(selectedLecturer, selectedRole);
            showAlert("Success", "Role assigned successfully", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void AssignLecturerToModule(ActionEvent actionEvent) {
        String selectedLecturer = lecturer_idComboBox.getValue();
        String selectedModule = moduleComboBox.getValue();

        if (selectedLecturer == null || selectedModule == null) {
            showAlert("Error", "Please select both a lecturer and a module", Alert.AlertType.ERROR);
        } else {
            fn.assignLecturerToModule(selectedLecturer, selectedModule);
            showAlert("Success", "Lecturer assigned to module successfully", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        loadScene("Login.fxml", "Login", 400, 300);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
