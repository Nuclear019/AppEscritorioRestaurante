package com.example.desktopapprestaurant;

import com.example.desktopapprestaurant.Model.CategoriaPlato;
import com.example.desktopapprestaurant.Model.Plato;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.Base64;

public class AñadirArticuloController {


    public TextField precioPlato;
    public TextField nombrePlato;
    public TextArea detallesPlato;
    public ImageView imagenPlato;
    public Button btonGuardarPlato;
    public Label labelModalActualizarReserva;
    public ComboBox<CategoriaPlato> categoriaPlato;

    private Plato nuevoPlato;

    public void initialize() {
        nuevoPlato = new Plato();
        // Añadir las categorías al ComboBox
        categoriaPlato.getItems().addAll(
                new CategoriaPlato(1L, "Entrante"),
                new CategoriaPlato(2L, "Principal"),
                new CategoriaPlato(3L, "Postre"),
                new CategoriaPlato(4L, "Bebida")
        );

        // Establecer un convertidor para mostrar solo el nombre en el ComboBox
        categoriaPlato.setConverter(new StringConverter<CategoriaPlato>() {
            @Override
            public String toString(CategoriaPlato categoria) {
                return categoria != null ? categoria.getCategoria() : ""; // Muestra el nombre de la categoría
            }

            @Override
            public CategoriaPlato fromString(String string) {
                // No se necesita implementar este método para el uso actual
                return null;
            }
        });

        // Establecer la fábrica de celdas para que también se muestren correctamente en el menú desplegable
        categoriaPlato.setCellFactory(lv -> new ListCell<CategoriaPlato>() {
            @Override
            protected void updateItem(CategoriaPlato categoria, boolean empty) {
                super.updateItem(categoria, empty);
                setText(empty ? null : categoria.getCategoria()); // Mostrar solo el nombre de la categoría
            }
        });
    }
    public Long obtenerIdCategoriaSeleccionada() {
        CategoriaPlato categoriaSeleccionada = categoriaPlato.getSelectionModel().getSelectedItem();
        return categoriaSeleccionada != null ? categoriaSeleccionada.getIdPlatoCategoria() : null;
    }







    public void seleccionarFoto(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg")
        );
        fileChooser.setTitle("Seleccionar imagen");

        // Abrir el cuadro de diálogo para seleccionar el archivo
        File file = fileChooser.showOpenDialog(null);

        // Verificar si se ha seleccionado un archivo
        if (file != null) {
            try {
                // Leer el archivo como bytes
                byte[] fileContent = Files.readAllBytes(file.toPath());

                // Convertir los bytes a Base64
                String base64Image = Base64.getEncoder().encodeToString(fileContent);

                // Establecer la imagen en el ImageView
                Image image = new Image(file.toURI().toString());
                imagenPlato.setImage(image);

                // Guardar la imagen codificada en el objeto Plato
                nuevoPlato.setImagenPlato(base64Image);

            } catch (IOException e) {
                e.printStackTrace();
                // Manejar el error en caso de que la lectura del archivo falle
            }
        }
    }


    public void añadirOActualizarPlato(ActionEvent actionEvent) {
        nuevoPlato.setIdPlato(0L);
        nuevoPlato.setNombrePlato(nombrePlato.getText());
        nuevoPlato.setPrecioPlato(Double.parseDouble(precioPlato.getText()));
        nuevoPlato.setDetallesPlato(detallesPlato.getText());

        // Obtener la categoría seleccionada directamente
        Long idCategoriaSeleccionada = obtenerIdCategoriaSeleccionada();
        nuevoPlato.setPlatoCategoria(new CategoriaPlato(idCategoriaSeleccionada, categoriaPlato.getSelectionModel().getSelectedItem().getCategoria()));


        // Depuración

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/v1/add-plato"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(nuevoPlato)))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Operación realizada con éxito: " + response.body());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Nuevo plato");
                alert.setHeaderText("Se ha añadido un nuevo plato con éxito.");
                alert.setContentText("El plato se ha añadido correctamente.");
                alert.showAndWait();
                // Cerrar la ventana
                Stage stage = (Stage) nombrePlato.getScene().getWindow();
                stage.close();


            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error al añadir el plato.");
                alert.setContentText("Ha ocurrido un error al añadir el plato. Por favor, inténtelo de nuevo.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
