/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;

/**
 *
 * @author Party
 */
public class AutoresWindow extends Application {

    private TableView<String[]> tableView;
    private static final String URL = "jdbc:mysql://localhost:3306/proyectofinal";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    /**
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        tableView = new TableView<>();
        TableColumn<String[], String> idColumn = new TableColumn<>("ID");
        TableColumn<String[], String> nombreColumn = new TableColumn<>("Nombre");
        TableColumn<String[], String> estadoColumn = new TableColumn<>("Estado");
        TableColumn<String[], Void> accionesColumn = new TableColumn<>("Acciones");

        // Configuración del CellValueFactory para cada columna
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        nombreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        estadoColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));

        // Configuración de las propiedades de las columnas para que ocupen el 100% del ancho disponible
        idColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        nombreColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        estadoColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        accionesColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));

        // Crear botón en la columna de acciones
        accionesColumn.setCellFactory(param -> new TableCell<String[], Void>() {
            private final Button botonEliminar = new Button("Eliminar");

            {
                botonEliminar.setOnAction(event -> {
                    String[] fila = getTableView().getItems().get(getIndex());
                    String idAutor = fila[0]; // Obtener el ID del autor
                    AutoresWindowEliminar ventanaEliminar = new AutoresWindowEliminar(idAutor, AutoresWindow.this);
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
        tableView.getColumns().addAll(idColumn, nombreColumn, estadoColumn, accionesColumn);

        VBox root = new VBox(tableView);

        // Crear botones
        Button boton1 = new Button("Volver");
        Button boton2 = new Button("Añadir Autor");

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
            AutoresWindowCreate autoresWindowCreate = new AutoresWindowCreate(this); // Pasar una referencia de AutoresWindow
            try {
                autoresWindowCreate.start(new Stage()); // Mostrar la ventana AutoresWindowCreate
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
            ResultSet resultado = consulta.executeQuery("SELECT * FROM Autores");

            while (resultado.next()) {
                String[] row = {
                        Integer.toString(resultado.getInt("id")),
                        resultado.getString("nombre"),
                        resultado.getString("estado")
                };

                tableView.getItems().add(row);
            }

            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Ventana de Autores");
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

    /**
     *
     */
    public void actualizarTablaAutores() {
        tableView.getItems().clear(); // Limpiar los elementos actuales en la tabla
        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            Statement consulta = conexion.createStatement();
            ResultSet resultado = consulta.executeQuery("SELECT * FROM Autores");

            while (resultado.next()) {
                String[] row = {
                        Integer.toString(resultado.getInt("id")),
                        resultado.getString("nombre"),
                        resultado.getString("estado")
                };

                tableView.getItems().add(row); // Agregar nueva fila a la tabla
            }

            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}