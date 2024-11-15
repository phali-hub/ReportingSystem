module company.demo15 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens company.demo15 to javafx.fxml;
    exports company.demo15;
}