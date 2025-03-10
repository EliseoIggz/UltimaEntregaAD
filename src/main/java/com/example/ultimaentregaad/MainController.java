package com.example.ultimaentregaad;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {
    @FXML private TextField txtNombre;
    @FXML private TextField txtTrabajo;
    @FXML private TextField txtEdad;
    @FXML private TableView<Persona> tableView;
    @FXML private TableColumn<Persona, String> colNombre;
    @FXML private TableColumn<Persona, String> colTrabajo;
    @FXML private TableColumn<Persona, Integer> colEdad;

    private DatabaseManager dbManager;
    private ObservableList<Persona> personas;

    public void initialize() {
        // Definir los eventos
        dbManager = new DatabaseManager(
                message -> System.out.println("Conexión: " + message),          // onConnected
                message -> System.out.println("Consulta ejecutada: " + message), // onQueryExecuted
                message -> System.err.println("Error: " + message)              // onError
        );

        // Configuración de la base de datos (esto es solo un ejemplo)
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "abc123.";

        // Guardar la configuración en el archivo JSON
        DatabaseManager.guardarConfiguracion(url, user, password);

        // Conectar a la base de datos
        dbManager.connect();  // Aquí es donde realizamos la conexión a la base de datos

        // Configuración de la tabla y los campos
        personas = FXCollections.observableArrayList();

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTrabajo.setCellValueFactory(new PropertyValueFactory<>("trabajo"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));

        tableView.setItems(personas);

        cargarDatos();

        // Establecer el evento para cerrar la conexión cuando la ventana se cierre
        setWindowCloseEvent();
    }

    private void setWindowCloseEvent() {
        // Usamos Platform.runLater para garantizar que se ejecute después de que la escena haya sido completamente cargada
        Platform.runLater(() -> {
            if (tableView.getScene() != null) {
                tableView.getScene().getWindow().setOnCloseRequest(closeEvent -> {
                    dbManager.disconnect(); // Llamamos al disconnect cuando la ventana se cierre
                });
            }
        });
    }

    @FXML
    private void agregarPersona() {
        String nombre = txtNombre.getText();
        String trabajo = txtTrabajo.getText();
        int edad = Integer.parseInt(txtEdad.getText());

        String query = String.format("INSERT INTO tablaBD (nombre, trabajo, edad) VALUES ('%s', '%s', %d)", nombre, trabajo, edad);
        dbManager.executeUpdate(query);
        cargarDatos();
    }

    @FXML
    private void eliminarPersona() {
        Persona persona = tableView.getSelectionModel().getSelectedItem();
        if (persona != null) {
            String query = String.format("DELETE FROM tablaBD WHERE nombre = '%s'", persona.getNombre());
            dbManager.executeUpdate(query);
            cargarDatos();
        }
    }

    @FXML
    private void cargarDatos() {
        personas.clear();
        ResultSet rs = dbManager.executeQuery("SELECT * FROM tablaBD");
        try {
            while (rs.next()) {
                personas.add(new Persona(rs.getString("nombre"), rs.getString("trabajo"), rs.getInt("edad")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
