package com.example.ultimaentregaad;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.function.Consumer;

public class DatabaseManager {
    private Connection connection;
    private Properties config;

    // Eventos específicos
    private Consumer<String> onConnected;
    private Consumer<String> onQueryExecuted;
    private Consumer<String> onError;

    public DatabaseManager(Consumer<String> onConnected, Consumer<String> onQueryExecuted, Consumer<String> onError) {
        this.onConnected = onConnected;
        this.onQueryExecuted = onQueryExecuted;
        this.onError = onError;
        loadConfig();
    }

    public static void guardarConfiguracion(String url, String user, String password) {
        JSONObject configJson = new JSONObject();
        configJson.put("url", url);
        configJson.put("user", user);
        configJson.put("password", password);

        // Escribir el archivo JSON en disco
        try (FileWriter file = new FileWriter("db_config.json")) {
            file.write(configJson.toString(4)); // '4' es la cantidad de espacios para indentación
            System.out.println("Configuración guardada correctamente en db_config.json.");
        } catch (IOException e) {
            System.err.println("Error al guardar la configuración: " + e.getMessage());
        }
    }

    private void loadConfig() {
        try (FileReader reader = new FileReader("db_config.json")) {
            JSONObject json = new JSONObject(new JSONTokener(reader));
            config = new Properties();
            config.setProperty("url", json.getString("url"));
            config.setProperty("user", json.getString("user"));
            config.setProperty("password", json.getString("password"));
            // No necesitamos un callback aquí porque no es un evento importante
        } catch (Exception e) {
            notifyError("Error cargando configuración: " + e.getMessage());
        }
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(config.getProperty("url"), config.getProperty("user"), config.getProperty("password"));
            notifyConnected("Conexión establecida correctamente.");
        } catch (SQLException e) {
            notifyError("Error al conectar: " + e.getMessage());
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            notifyQueryExecuted("Consulta ejecutada: " + query);
            return resultSet;
        } catch (SQLException e) {
            notifyError("Error ejecutando consulta: " + e.getMessage());
            return null;
        }
    }

    public void executeUpdate(String query) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            notifyQueryExecuted("Actualización realizada: " + query);
        } catch (SQLException e) {
            notifyError("Error en actualización: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                // No es necesario un callback aquí, pero podemos agregar uno si lo necesitas
            }
        } catch (SQLException e) {
            notifyError("Error al cerrar conexión: " + e.getMessage());
        }
    }

    // Métodos para notificar los eventos
    private void notifyConnected(String message) {
        if (onConnected != null) {
            onConnected.accept(message);
        }
    }

    private void notifyQueryExecuted(String message) {
        if (onQueryExecuted != null) {
            onQueryExecuted.accept(message);
        }
    }

    private void notifyError(String message) {
        if (onError != null) {
            onError.accept(message);
        }
    }
}
