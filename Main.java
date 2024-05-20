/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;
/**
 *
 * @author Party
 */
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
 *
 * @author Party
 */
public class Main extends Application {

    private static final String URL = "jdbc:mysql://localhost:3306/proyectofinal";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    /**
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        TableView<String[]> tableView = new TableView<>();
        TableColumn<String[], String> idColumn = new TableColumn<>("ID");
        TableColumn<String[], String> animeColumn = new TableColumn<>("Anime");
        TableColumn<String[], String> mangaColumn = new TableColumn<>("Manga");
        TableColumn<String[], String> autorColumn = new TableColumn<>("Autor");

        // Configuración del CellValueFactory para cada columna
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        animeColumn.setCellValueFactory(data -> new SimpleStringProperty(getNombreById("Animes", Integer.parseInt(data.getValue()[1]), "nombre")));
        mangaColumn.setCellValueFactory(data -> new SimpleStringProperty(getNombreById("Manga", Integer.parseInt(data.getValue()[2]), "nombre")));
        autorColumn.setCellValueFactory(data -> new SimpleStringProperty(getNombreById("Autores", Integer.parseInt(data.getValue()[3]), "nombre")));

        tableView.getColumns().addAll(idColumn, animeColumn, mangaColumn, autorColumn);

        // Configuración de las propiedades de las columnas para que se expandan
        idColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        animeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        mangaColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        autorColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));

        VBox root = new VBox(tableView);

        // Crear botones
        Button boton1 = new Button("Autores");
        Button boton2 = new Button("Mangas");
        Button boton3 = new Button("Animes");

        // Acción del botón "Autores"
        boton1.setOnAction(e -> {
            primaryStage.close();
            AutoresWindow autoresWindow = new AutoresWindow();
            try {
                autoresWindow.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlertaError("Error al abrir la ventana de autores", ex.getMessage());
            }
        });

        // Acción del botón "Mangas"
        boton2.setOnAction(e -> {
            primaryStage.close();
            MangasWindow mangasWindow = new MangasWindow();
            try {
                mangasWindow.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlertaError("Error al abrir la ventana de mangas", ex.getMessage());
            }
        });

        // Acción del botón "Animes"
        boton3.setOnAction(e -> {
            primaryStage.close();
            AnimesWindow animesWindow = new AnimesWindow();
            try {
                animesWindow.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlertaError("Error al abrir la ventana de animes", ex.getMessage());
            }
        });

        HBox buttons = new HBox(boton1, boton2, boton3);
        buttons.setSpacing(10); // Espacio entre los botones
        boton1.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.33));
        boton2.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.33));
        boton3.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.33));

        root.getChildren().addAll(buttons);

        Scene scene = new Scene(root, 650, 400);
        
        buscarYAgregarSeries(tableView);

        primaryStage.setTitle("Lista de Series");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para obtener el nombre por ID de la tabla especificada
    private String getNombreById(String tabla, int id, String columna) {
        String query = "SELECT " + columna + " FROM " + tabla + " WHERE id = ?";
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getString(columna);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al obtener el nombre de la tabla " + tabla, e.getMessage());
        }
        return null;
    }

    private void buscarYAgregarSeries(TableView<String[]> tableView) {
        String query = "SELECT DISTINCT a.id AS autor_id, m.id AS manga_id, n.id AS anime_id " +
                       "FROM Autores a " +
                       "INNER JOIN Manga m ON a.id = m.autor_id " +
                       "INNER JOIN Animes n ON a.id = n.autor_id";
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             Statement consulta = conexion.createStatement();
             ResultSet resultado = consulta.executeQuery(query)) {

            while (resultado.next()) {
                int autorId = resultado.getInt("autor_id");
                int mangaId = resultado.getInt("manga_id");
                int animeId = resultado.getInt("anime_id");

                // Verificar si ya existe esta serie en la tabla de series
                if (!existeSerie(autorId, mangaId, animeId, conexion)) {
                    insertarSerie(autorId, mangaId, animeId, conexion);
                }
            }

            // Actualizar la tabla con los datos actuales
            actualizarTabla(tableView);
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al buscar y agregar series", e.getMessage());
        }
    }

    // Método para verificar si una serie ya existe en la tabla de series
    private boolean existeSerie(int autorId, int mangaId, int animeId, Connection conexion) throws SQLException {
        String consulta = "SELECT COUNT(*) AS count FROM Series WHERE id_autor = ? AND id_manga = ? AND id_anime = ?";
        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setInt(1, autorId);
            statement.setInt(2, mangaId);
            statement.setInt(3, animeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count") > 0;
                }
            }
        }
        return false;
    }

    // Método para insertar una nueva serie en la tabla de series
    private void insertarSerie(int autorId, int mangaId, int animeId, Connection conexion) throws SQLException {
        String consulta = "INSERT INTO Series (id_autor, id_manga, id_anime) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setInt(1, autorId);
            statement.setInt(2, mangaId);
            statement.setInt(3, animeId);
            statement.executeUpdate();
        }
    }

    // Método para actualizar la tabla con los datos actuales
    private void actualizarTabla(TableView<String[]> tableView) {
        tableView.getItems().clear(); // Limpiar la tabla antes de actualizar
        String query = "SELECT * FROM Series";
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             Statement consulta = conexion.createStatement();
             ResultSet resultado = consulta.executeQuery(query)) {

            while (resultado.next()) {
                String[] row = {
                    Integer.toString(resultado.getInt("id")),
                    Integer.toString(resultado.getInt("id_anime")),
                    Integer.toString(resultado.getInt("id_manga")),
                    Integer.toString(resultado.getInt("id_autor"))
                };

                tableView.getItems().add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al actualizar la tabla de series", e.getMessage());
        }
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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