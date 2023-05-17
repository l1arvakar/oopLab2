module com.example.metrics2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.oopLab2 to javafx.fxml;
    exports com.example.oopLab2;
    exports com.example.oopLab2.tools;
    opens com.example.oopLab2.tools to javafx.fxml;
}