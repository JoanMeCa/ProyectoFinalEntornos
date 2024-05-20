/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Ventana principal para mostrar y gestionar la información de los mangas.
 * Muestra la tabla de mangas de la BBDD y permite realizar operaciones como añadir o eliminar mangas.
 * 
 * @author Party
 */
public class MangasWindow extends Application {

    private TableView<String[]> tableView;
    private static final String URL = "jdbc:mysql://localhost:3306/proyectofinal";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    /**
     * Inicia la aplicación, creando la tabla y la ventana.
     * 
     * @param primaryStage El escenario, donde se mostrará todo
     */
    @Override
    public void start(Stage primaryStage) {
        tableView = new TableView<>();
        TableColumn<String[], String> idColumn = new TableColumn<>("ID");
        TableColumn<String[], String> nombreColumn = new TableColumn<>("Nombre");
        TableColumn<String[], String> autorColumn = new TableColumn<>("Autor");
        TableColumn<String[], String> estadoColumn = new TableColumn<>("Estado");
        TableColumn<String[], String> capitulosColumn = new TableColumn<>("Nº Capitulos");
        TableColumn<String[], Void> accionesColumn = new TableColumn<>("Acciones");

        // Configuración del CellValueFactory para cada columna
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        nombreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        autorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));
        estadoColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3]));
        capitulosColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[4]));

        // Configuración de las propiedades de las columnas para que ocupen el 100% del ancho disponible
        idColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.167));
        nombreColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.167));
        autorColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.167));
        estadoColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.167));
        capitulosColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.167));
        accionesColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.167));

        // Crear botón en la columna de acciones
        accionesColumn.setCellFactory(param -> new TableCell<String[], Void>() {
            private final Button botonEliminar = new Button("Eliminar");

            {
                botonEliminar.setOnAction(event -> {
                    String[] fila = getTableView().getItems().get(getIndex());
                    String idManga = fila[0]; // Obtener el ID del manga
                    MangasWindowEliminar ventanaEliminar = new MangasWindowEliminar(idManga, MangasWindow.this);
                    try {
                        ventanaEliminar.start(new Stage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        mostrarAlertaError("Error al abrir la ventana de eliminación", ex.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(botonEliminar);
                }
            }
        });

        // Agregar columnas a la tabla
        tableView.getColumns().addAll(idColumn, nombreColumn, autorColumn, estadoColumn, capitulosColumn, accionesColumn);

        VBox root = new VBox(tableView);

        // Crear botones
        Button botonVolver = new Button("Volver");
        Button botonAñadir = new Button("Añadir Manga");

        // Acción del botón "Volver"
        botonVolver.setOnAction(e -> {
            primaryStage.close();
            Main main = new Main();
            try {
                main.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlertaError("Error al volver al menú principal", ex.getMessage());
            }
        });

        // Acción del botón "Añadir Manga"
        botonAñadir.setOnAction(e -> {
            MangasWindowCreate mangasWindowCreate = new MangasWindowCreate(this); // Pasar una referencia de MangasWindow
            try {
                mangasWindowCreate.start(new Stage()); // Mostrar la ventana MangasWindowCreate
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlertaError("Error al abrir la ventana de creación", ex.getMessage());
            }
        });

        // Organizar botones horizontalmente
        HBox buttons = new HBox(botonVolver, botonAñadir);
        buttons.setSpacing(10); // Espacio entre los botones

        // Configuración de ancho de los botones
        botonVolver.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.5));
        botonAñadir.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.5));

        root.getChildren().addAll(buttons);

        Scene scene = new Scene(root, 650, 400);

        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             Statement consulta = conexion.createStatement();
             ResultSet resultado = consulta.executeQuery("SELECT * FROM Manga")) {

            while (resultado.next()) {
                int idAutor = resultado.getInt("autor_id");
                String nombreAutor = getNombreAutor(idAutor);
                String[] row = {
                        Integer.toString(resultado.getInt("id")),
                        resultado.getString("nombre"),
                        nombreAutor,
                        resultado.getString("estado"),
                        Integer.toString(resultado.getInt("num_capitulos")),
                };

                tableView.getItems().add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al cargar los datos de los mangas", e.getMessage());
        }

        primaryStage.setTitle("Ventana de Mangas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Main de la aplicación, básicamente inicia el resto del código al abrir la aplicación y poco más.
     * 
     * @param args Aquí se encontrarían los argumento que especificarias en la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }

    private String getNombreAutor(int idAutor) {
        String nombreAutor = "";
        String query = "SELECT nombre FROM Autores WHERE id = ?";
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, idAutor);
            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    nombreAutor = resultado.getString("nombre");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al obtener el nombre del autor", e.getMessage());
        }
        return nombreAutor;
    }

    /**
     *
     */
    public void actualizarTablaMangas() {
        tableView.getItems().clear(); // Limpiar los elementos actuales en la tabla
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             Statement consulta = conexion.createStatement();
             ResultSet resultado = consulta.executeQuery("SELECT * FROM Manga")) {

            while (resultado.next()) {
                int idAutor = resultado.getInt("autor_id");
                String nombreAutor = getNombreAutor(idAutor);
                String[] row = {
                        Integer.toString(resultado.getInt("id")),
                        resultado.getString("nombre"),
                        nombreAutor,
                        resultado.getString("estado"),
                        Integer.toString(resultado.getInt("num_capitulos")),
                };

                tableView.getItems().add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al actualizar los datos de los mangas", e.getMessage());
        }
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlertaInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}