/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Ventana para eliminar un manga del sistema.
 * Permite al usuario confirmar la eliminación del manga seleccionado.
 * Al confirmar, se elimina el manga y todas las series asociadas a él de la base de datos.
 * 
 * @author Party
 */
public class MangasWindowEliminar extends Application {
    private MangasWindow mangasWindow;
    private String idManga;
    private static final String URL = "jdbc:mysql://localhost:3306/proyectofinal";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    /**
     * Constructor de la clase AnimesWindowEliminar.
     * 
     * @param idManga El ID del anime que se va a eliminar.
     * @param mangasWindow La ventana principal de animes, 
     * utilizada para actualizar la tabla de animes después de eliminar uno.
     */
    public MangasWindowEliminar(String idManga, MangasWindow mangasWindow) {
        this.idManga = idManga;
        this.mangasWindow = mangasWindow;
    }

    /**
     * Método principal para iniciar la ventana de eliminación de animes.
     * 
     * @param primaryStage El escenario principal de la aplicación.
     */
    @Override
    public void start(Stage primaryStage) {
        eliminarManga();
    }

    private void eliminarManga() {
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Consulta para contar las series asociadas al manga
            String querySeries = "SELECT COUNT(*) AS count FROM Series WHERE id_manga = ?";
            try (PreparedStatement statementSeries = conexion.prepareStatement(querySeries)) {
                statementSeries.setString(1, idManga);
                try (ResultSet resultSetSeries = statementSeries.executeQuery()) {
                    resultSetSeries.next();
                    int countSeries = resultSetSeries.getInt("count");

                    // Consulta para contar los animes asociados al manga
                    String queryAnimes = "SELECT COUNT(*) AS count FROM Animes WHERE manga_id = ?";
                    try (PreparedStatement statementAnimes = conexion.prepareStatement(queryAnimes)) {
                        statementAnimes.setString(1, idManga);
                        try (ResultSet resultSetAnimes = statementAnimes.executeQuery()) {
                            resultSetAnimes.next();
                            int countAnimes = resultSetAnimes.getInt("count");

                            // Mostrar alerta de confirmación
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmar eliminación");
                            alert.setHeaderText("¿Estás seguro de que quieres eliminar el manga?");
                            if (countSeries > 0 || countAnimes > 0) {
                                alert.setContentText("Esta acción eliminará el manga y las series asociadas.");
                            } else {
                                alert.setContentText("Esta acción eliminará solo el manga.");
                            }
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    try {
                                        if (countSeries > 0) {
                                            eliminarAnimeYSeries(conexion);
                                        } else {
                                            eliminarMangaYAnime(conexion);
                                        }
                                    } catch (SQLException e) {
                                        mostrarAlertaError("Error al eliminar el manga", e.getMessage());
                                    }
                                }
                            });
                        }
                    }
                }
            }
            mangasWindow.actualizarTablaMangas();
        } catch (SQLException e) {
            mostrarAlertaError("Error de conexión", e.getMessage());
        }
    }

    private void eliminarAnimeYSeries(Connection conexion) throws SQLException {
        // Eliminar series
        String eliminarSeriesQuery = "DELETE FROM Series WHERE id_manga = ?";
        try (PreparedStatement eliminarSeriesStatement = conexion.prepareStatement(eliminarSeriesQuery)) {
            eliminarSeriesStatement.setString(1, idManga);
            eliminarSeriesStatement.executeUpdate();
        }
        // Eliminar manga y anime
        eliminarMangaYAnime(conexion);
    }

    private void eliminarMangaYAnime(Connection conexion) throws SQLException {
        // Eliminar anime
        String eliminarAnimeQuery = "DELETE FROM Animes WHERE manga_id = ?";
        try (PreparedStatement eliminarAnimeStatement = conexion.prepareStatement(eliminarAnimeQuery)) {
            eliminarAnimeStatement.setString(1, idManga);
            eliminarAnimeStatement.executeUpdate();
        }
        
        // Eliminar manga
        String eliminarMangaQuery = "DELETE FROM Manga WHERE id = ?";
        try (PreparedStatement eliminarMangaStatement = conexion.prepareStatement(eliminarMangaQuery)) {
            eliminarMangaStatement.setString(1, idManga);
            eliminarMangaStatement.executeUpdate();
        }
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
        
    }

    /**
     * Main de la aplicación, básicamente inicia el resto del código al abrir la aplicación y poco más.
     * 
     * @param args Aquí se encontrarían los argumento que especificarias en la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}