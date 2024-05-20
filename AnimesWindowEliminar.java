/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Ventana para eliminar un anime del sistema.
 * Permite al usuario confirmar la eliminación del anime seleccionado.
 * Al confirmar, se elimina el anime y todas las series asociadas a él de la base de datos.
 * 
 * @author Party
 */
public class AnimesWindowEliminar extends Application {
    private AnimesWindow animesWindow;
    private String idAnime;
    private static final String URL = "jdbc:mysql://localhost:3306/proyectofinal";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    /**
     * Constructor de la clase AnimesWindowEliminar.
     * 
     * @param idAnime El ID del anime que se va a eliminar.
     * @param animesWindow La ventana principal de animes, utilizada para actualizar la tabla de animes después de eliminar uno.
     */
    public AnimesWindowEliminar(String idAnime, AnimesWindow animesWindow) {
        this.idAnime = idAnime;
        this.animesWindow = animesWindow;
    }

    /**
     * Método principal para iniciar la ventana de eliminación de animes.
     * 
     * @param primaryStage El escenario principal de la aplicación.
     */
    @Override
    public void start(Stage primaryStage) {
        eliminarAnime();
    }

    private void eliminarAnime() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de que quieres eliminar el anime?");
        alert.setContentText("Esta acción eliminará el anime.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
                    // Eliminar las series asociadas al anime
                    String eliminarSeriesQuery = "DELETE FROM Series WHERE id_anime = ?";
                    try (PreparedStatement eliminarSeriesStatement = conexion.prepareStatement(eliminarSeriesQuery)) {
                        eliminarSeriesStatement.setString(1, idAnime);
                        eliminarSeriesStatement.executeUpdate();
                    }

                    // Eliminar el anime
                    String eliminarAnimeQuery = "DELETE FROM Animes WHERE id = ?";
                    try (PreparedStatement eliminarAnimeStatement = conexion.prepareStatement(eliminarAnimeQuery)) {
                        eliminarAnimeStatement.setString(1, idAnime);
                        eliminarAnimeStatement.executeUpdate();
                    }

                    animesWindow.actualizarTablaAnimes();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El anime ha sido eliminado correctamente.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "Hubo un problema al eliminar el anime.");
                }
            }
        });
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Main de la aplicación, básicamente inicia el resto del código al abrir la aplicación y poco más.
     * @param args Aquí se encontrarían los argumento que especificarias en la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}