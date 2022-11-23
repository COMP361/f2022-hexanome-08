module com.example.model {
  requires javafx.controls;
  requires javafx.fxml;


  opens com.example.model to javafx.fxml;
  exports com.example.model;
}