/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Ventana para crear un nuevo manga en el sistema.
 * Permite al usuario introducir información sobre el nuevo manga, como nombre, estado, número de capítulos y autor.
 * También realiza validaciones y operaciones en la base de datos para guardar el nuevo manga.
 * 
 * @author Party
 */
public class MangasWindowCreate extends Application {

    private MangasWindow mangasWindow;

    /**
     * Constructor de la clase MangasWindowCreate.
     * 
     * @param mangasWindow La ventana principal de animes, utilizada para actualizar 
     * la tabla de animes después de añadir uno nuevo.
     */
    public MangasWindowCreate(MangasWindow mangasWindow) {
        this.mangasWindow = mangasWindow;
    }

    private static final String URL = "jdbc:mysql://localhost:3306/proyectofinal";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    /**
     * Método principal para iniciar la ventana de creación de mangas.
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

        Button saveButton = new Button("Guardar");
        saveButton.setOnAction(e -> {
            String nombre = nameField.getText();
            String estado = stateComboBox.getValue();
            String capitulosText = capitulosField.getText();
            String autorSeleccionado = autoresComboBox.getValue();

            if (nombre.isEmpty() || estado == null || autorSeleccionado == null || capitulosText.isEmpty()) {
                mostrarAlertaError("Error de validación", "Por favor, complete todos los campos.");
                return;
            }

            int capitulos = Integer.parseInt(capitulosText);

            try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
                // Obtener el ID del autor seleccionado
                int idAutor = getIdAutor(autorSeleccionado);

                String query = "INSERT INTO Manga (nombre, autor_id, estado, num_capitulos ) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = conexion.prepareStatement(query)) {
                    statement.setString(1, nombre);
                    statement.setInt(2, idAutor);
                    statement.setString(3, estado);
                    statement.setInt(4, capitulos);

                    statement.executeUpdate();
                    mostrarAlertaInfo("Éxito", "Manga agregado con éxito.");
                }

                mangasWindow.actualizarTablaMangas();
                primaryStage.close(); // Cerrar la ventana al guardar el manga
            } catch (SQLException ex) {
                mostrarAlertaError("Error al agregar el manga", ex.getMessage());
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.addRow(0, nameLabel, nameField);
        gridPane.addRow(1, stateLabel, stateComboBox);
        gridPane.addRow(2, capitulosLabel, capitulosField);
        gridPane.addRow(3, autoresLabel, autoresComboBox);

        HBox buttonBox = new HBox(saveButton);
        buttonBox.setSpacing(10);
        buttonBox.setStyle("-fx-alignment: CENTER;");

        root.getChildren().addAll(gridPane, buttonBox);

        primaryStage.setTitle("Añadir Manga");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para obtener los nombres de los autores desde la base de datos
    private ObservableList<String> getNombresAutores() {
        ObservableList<String> nombresAutores = FXCollections.observableArrayList();
        String query = "SELECT nombre FROM Autores";
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             PreparedStatement statement = conexion.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                nombresAutores.add(resultSet.getString("nombre"));
            }
        } catch (SQLException ex) {
            mostrarAlertaError("Error al obtener los nombres de los autores", ex.getMessage());
        }
        return nombresAutores;
    }

    // Método para obtener el ID de un autor a partir de su nombre
    private int getIdAutor(String nombreAutor) {
        int idAutor = -1;
        String query = "SELECT id FROM Autores WHERE nombre = ?";
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, nombreAutor);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    idAutor = resultSet.getInt("id");
                }
            }
        } catch (SQLException ex) {
            mostrarAlertaError("Error al obtener el ID del autor", ex.getMessage());
        }
        return idAutor;
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

    /**
     * Main de la aplicación, básicamente inicia el resto del código al abrir la aplicación y poco más.
     * 
     * @param args Aquí se encontrarían los argumento que especificarias en la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}