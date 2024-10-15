package com.example.desktopapprestaurant;

import com.example.desktopapprestaurant.Model.Reserva;
import com.google.gson.Gson;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ConsultarReservasController {

    public Button actualizarReservas;
    @FXML
    private TableView<Reserva> tableView;

    @FXML
    private TableColumn<Reserva, String> nombreColumn;
    @FXML
    private TableColumn<Reserva, String> correoColumn;
    @FXML
    private TableColumn<Reserva, String> fechaColumn;
    @FXML
    private TableColumn<Reserva, String> horaColumn;
    @FXML
    private TableColumn<Reserva, Integer> personasColumn;
    @FXML
    private TableColumn<Reserva, String> mesaColumn;
    @FXML
    private TableColumn<Reserva, String> detallesColumn;

    private List<Reserva> reservas;
    private ContextMenu contextMenu;

    @FXML
    public void initialize() {
        tableView.setStyle("-fx-font-size: 16px; -fx-cell-size: 40px;");

        // También puedes aplicar estilos a las columnas si es necesario
        nombreColumn.setStyle("-fx-font-size: 16px;");
        correoColumn.setStyle("-fx-font-size: 16px;");
        fechaColumn.setStyle("-fx-font-size: 16px;");
        horaColumn.setStyle("-fx-font-size: 16px;");
        personasColumn.setStyle("-fx-font-size: 16px;");
        mesaColumn.setStyle("-fx-font-size: 16px;");
        detallesColumn.setStyle("-fx-font-size: 16px;");

    actualizarReservas.setOnAction(event -> cargarReservasView());


        // Inicializar las columnas
        nombreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreReserva()));
        correoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCorreoReserva()));
        fechaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaReserva()));
        horaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHoraReserva()));
        personasColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPersonasReserva()).asObject());
        mesaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMesa()));
        detallesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDetallesReserva()));

        // Habilitar tooltips para las celdas
        enableCellTooltips();

        cargarReservasView();
        crearContextMenu();
    }

    private void cargarReservasView() {
        reservas = getReservas();
        if (reservas != null) {
            tableView.setItems(FXCollections.observableList(reservas));
        }
    }

    public List<Reserva> getReservas() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/v1/reservas"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                System.out.println("Consulta exitosa. Respuesta: " + responseBody);

                Reserva[] reservas = new Gson().fromJson(response.body(), Reserva[].class);
                return List.of(reservas);
            } else {
                System.out.println("Error en la consulta: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void enableCellTooltips() {
        // Añadir tooltip a cada columna de la tabla
        addTooltipToColumnCells(nombreColumn);
        addTooltipToColumnCells(correoColumn);
        addTooltipToColumnCells(fechaColumn);
        addTooltipToColumnCells(horaColumn);
        addTooltipToColumnCells(personasColumn);
        addTooltipToColumnCells(mesaColumn);
        addTooltipToColumnCells(detallesColumn);
    }

    private <T> void addTooltipToColumnCells(TableColumn<Reserva, T> column) {
        column.setCellFactory(col -> {
            TableCell<Reserva, T> cell = new TableCell<>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setTooltip(null);
                    } else {
                        setText(item.toString());
                        Tooltip tooltip = new Tooltip(item.toString());
                        setTooltip(tooltip);
                    }
                }
            };
            return cell;
        });
    }

    private void crearContextMenu() {
        contextMenu = new ContextMenu();
        MenuItem editMenuItem = new MenuItem("Editar");
        MenuItem deleteMenuItem = new MenuItem("Eliminar");

        contextMenu.getItems().addAll(editMenuItem, deleteMenuItem);

        tableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                Reserva selectedReserva = tableView.getSelectionModel().getSelectedItem();
                if (selectedReserva != null) {
                    contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
                }
            }
        });

        editMenuItem.setOnAction(event -> {
            Reserva selectedReserva = tableView.getSelectionModel().getSelectedItem();
            if (selectedReserva != null) {
                editReserva(selectedReserva);
            }
        });

        deleteMenuItem.setOnAction(event -> {
            Reserva selectedReserva = tableView.getSelectionModel().getSelectedItem();
            if (selectedReserva != null) {
                deleteReserva(selectedReserva);
            }
        });
    }

    private void editReserva(Reserva reserva) {
        if (reserva != null) {
            abrirModalEdicion(reserva);
        } else {
            mostrarMensaje("Por favor, selecciona una reserva para editar.");
        }
    }

    private void deleteReserva(Reserva reserva) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/v1/reservas/" + reserva.getIdReserva()))
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            Alert confirmDeleteReserva = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDeleteReserva.setTitle("Eliminar Reserva");
            confirmDeleteReserva.setHeaderText("¿Estás seguro de que deseas eliminar la reserva?");
            confirmDeleteReserva.setContentText("Esta acción no se puede deshacer.");
            confirmDeleteReserva.showAndWait();
            if (confirmDeleteReserva.getResult() != ButtonType.OK) {
                return;
            }

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                mostrarMensaje("Reserva eliminada con éxito.");
            } else {
                System.out.println("Error al eliminar la reserva: " + response.statusCode());
                mostrarMensaje("Error al eliminar la reserva.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensaje("Error al eliminar la reserva.");
        }
        reservas= getReservas();
    }

    private void abrirModalEdicion(Reserva reserva) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/desktopapprestaurant/añadirReserva.fxml"));
            Parent root = loader.load();

            AñadirReservaController editarController = loader.getController();
            editarController.cargarDatos(reserva);

            Stage stage = new Stage();
            stage.setTitle("Editar Reserva");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tableView.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();

            cargarReservasView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
