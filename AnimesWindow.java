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
import javafx.scene.Scene;
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
import static javafx.application.Application.launch;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;

/**
 * Ventana principal para mostrar y gestionar la información de los animes.
 * Muestra la tabla de animes en la BBDD y permite realizar operaciones como añadir o eliminar animes.
 * @author Party
 */
public class AnimesWindow extends Application {

    private TableView<String[]> tableView;
    private static final String URL = "jdbc:mysql://localhost:3306/proyectofinal";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    /**
     * Inicia la aplicación, creando la tabla y la ventana
     * @param primaryStage El escenario, donde se mostrará todo
     * @throws Exception En caso de error, se lanza el Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        tableView = new TableView<>();
        TableColumn<String[], String> idColumn = new TableColumn<>("ID");
        TableColumn<String[], String> nombreColumn = new TableColumn<>("Nombre");
        TableColumn<String[], String> autorColumn = new TableColumn<>("Autor");
        TableColumn<String[], String> mangaColumn = new TableColumn<>("Manga");
        TableColumn<String[], String> estadoColumn = new TableColumn<>("Estado");
        TableColumn<String[], String> capitulosColumn = new TableColumn<>("Nº Capitulos");
        TableColumn<String[], Void> accionesColumn = new TableColumn<>("Acciones");

        // Configuración del CellValueFactory para cada columna
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        nombreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        autorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));
        mangaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3]));
        estadoColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[4]));
        capitulosColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[5]));

        // Configuración de las propiedades de las columnas para que ocupen el 100% del ancho disponible
        idColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.14285714285));
        nombreColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.14285714285));
        autorColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.14285714285));
        mangaColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.14285714285));
        estadoColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.14285714285));
        capitulosColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.14285714285));
        accionesColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.14285714285));

        // Crear botón en la columna de acciones
        accionesColumn.setCellFactory(param -> new TableCell<String[], Void>() {
            private final Button botonEliminar = new Button("Eliminar");

            {
                botonEliminar.setOnAction(event -> {
                    String[] fila = getTableView().getItems().get(getIndex());
                    String idAnime = fila[0]; // Obtener el ID del anime
                    AnimesWindowEliminar ventanaEliminar = new AnimesWindowEliminar(idAnime, AnimesWindow.this);
                    try {
                        ventanaEliminar.start(new Stage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
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
        tableView.getColumns().addAll(idColumn, nombreColumn, autorColumn, mangaColumn, estadoColumn, capitulosColumn, accionesColumn);

        VBox root = new VBox(tableView);

        // Crear botones
        Button boton1 = new Button("Volver");
        Button boton2 = new Button("Añadir Anime");

        // Acción del botón 1
        boton1.setOnAction(e -> {
            primaryStage.close();
            Main main = new Main();
            try {
                main.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Acción del botón 2
        boton2.setOnAction(e -> {
            AnimesWindowCreate animesWindowCreate = new AnimesWindowCreate(this); // Pasar una referencia de AutoresWindow
            try {
                animesWindowCreate.start(new Stage()); // Mostrar la ventana AutoresWindowCreate
            } catch (Exception ex) {
                ex.printStackTrace(); // Manejar la excepción
            }
        });

        // Organizar botones horizontalmente
        HBox buttons = new HBox(boton1, boton2);
        buttons.setSpacing(10); // Espacio entre los botones

        // Configuración de ancho de los botones
        boton1.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.5));
        boton2.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.5));

        root.getChildren().addAll(buttons);

        Scene scene = new Scene(root, 650, 400);

        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            Statement consulta = conexion.createStatement();
            ResultSet resultado = consulta.executeQuery("SELECT * FROM Animes");

            while (resultado.next()) {
                int idAutor = resultado.getInt("autor_id");
                String nombreAutor = getNombreAutor(idAutor);
                int idManga = resultado.getInt("manga_id");
                String nombreManga = getNombreManga(idManga);
                String[] row = {
                        Integer.toString(resultado.getInt("id")),
                        resultado.getString("nombre"),
                        nombreAutor,
                        nombreManga,
                        resultado.getString("estado"),
                        Integer.toString(resultado.getInt("num_capitulos")),
                };

                tableView.getItems().add(row);
            }

            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Ventana de Animes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Main de la aplicación, básicamente inicia el resto del código al abrir la aplicación y poco más.
     * @param args Aquí se encontrarían los argumento que especificarias en la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
    private String getNombreAutor(int idAutor) {
        String nombreAutor = "";
        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            String query = "SELECT nombre FROM Autores WHERE id = ?";
            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setInt(1, idAutor);
            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                nombreAutor = resultado.getString("nombre");
            }

            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreAutor;
    }
    private String getNombreManga(int idManga) {
        String nombreManga = "";
        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            String query = "SELECT nombre FROM Manga WHERE id = ?";
            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setInt(1, idManga);
            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                nombreManga = resultado.getString("nombre");
            }

            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreManga;
    }

    /**
     * Al crear o eliminar un Anime se llama a esta función que limpia la tabla y la vuelve a imprimir con los
     * datos actualizados obtenidos de la base de datos.
     */
    public void actualizarTablaAnimes() {
        tableView.getItems().clear(); // Limpiar los elementos actuales en la tabla
        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            Statement consulta = conexion.createStatement();
            ResultSet resultado = consulta.executeQuery("SELECT * FROM Animes");

            while (resultado.next()) {
                int idAutor = resultado.getInt("autor_id");
                String nombreAutor = getNombreAutor(idAutor);
                int idManga = resultado.getInt("manga_id");
                String nombreManga = getNombreManga(idManga);
                String[] row = {
                        Integer.toString(resultado.getInt("id")),
                        resultado.getString("nombre"),
                        nombreAutor,
                        nombreManga,
                        resultado.getString("estado"),
                        Integer.toString(resultado.getInt("num_capitulos")),
                };

                tableView.getItems().add(row);
            }

            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}