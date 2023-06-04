module com.example.oopLab {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires gson.extras;

    requires plugin;

    requires org.bouncycastle.provider;
    opens com.example.oopLab2 to javafx.fxml;
    exports com.example.oopLab2;
    exports com.example.oopLab2.tools;
    opens com.example.oopLab2.tools to javafx.fxml;

    exports com.example.oopLab2.hierarchy;
    opens com.example.oopLab2.hierarchy to javafx.fxml;

    exports com.example.oopLab2.factories;
    opens com.example.oopLab2.factories to javafx.fxml;


}