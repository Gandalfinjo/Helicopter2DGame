module com.example.helicopter2dgame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.helicopter2dgame to javafx.fxml;
    exports com.example.helicopter2dgame;
}