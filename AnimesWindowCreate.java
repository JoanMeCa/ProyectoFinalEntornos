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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextFormatter;

/**
 * Ventana para crear un nuevo anime en el sistema.
 * Permite al usuario introducir información sobre el nuevo anime, como nombre, estado, número de capítulos, autor y manga asociado.
 * También realiza validaciones y operaciones en la base de datos para guardar el nuevo anime.
 * @author Party
 */
public class AnimesWindowCreate extends Application {

    private AnimesWindow animesWindow;

    /**
     * Constructor de la clase AnimesWindowCreate.
     * @param animesWindow La ventana principal de animes, utilizada para actualizar la tabla de animes después de añadir uno nuevo.
     */
    public AnimesWindowCreate(AnimesWindow animesWindow) {
        this.animesWindow = animesWindow;
    }

    private static final String URL = "jdbc:mysql://localhost:3306/proyectofinal";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    /**
     * Método principal para iniciar la ventana de creación de animes.
     * 
     * @param primaryStage El escenario principal de la aplicación.
     * @throws Exception Excepción en caso de error al iniciar la ventana.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(10);

        Scene scene = new Scene(root, 300, 250);

        Label nameLabel = new Label("Nombre:");
        TextField nameField = new TextField();

        Label stateLabel = new Label("Estado:");
        ComboBox<String> stateComboBox = new ComboBox<>();
        stateComboBox.getItems().addAll("Finalizado", "Cancelado", "En hiatus", "En progreso");
        
        Label capitulosLabel = new Label("Capítulos:");
        TextField capitulosField = new TextField();
        capitulosField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        }));
        
        Label autoresLabel = new Label("Autor:");
        ComboBox<String> autoresComboBox = new ComboBox<>();
        autoresComboBox.getItems().addAll(getNombresAutores());
        
        Label mangasLabel = new Label("Manga:");
        ComboBox<String> mangasComboBox = new ComboBox<>();
        mangasComboBox.getItems().addAll(getNombresMangas());
        
        Button saveButton = new Button("Guardar");
        saveButton.setOnAction(e -> {
            String nombre = nameField.getText();
            String estado = stateComboBox.getValue();
            int capitulos = Integer.parseInt(capitulosField.getText());
            String autorSeleccionado = autoresComboBox.getValue();
            String mangaSeleccionado = mangasComboBox.getValue();

            if (nombre.isEmpty() || estado == null || autorSeleccionado == null) {
                System.out.println("Por favor, complete todos los campos.");
                return;
            }

            try {
                Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
                
                // Obtener el ID del autor seleccionado
                int idAutor = getIdAutor(autorSeleccionado);
                int idManga = getIdManga(mangaSeleccionado);
                
                String query = "INSERT INTO Animes (nombre, autor_id, manga_id, estado, num_capitulos ) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = conexion.prepareStatement(query);
                statement.setString(1, nombre);
                statement.setInt(2, idAutor);
                statement.setInt(3, idManga);
                statement.setString(4, estado);
                statement.setInt(5, capitulos);
                
                statement.executeUpdate();

                System.out.println("Anime agregado con éxito.");

                conexion.close();
                animesWindow.actualizarTablaAnimes();
                primaryStage.close(); // Cerrar la ventana al guardar el manga
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("Error al agregar el anime.");
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.addRow(0, nameLabel, nameField);
        gridPane.addRow(1, stateLabel, stateComboBox);
        gridPane.addRow(2, capitulosLabel, capitulosField);
        gridPane.addRow(3, autoresLabel, autoresComboBox);
        gridPane.addRow(4, mangasLabel, mangasComboBox);

        HBox buttonBox = new HBox(saveButton);
        buttonBox.setSpacing(10);
        buttonBox.setStyle("-fx-alignment: CENTER;");

        root.getChildren().addAll(gridPane, buttonBox);

        primaryStage.setTitle("Añadir Anime");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    // Método para obtener los nombres de los autores desde la base de datos
    private ObservableList<String> getNombresAutores() {
        ObservableList<String> nombresAutores = FXCollections.observableArrayList();
        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            String query = "SELECT nombre FROM Autores";
            PreparedStatement statement = conexion.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                nombresAutores.add(resultSet.getString("nombre"));
            }
            conexion.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al obtener los nombres de los autores.");
        }
        return nombresAutores;
    }
    private ObservableList<String> getNombresMangas() {
        ObservableList<String> nombresMangas = FXCollections.observableArrayList();
        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            String query = "SELECT nombre FROM Manga";
            PreparedStatement statement = conexion.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                nombresMangas.add(resultSet.getString("nombre"));
            }
            conexion.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al obtener los nombres de los autores.");
        }
        return nombresMangas;
    }
    
    // Método para obtener el ID de un autor a partir de su nombre
    private int getIdAutor(String nombreAutor) {
        int idAutor = -1;
        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            String query = "SELECT id FROM Autores WHERE nombre = ?";
            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setString(1, nombreAutor);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                idAutor = resultSet.getInt("id");
            }
            conexion.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al obtener el ID del autor.");
        }
        return idAutor;
    }
    
    private int getIdManga(String nombreManga) {
        int idManga = -1;
        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            String query = "SELECT id FROM Manga WHERE nombre = ?";
            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setString(1, nombreManga);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                idManga = resultSet.getInt("id");
            }
            conexion.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al obtener el ID del manga.");
        }
        return idManga;
    }
    
    /**
     * Main de la aplicación, básicamente inicia el resto del código al abrir la aplicación y poco más.
     * @param args Aquí se encontrarían los argumento que especificarias en la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
