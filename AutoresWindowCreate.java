/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;

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
import java.sql.SQLException;

/**
 * Ventana para crear un nuevo autor en el sistema.
 * Permite al usuario introducir información sobre el nuevo autor, como nombre y estado.
 * También realiza validaciones y operaciones en la base de datos para guardar el nuevo autor.
 * 
 * @author Party
 */
public class AutoresWindowCreate extends Application {

    private AutoresWindow autoresWindow;

    /**
     * Constructor de la clase AutoresWindowCreate.
     * 
     * @param autoresWindow La ventana principal de autores, utilizada para actualizar
     * la tabla de autores después de añadir uno nuevo.
     */
    public AutoresWindowCreate(AutoresWindow autoresWindow) {
        this.autoresWindow = autoresWindow;
    }

    private static final String URL = "jdbc:mysql://localhost:3306/proyectofinal";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    /**
     * Método principal para iniciar la ventana de creación de autores.
     * 
     * @param primaryStage El escenario principal de la aplicación.
     * @throws Exception Excepción en caso de error al iniciar la ventana.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(10);

        Scene scene = new Scene(root, 300, 200);

        Label nameLabel = new Label("Nombre:");
        TextField nameField = new TextField();

        Label stateLabel = new Label("Estado:");
        ComboBox<String> stateComboBox = new ComboBox<>();
        stateComboBox.getItems().addAll("Activo", "Inactivo", "Retirado", "Fallecido");

        Button saveButton = new Button("Guardar");
        saveButton.setOnAction(e -> {
            String nombre = nameField.getText();
            String estado = stateComboBox.getValue();

            if (nombre.isEmpty() || estado == null) {
                System.out.println("Por favor, complete todos los campos.");
                return;
            }

            try {
                Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
                String query = "INSERT INTO Autores (nombre, estado) VALUES (?, ?)";
                PreparedStatement statement = conexion.prepareStatement(query);
                statement.setString(1, nombre);
                statement.setString(2, estado);
                statement.executeUpdate();

                System.out.println("Autor agregado con éxito.");

                conexion.close();
                autoresWindow.actualizarTablaAutores();
                primaryStage.close(); // Cerrar la ventana al guardar el autor
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("Error al agregar el autor.");
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.addRow(0, nameLabel, nameField);
        gridPane.addRow(1, stateLabel, stateComboBox);

        HBox buttonBox = new HBox(saveButton);
        buttonBox.setSpacing(10);
        buttonBox.setStyle("-fx-alignment: CENTER;");

        root.getChildren().addAll(gridPane, buttonBox);

        primaryStage.setTitle("Añadir Autor");
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
}
