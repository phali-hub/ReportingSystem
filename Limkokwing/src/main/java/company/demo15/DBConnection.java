package company.demo15;

import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
    private final String url = "jdbc:mysql://127.0.0.1:3306/limkokwing_academic_reporting"; // Your database URL
    private final String user = "root"; // Your database username
    private final String pass = "@Kurata2002"; // Your database password
    private Connection connect;  // Corrected variable name
    private PreparedStatement stm;
    private ResultSet res;

    // Singleton instance
    private static DBConnection instance;

    // Private constructor to prevent direct instantiation
    DBConnection() {
        try {
            // Establishing the connection
            this.connect = DriverManager.getConnection(this.url, this.user, this.pass);  // Use 'connect' here
        } catch (SQLException e) {
            HelloApplication.showAlert(Alert.AlertType.ERROR, "DATABASE ERROR", "Error connecting to the database: " + e.getMessage());
        }
    }

    // Method to get the singleton instance of DBConnection
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();  // Create the instance if it doesn't exist
        }
        return instance;
    }

    // Method to get the current connection
    public Connection getConnection() {
        return this.connect;
    }

    public PreparedStatement getStm() {
        return stm;
    }

    public void setStm(String sql) throws SQLException {
        this.stm = this.connect.prepareStatement(sql);  // Use 'connect' here
    }

    public ResultSet getRes() {
        return res;
    }

    public void setRes() throws SQLException {
        this.res = this.stm.executeQuery();  // Use 'stm' here
    }

    public void closeConnection() {
        try {
            if (res != null) res.close(); // Close ResultSet
            if (stm != null) stm.close(); // Close PreparedStatement
            if (connect != null) connect.close(); // Close Connection (Use 'connect' instead of 'conn')
        } catch (SQLException e) {
            HelloApplication.showAlert(Alert.AlertType.ERROR, "CLOSING CONNECTION", "Error closing the connection: " + e.getMessage());
        }
    }
}
