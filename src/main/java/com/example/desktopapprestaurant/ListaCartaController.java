package com.example.desktopapprestaurant;

import com.example.desktopapprestaurant.Model.Plato;
import com.example.desktopapprestaurant.Model.Reserva;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class ListaCartaController {

    @FXML
    private Pagination pagination;
    @FXML
    private GridPane gridPane;
    @FXML
    private ComboBox<String> comboBoxCategoria;

    private ContextMenu contextMenu;
    private MenuItem menuEditar;

    private MenuItem menuEliminar ;


    private static final int ITEMS_PER_PAGE = 16; // 4 filas x 4 columnas
    private List<Plato> articulos = new ArrayList<>(); // Inicialización de la lista
    private int categoriaSeleccionada = 0; // Categoría por defecto

    @FXML
    public void initialize() {
        contextMenu = new ContextMenu();
        menuEditar = new MenuItem("Editar");
        menuEliminar = new MenuItem("Eliminar");

        contextMenu.getItems().addAll(menuEditar, menuEliminar);


        // Cargar categorías en el ComboBox
        comboBoxCategoria.getItems().addAll("Entrantes", "Principales", "Postres", "Bebidas");
        comboBoxCategoria.getSelectionModel().selectFirst(); // Selecciona la primera categoría
        handleCategoriaSeleccionada(); // Cargar artículos de la categoría seleccionada
        comboBoxCategoria.setOnAction(event -> handleCategoriaSeleccionada()); // Añadir acción al ComboBox
    }

    @FXML
    private void handleCategoriaSeleccionada() {
        // Actualizar la categoría seleccionada
        categoriaSeleccionada = comboBoxCategoria.getSelectionModel().getSelectedIndex();
        articulos = cargarArticulos(categoriaSeleccionada); // Cargar artículos según la categoría

        if (articulos.isEmpty()) {
            pagination.setPageCount(1); // Establece al menos una página
            pagination.setCurrentPageIndex(0);
            pagination.setPageFactory(pageIndex -> new Label("No hay artículos disponibles.")); // Mensaje de no hay artículos
        } else {
            // Actualizar la paginación
            pagination.setPageCount((int) Math.ceil((double) articulos.size() / ITEMS_PER_PAGE));
            pagination.setCurrentPageIndex(0); // Reiniciar a la primera página
            pagination.setPageFactory(this::createPage); // Actualizar la paginación

        }
    }
    private Node createPage(int pageIndex) {
        if (articulos == null || articulos.isEmpty()) {
            return new Label("No hay artículos disponibles.");
        }

        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, articulos.size());

        if (fromIndex >= articulos.size()) {
            return new Label("No hay más artículos para mostrar.");
        }

        List<Plato> platosEnPagina = articulos.subList(fromIndex, toIndex);

        VBox vBox = new VBox();
        GridPane grid = new GridPane();
        grid.setHgap(10); // Espacio horizontal entre elementos
        grid.setVgap(10); // Espacio vertical entre elementos
        grid.setAlignment(Pos.CENTER); // Alinear el GridPane al centro

        int column = 0;
        int row = 0;

        for (Plato plato : platosEnPagina) {
            if (plato == null) {
                continue; // Ignora si plato es null
            }

            // Crear AnchorPane para cada artículo, incluyendo la imagen y las etiquetas
            VBox itemBox = createItemBox(plato);
            itemBox.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(itemBox, event.getScreenX(), event.getScreenY());
                    menuEditar.setOnAction(e -> {
                        abrirModalEdicion(plato);
                    });
                    menuEliminar.setOnAction(e -> {
                        eliminarArticulo(plato);
                    });
                }
            });

            // Limitar el tamaño de cada itemBox a un tamaño fijo de celda
            itemBox.setPrefSize(200, 200);
            itemBox.setMaxSize(200, 200);

            // Agregar al GridPane
            grid.add(itemBox, column, row);

            // Restringir el crecimiento de las celdas
            GridPane.setHgrow(itemBox, Priority.NEVER);
            GridPane.setVgrow(itemBox, Priority.NEVER);

            column++;
            if (column == 4) { // 4 columnas
                column = 0;
                row++;
            }
        }

        // Ajustar el tamaño mínimo del GridPane
        grid.setMinSize(800, 600); // Tamaño mínimo del GridPane

        // Añadir el GridPane al VBox
        vBox.getChildren().add(grid);

        // Establecer el tamaño mínimo del VBox
        vBox.setMinSize(200, 200); // Tamaño mínimo del VBox
        vBox.setAlignment(Pos.CENTER); // Alinear el VBox al centro

        return vBox;
    }


    private VBox createItemBox(Plato plato) {
        String imagenBase64 = plato.getImagenPlato();
        Image image;

        if (imagenBase64 != null) {
            // Decodificar el String Base64
            byte[] imageBytes = Base64.getDecoder().decode(imagenBase64);

            // Crear un InputStream a partir de los bytes decodificados
            InputStream inputStream = new ByteArrayInputStream(imageBytes);

            // Crear la imagen
            image = new Image(inputStream);
        } else {
            // Usar la imagen por defecto
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/Logo.png")));
        }

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true); // Mantener la relación de aspecto

        // Crear etiquetas
        Label nombreLabel = new Label(plato.getNombrePlato());
        nombreLabel.setStyle("-fx-font-weight: bold;");

        Label precioLabel = new Label(String.format("$%.2f", plato.getPrecioPlato()));
        precioLabel.setStyle("-fx-text-fill: green;");

        // Crear AnchorPane para el artículo
        VBox itemBox = new VBox();
        itemBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        // Agregar elementos al AnchorPane
        itemBox.getChildren().addAll(imageView, nombreLabel, precioLabel);

        // Configurar el ajuste de los elementos dentro del AnchorPane
        itemBox.setAlignment(Pos.CENTER); // Alinear al centro

        // Vincular el tamaño del ImageView a su contenedor
        imageView.fitWidthProperty().bind(itemBox.widthProperty().multiply(0.9)); // El ImageView ocupa el 90% del ancho del AnchorPane
        imageView.fitHeightProperty().bind(itemBox.heightProperty().multiply(0.6)); // El ImageView ocupa el 60% de la altura del AnchorPane

        // Hacer que el AnchorPane crezca con el espacio disponible
        itemBox.setMinSize(100, 100); // Tamaño mínimo del AnchorPane
        itemBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Permitir que el AnchorPane crezca a su máximo tamaño

        return itemBox;
    }




    private List<Plato> cargarArticulos(int categoriaSeleccionada) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/v1/platos?idPlatoCategoria=" + (categoriaSeleccionada + 1)))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                Plato[] platos = new Gson().fromJson(responseBody, Plato[].class);
                List<Plato> listaPlatos = new ArrayList<>();
                for (Plato plato : platos) {
                    if (plato != null) {
                        listaPlatos.add(plato);
                    }
                }
                return listaPlatos;
            } else {
                System.out.println("Error en la consulta: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>(); // Retorna lista vacía en caso de error
    }


    private void abrirModalEdicion(Plato articulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/desktopapprestaurant/añadirArticulo.fxml"));
            Parent root = loader.load();

            AñadirArticuloController editarController = loader.getController();
            editarController.cargarDatos(articulo);

            Stage stage = new Stage();
            stage.setTitle("Editar Articulo");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            cargarArticulos(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarArticulo(Plato articulo) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/v1/platos/" + articulo.getIdPlato()))
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setTitle("Eliminar Articulo");
            confirmDelete.setHeaderText("¿Estás seguro de que deseas eliminar el articulo?");
            confirmDelete.setContentText("Esta acción no se puede deshacer.");
            if (confirmDelete.showAndWait().get() == ButtonType.OK) {

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    System.out.println("Articulo eliminado con éxito: " + response.body());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Articulo eliminado");
                    alert.setHeaderText("Se ha eliminado el articulo con éxito.");
                    alert.setContentText("El articulo se ha eliminado correctamente.");
                    alert.showAndWait();
                    handleCategoriaSeleccionada();
                } else {
                    System.out.println("Error al eliminar el articulo: " + response.statusCode());
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error al eliminar el articulo.");
                    alert.setContentText("Ha ocurrido un error al eliminar el articulo. Por favor, inténtelo de nuevo.");
                    alert.showAndWait();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
