package com.example.ultimaentregaad;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class ConfigController {

    @FXML
    private TextField urlField;
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;

    // Método para guardar la configuración en un archivo JSON
    @FXML
    private void guardarConfiguracion() {
        String url = urlField.getText();
        String user = userField.getText();
        String password = passwordField.getText();

        // Validar que los campos no estén vacíos
        if (url.isEmpty() || user.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Todos los campos son obligatorios");
            alert.showAndWait();
            return;
        }

        // Guardar la configuración en el archivo JSON
        DatabaseManager.guardarConfiguracion(url, user, password);

        // Mostrar mensaje de éxito
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Configuración Guardada");
        alert.setHeaderText("La configuración se guardó correctamente.");
        alert.showAndWait();

        // Cerrar la ventana de configuración
        cerrarVentana();
    }

    @FXML
    private void cancelar() {
        // Solo cerrar la ventana sin hacer cambios
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) urlField.getScene().getWindow();
        stage.close();
    }
}
