module com.nepaltourism.nepal_tourism3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.nepaltourism.nepal_tourism3 to javafx.fxml;
    opens com.nepaltourism.nepal_tourism3.controller to javafx.fxml;
    opens com.nepaltourism.nepal_tourism3.model to javafx.base;

    exports com.nepaltourism.nepal_tourism3;
    exports com.nepaltourism.nepal_tourism3.controller;
}
