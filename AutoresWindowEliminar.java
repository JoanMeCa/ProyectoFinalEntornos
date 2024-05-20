/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Ventana para eliminar un autor del sistema.
 * Permite al usuario confirmar la eliminación del autor seleccionado.
 * Al confirmar, se elimina el autor y todas las series asociadas a él de la base de datos.
 * 
 * @author Party
 */
public class AutoresWindowEliminar extends Application {
    private AutoresWindow autoresWindow;
    private String idAutor;
    private static final String URL = "jdbc:mysql://localhost:3306/proyectofinal";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    /**
     * Constructor de la clase AutoresWindowEliminar.
     * 
     * @param idAutor El ID del autor que se va a eliminar.
     * @param autoresWindow La ventana principal de autores, 
     * utilizada para actualizar la tabla de animes después de eliminar uno.
     */
    public AutoresWindowEliminar(String idAutor, AutoresWindow autoresWindow) {
        this.idAutor = idAutor;
        this.autoresWindow = autoresWindow;
    }

    /**
     * Método principal para iniciar la ventana de eliminación de animes.
     * 
     * @param primaryStage El escenario principal de la aplicación.
     * @throws Exception Control de errores.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        Scene scene = new Scene(root, 300, 150);

        // Verificar si el autor está asociado a alguna obra
        boolean autorAsociado = verificarAutorAsociado();

        if (autorAsociado) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(null);
            alerta.setContentText("¡ADVERTENCIA! Este autor tiene una o más obras a su nombre. " +
                    "Eliminarlo eliminará las obras realizadas.");

            ButtonType botonSi = new ButtonType("Eliminar");
            ButtonType botonNo = new ButtonType("Cancelar");
            alerta.getButtonTypes().setAll(botonSi, botonNo);

            alerta.showAndWait().ifPresent(response -> {
                if (response == botonSi) {
                    try {
                        eliminarAutorYObra(); // Eliminar autor y todas sus obras
                        autoresWindow.actualizarTablaAutores();
                        primaryStage.close(); // Cerrar la ventana al confirmar la eliminación
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        System.out.println("Error al eliminar el autor y sus obras.");
                    }
                }
            });
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(null);
            alerta.setContentText("¿Estás seguro de que quieres eliminar al autor?");

            // Añadir botones de confirmación y cancelación
            ButtonType botonSi = new ButtonType("Sí");
            ButtonType botonNo = new ButtonType("No");
            alerta.getButtonTypes().setAll(botonSi, botonNo);

            alerta.showAndWait().ifPresent(response -> {
                if (response == botonSi) {
                    try {
                        eliminarAutor(); // Eliminar autor
                        autoresWindow.actualizarTablaAutores();
                        primaryStage.close(); // Cerrar la ventana al confirmar la eliminación
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        System.out.println("Error al eliminar el autor y sus obras.");
                    }
                }
            });
        }
    }

    // Método para verificar si el autor está asociado a alguna obra
    private boolean verificarAutorAsociado() throws SQLException {
        Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        String query = "SELECT COUNT(*) FROM Series WHERE id_autor = ?";
        PreparedStatement statement = conexion.prepareStatement(query);
        statement.setString(1, idAutor);
        ResultSet resultado = statement.executeQuery();

        if (resultado.next()) {
            int countSeries = resultado.getInt(1);

            // Verificar también los animes asociados al autor
            query = "SELECT COUNT(*) FROM animes WHERE autor_id = ?";
            statement = conexion.prepareStatement(query);
            statement.setString(1, idAutor);
            resultado = statement.executeQuery();

            if (resultado.next()) {
                int countAnimes = resultado.getInt(1);

                // Verificar también los mangas asociados al autor
                query = "SELECT COUNT(*) FROM Manga WHERE autor_id = ?";
                statement = conexion.prepareStatement(query);
                statement.setString(1, idAutor);
                resultado = statement.executeQuery();

                if (resultado.next()) {
                    int countManga = resultado.getInt(1);

                    conexion.close();
                    return countSeries > 0 || countAnimes > 0 || countManga > 0;
                }
            }
        }

        conexion.close();
        return false;
    }

    // Método para eliminar autor y sus obras
    private void eliminarAutorYObra() throws SQLException {
        Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        conexion.setAutoCommit(false); // Desactivar el autocommit para realizar la transacción

        try {
            // Eliminar los registros de la tabla Series asociados al autor
            String deleteSeries = "DELETE FROM Series WHERE id_autor = ?";
            PreparedStatement statementSeries = conexion.prepareStatement(deleteSeries);
            statementSeries.setString(1, idAutor);
            statementSeries.executeUpdate();
            
            // Eliminar los registros de la tabla animes asociados al autor
            String deleteAnimes = "DELETE FROM animes WHERE autor_id = ?";
            PreparedStatement statementAnimes = conexion.prepareStatement(deleteAnimes);
            statementAnimes.setString(1, idAutor);
            statementAnimes.executeUpdate();

            // Eliminar los registros de la tabla manga asociados al autor
            String deleteManga = "DELETE FROM Manga WHERE autor_id = ?";
            PreparedStatement statementManga = conexion.prepareStatement(deleteManga);
            statementManga.setString(1, idAutor);
            statementManga.executeUpdate();

            // Eliminar al autor de la tabla Autores
            String deleteAutor = "DELETE FROM Autores WHERE id = ?";
            PreparedStatement statementAutor = conexion.prepareStatement(deleteAutor);
            statementAutor.setString(1, idAutor);
            statementAutor.executeUpdate();

            // Confirmar la transacción
            conexion.commit();
        } catch (SQLException e) {
            // Si ocurre algún error, hacer rollback para deshacer los cambios
            conexion.rollback();
            throw e; // Lanzar la excepción para manejarla fuera de este método
        } finally {
            // Restaurar el autocommit a su estado original y cerrar la conexión
            conexion.setAutoCommit(true);
            conexion.close();
        }
    }

    // Método para eliminar solo al autor
    private void eliminarAutor() throws SQLException {
        Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        String deleteAutor = "DELETE FROM Autores WHERE id = ?";
        PreparedStatement statementAutor = conexion.prepareStatement(deleteAutor);
        statementAutor.setString(1, idAutor);
        statementAutor.executeUpdate();

        conexion.close();
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