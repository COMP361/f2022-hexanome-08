module com.result.splendorguim3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.result.splendorguim3 to javafx.fxml;
    exports com.result.splendorguim3;
}