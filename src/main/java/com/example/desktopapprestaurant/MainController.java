package com.example.desktopapprestaurant;

import com.example.desktopapprestaurant.Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane panelContenidos;

    @FXML
    private MenuItem menuItemConsultarReservas;

    @FXML
    private MenuItem menuItemAñadirReserva;

    @FXML
    private MenuItem menuItemCerrarSesion;

    @FXML
    private MenuItem menuItemConsultarCarta;

    @FXML
    private MenuItem menuItemAñadirArticulo;


    @FXML
    public void initialize() {
        cargarVista("listaReservas.fxml");
        menuItemConsultarReservas.setOnAction(event -> cargarVista("listaReservas.fxml"));
        menuItemAñadirReserva.setOnAction(event -> mostrarModal("añadirReserva.fxml"));
        menuItemConsultarCarta.setOnAction(event -> cargarVista("listaCarta.fxml"));
        menuItemAñadirArticulo.setOnAction(event -> mostrarModal("añadirArticulo.fxml"));

        menuItemCerrarSesion.setOnAction(event -> cerrarSesion());
    }

    private void cerrarSesion() {
        System.exit(0);
    }

    // Método para cargar la vista desde un archivo FXML
    public void cargarVista(String fxmlFile) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node contenido = loader.load();

            // Limpiar el panel y agregar el nuevo contenido
            panelContenidos.getChildren().clear();
            panelContenidos.getChildren().add(contenido);

            // Ajustar el contenido cargado al tamaño del panel
            AnchorPane.setTopAnchor(contenido, 0.0);
            AnchorPane.setBottomAnchor(contenido, 0.0);
            AnchorPane.setLeftAnchor(contenido, 0.0);
            AnchorPane.setRightAnchor(contenido, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para mostrar un modal
    public void mostrarModal(String fxmlFile) {
        try {
            // Cargar el archivo FXML para el modal
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node contenido = loader.load();

            // Crear un nuevo escenario (Stage) para el modal
            Stage modalStage = new Stage();
            modalStage.setTitle("Añadir Reserva");
            modalStage.initModality(Modality.APPLICATION_MODAL); // Hacer el modal modal
            modalStage.setResizable(false); // Deshabilitar redimensionamiento

            // Crear un nuevo AnchorPane para el contenido del modal
            AnchorPane modalPane = new AnchorPane();
            modalPane.getChildren().add(contenido);

            // Ajustar el contenido cargado al tamaño del panel
            AnchorPane.setTopAnchor(contenido, 0.0);
            AnchorPane.setBottomAnchor(contenido, 0.0);
            AnchorPane.setLeftAnchor(contenido, 0.0);
            AnchorPane.setRightAnchor(contenido, 0.0);

            // Configurar la escena y mostrar el modal
            modalStage.setScene(new javafx.scene.Scene(modalPane));
            modalStage.showAndWait(); // Mostrar el modal y esperar a que se cierre
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
