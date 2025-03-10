module com.example.ultimaentregaad {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires org.json;

    opens com.example.ultimaentregaad to javafx.fxml;
    exports com.example.ultimaentregaad;
}