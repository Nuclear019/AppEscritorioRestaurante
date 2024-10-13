package com.example.desktopapprestaurant;

import com.example.desktopapprestaurant.Model.Mesa;
import com.example.desktopapprestaurant.Model.Reserva;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.google.gson.Gson;
import javafx.stage.Stage;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.sql.Time;
import java.time.ZoneId;
import java.util.List;

public class AñadirReservaController {

    private final String añaadirReservaUrl = "http://localhost:8080/api/v1/reservas";
    private final String actualizarReservaUrl = "http://localhost:8080/api/v1/reservas";



    public TextField nombreReserva;
    public TextField correoReserva;
    public ComboBox<String> selectHora;
    public TextField personasReserva;
    public DatePicker fechaReserva;
    public Label mesasDisponiblexTxt;
    public Label labelModalActualizarReserva;
    public Button btonActualizarReserva;
    private Mesa mesaAsignada;
    private Time hora;
    private Date fecha;
    private int personas;

    private int tipoOperacion = 0; // 0: Añadir, 1: Actualizar

    private Reserva antiguaReserva;
    @FXML
    public TextArea detallesReserva;

    @FXML
    public void initialize() {
        // Listener para actualizar la selección de la hora cuando cambia la fecha
        fechaReserva.valueProperty().addListener((observable, oldValue, newValue) -> showTimeSelector());

        // Listener para permitir solo números en el campo de personas
        personasReserva.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                personasReserva.setText(newValue.replaceAll("\\D", "")); // Elimina caracteres no numéricos
            }
        });

        showTimeSelector();
    }

    public void showTimeSelector() {
        // Deshabilitar el ComboBox si no hay fecha seleccionada
        selectHora.setDisable(fechaReserva.getValue() == null);
    }

    public void setMesaAsignada() {
        if (fechaReserva.getValue() != null && selectHora.getValue() != null && !personasReserva.getText().isEmpty()) {
            this.hora = Time.valueOf(selectHora.getValue() + ":00");  // Añadir los segundos
            this.fecha = Date.valueOf(fechaReserva.getValue());
            this.personas = Integer.parseInt(personasReserva.getText());

            try {
                // Crear el cliente HttpClient
                HttpClient client = HttpClient.newHttpClient();

                // Construir la URI con los parámetros
                String uri = String.format("http://localhost:8080/api/v1/mesas/disponibles?fechaReserva=%s&horaReserva=%s&personas=%d",
                        fecha, hora.toLocalTime(), personas);

                // Crear la solicitud GET
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(uri))
                        .header("Content-Type", "application/json")
                        .GET()  // Usar GET aquí
                        .build();

                // Enviar la solicitud y obtener la respuesta
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                Type listType = new TypeToken<List<Mesa>>(){}.getType();
                List<Mesa> mesas = new Gson().fromJson(response.body(), listType);

                // Asignar el primer valor a mesaAsignada
                if (!mesas.isEmpty()) {
                    mesaAsignada = mesas.get(0); // Asigna la primera mesa
                    mesasDisponiblexTxt.setText("Mesas disponibles: " + mesas.size());
                } else {
                    mesasDisponiblexTxt.setText("No hay mesas disponibles");
                }

                System.out.println(response.body());
            } catch (Exception e) {
                System.out.println("Error al obtener las mesas disponibles: " + e.getMessage());
            }
        }
    }

    public void añadirOActualizarReserva() {
        if (nombreReserva.getText().isEmpty() || correoReserva.getText().isEmpty() || personasReserva.getText().isEmpty() || fecha == null || hora == null) {
            System.out.println("Por favor, complete todos los campos antes de continuar.");
            return;
        }

        try {
            int idReserva = antiguaReserva != null ? antiguaReserva.getIdReserva() : 0;
            // Obtener los datos de los campos de texto
            String nombre = nombreReserva.getText();
            String correo = correoReserva.getText();
            String fechaFormateada = fecha.toString();  // Convertir a cadena
            int personas = Integer.parseInt(personasReserva.getText());
            String detalles = detallesReserva.getText();
            String fechaCreacion = java.time.Instant.now().toString();

            // Crear la reserva
            Reserva nuevaReserva = new Reserva( idReserva, nombre, correo, fechaFormateada, hora.toString(), personas, fechaCreacion, detalles, mesaAsignada);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();

            // Construir la solicitud dependiendo de si es una nueva reserva (POST) o actualización (PUT)
            HttpRequest request;
            if (tipoOperacion == 0) { // Añadir nueva reserva
                request = HttpRequest.newBuilder()
                        .uri(new URI(añaadirReservaUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(nuevaReserva)))
                        .build();
            } else { // Actualizar reserva existente

                request = HttpRequest.newBuilder()
                        .uri(new URI(actualizarReservaUrl + "/" + nuevaReserva.getIdReserva()))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(nuevaReserva)))
                        .build();
            }

            // Enviar la solicitud
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Manejar la respuesta
            if (response.statusCode() == 200) {
                System.out.println("Operación realizada con éxito: " + response.body());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Nueva reserva");
                alert.setHeaderText("Se ha añadido una nueva reserva con éxito.");
                alert.setContentText("La reserva se ha añadido correctamente.");
                alert.showAndWait();
                Stage stage = (Stage) nombreReserva.getScene().getWindow();  // Obtener el Stage
                stage.close();


            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error al añadir la reserva.");
                alert.setContentText("Ha ocurrido un error al añadir la reserva. Por favor, inténtelo de nuevo.");
                alert.showAndWait();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void cargarDatos(Reserva reserva) {
        antiguaReserva = reserva;
        System.out.println(reserva.toString());
        btonActualizarReserva.setText("Actualizar");
        labelModalActualizarReserva.setText("Actualizar reserva");
        tipoOperacion = 1;
        nombreReserva.setText(reserva.getNombreReserva());
        correoReserva.setText(reserva.getCorreoReserva());
        selectHora.setValue(reserva.getHoraReserva().substring(0,5));
        personasReserva.setText(String.valueOf(reserva.getPersonasReserva()));
        detallesReserva.setText(reserva.getDetallesReserva());
    }


}
