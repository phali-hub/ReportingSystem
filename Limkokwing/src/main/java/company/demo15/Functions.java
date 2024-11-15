package company.demo15;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static company.demo15.HelloApplication.showAlert;

public class Functions {
    private final DBConnection db;

    public Functions() {
        db = new DBConnection();
    }

    // Method to view all lecturers
    public List<String> viewLecturers() {
        String sql = "SELECT * FROM lecturers";
        List<String> lecturers = new ArrayList<>();
        try {
            DBConnection db = DBConnection.getInstance();
            Connection conn = db.getConnection();
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet resultSet = pstmt.executeQuery();

                while (resultSet.next()) {
                    lecturers.add(resultSet.getString("name"));
                }

                resultSet.close();
                pstmt.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Connection Error", "Database connection is not available.");
                return null;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error fetching lecturers: " + e.getMessage());
        }
        return lecturers;
    }

    public List<String> viewModules() {
        String sql = "SELECT * FROM modules";
        List<String> modules = new ArrayList<>();
        try {
            DBConnection db = DBConnection.getInstance();
            Connection conn = db.getConnection();
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet resultSet = pstmt.executeQuery();

                while (resultSet.next()) {
                    modules.add(resultSet.getString("module_name"));
                }

                resultSet.close();
                pstmt.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Connection Error", "Database connection is not available.");
                return null;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error fetching modules: " + e.getMessage());
        }
        return modules;
    }

    public List<String> viewSemesters() {
        String sql = "SELECT * FROM semesters";
        List<String> semesters = new ArrayList<>();
        try {
            DBConnection db = DBConnection.getInstance();
            Connection conn = db.getConnection();
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet resultSet = pstmt.executeQuery();

                while (resultSet.next()) {
                    semesters.add(resultSet.getString("semester_name"));
                }

                resultSet.close();
                pstmt.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Connection Error", "Database connection is not available.");
                return null;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error fetching semesters: " + e.getMessage());
        }
        return semesters;
    }
    // Method to add a new lecturer
    public void addLecturer(String name, String department) {
        String sql = "INSERT INTO lecturers (name, department) VALUES (?, ?)";
        try {
            if (executeUpdate(sql, name, department) > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Lecturer added successfully.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding lecturer: " + e.getMessage());
        }
    }

    // Method to add a new module
    public void addModule(String moduleName, String moduleCode) {
        String sql = "INSERT INTO modules (module_name, module_code) VALUES (?, ?)";
        try {
            if (executeUpdate(sql, moduleName, moduleCode) > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Module added successfully.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding module: " + e.getMessage());
        }
    }

    // Method to add a new semester
    public void addSemester(String semesterName, int year) {
        String sql = "INSERT INTO semesters (semester_name, year) VALUES (?, ?)";
        try {
            if (executeUpdate(sql, semesterName, String.valueOf(year)) > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Semester added successfully.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding semester: " + e.getMessage());
        }
    }

    // Helper method to execute SQL updates (Insert, Update, Delete)
    private int executeUpdate(String sql, String... params) throws SQLException {
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setString(i + 1, params[i]);
            }
            return pstmt.executeUpdate();
        }
    }

    // Sign-up method
    public void signUp(String username, String password, String role, String employeeNumber) throws SQLException, IOException {
        String sql = "INSERT INTO users (username, password, employee_number, role) VALUES (?, ?, ?, ?)";
        try {
            if (executeUpdate(sql, username, password, employeeNumber, role) > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User signed up successfully.");
                openPage("Login.fxml", "User Selection", 600, 400);
            } else {
                showAlert(Alert.AlertType.ERROR, "SIGN UP ERROR", "Failed to sign up.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error signing up: " + e.getMessage());
        }
    }

    // Login method
    public void login(String username, String password) throws SQLException, IOException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    openPage("Home.fxml", "Home", 600, 400);
                } else {
                    showAlert(Alert.AlertType.WARNING, "SIGNING IN", "Invalid credentials.");
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Error during login: " + e.getMessage());
        }
    }

    // Helper method to navigate pages
    private void openPage(String fxmlFile, String title, int width, int height) throws IOException {
        Stage stage = new Stage();
        HelloApplication.page(stage, fxmlFile, title, width, height);
    }

    // Assign a lecturer to a module in a class, semester, and academic year
    public void assignLecturerToModule(int lecturer_id, int module_id, int class_id, int semester_id, int academicYear_id) {
        String sql = "INSERT INTO lecturer_module_assignments (lecturer_id, module_id, class_id, semester_id, academic_year_id) VALUES (?, ?, ?, ?, ?)";
        try {
            if (executeUpdate(sql, String.valueOf(lecturer_id), String.valueOf(module_id), String.valueOf(class_id), String.valueOf(semester_id), String.valueOf(academicYear_id)) > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Assignment Success", "Lecturer assigned to module successfully.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Assignment Error", "Error assigning lecturer to module.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error assigning lecturer to module: " + e.getMessage());
        }
    }

    // Assign Lecturer role
    public void assignLecturerRole(String lecturer, String role) {
        String sql = "UPDATE lecturers SET role = ? WHERE name = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role);
            stmt.setString(2, lecturer);
            stmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Role assigned successfully.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error assigning role: " + e.getMessage());
        }
    }

    // Fetch all lecturers from the database
    public List<String> getLecturers() {
        return viewLecturers();
    }

    // Fetch all modules from the database
    public List<String> getModules() {
        return viewModules();
    }

    public void assignLecturerToModule(String selectedLecturer, String selectedModule) {

    }
}
