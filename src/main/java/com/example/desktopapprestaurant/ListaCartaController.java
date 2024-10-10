package com.example.desktopapprestaurant;

import com.example.desktopapprestaurant.Model.Plato;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListaCartaController {

    @FXML
    private Pagination pagination;
    @FXML
    private GridPane gridPane;
    @FXML
    private ComboBox<String> comboBoxCategoria;

    private static final int ITEMS_PER_PAGE = 16; // 4 filas x 4 columnas
    private List<Plato> articulos = new ArrayList<>(); // Inicialización de la lista
    private int categoriaSeleccionada = 0; // Categoría por defecto

    @FXML
    public void initialize() {
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

        AnchorPane anchorPane = new AnchorPane();
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
            AnchorPane itemBox = createItemBox(plato);

            // Limitar el tamaño de cada itemBox a un tamaño fijo de celda
            itemBox.setPrefSize(200, 200); // Tamaño de cada celda, ajustable según lo necesites
            itemBox.setMaxSize(200, 200); // Evita que el itemBox crezca más allá de su celda

            // Agregar al GridPane
            grid.add(itemBox, column, row);

            // Restringir el crecimiento de las celdas
            GridPane.setHgrow(itemBox, Priority.NEVER); // No permitir que el itemBox crezca horizontalmente
            GridPane.setVgrow(itemBox, Priority.NEVER); // No permitir que el itemBox crezca verticalmente

            column++;
            if (column == 4) { // 4 columnas
                column = 0;
                row++;
            }
        }

        // Ajustar el tamaño total del GridPane
        grid.setPrefSize(800, 600); // Ajusta el tamaño del GridPane según la ventana

        // Añadir el GridPane al AnchorPane
        anchorPane.getChildren().add(grid);
        AnchorPane.setTopAnchor(grid, 0.0); // Alinear al tope
        AnchorPane.setLeftAnchor(grid, 0.0); // Alinear a la izquierda

        return anchorPane;
    }


    private AnchorPane createItemBox(Plato plato) {
        Image image;
        if (plato.getImagenPlato() != null) {
            image = new Image(plato.getImagenPlato());
        } else {
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
        AnchorPane itemBox = new AnchorPane();
        itemBox.setPrefSize(250, 250); // Establecer tamaño preferido de 250x250

        // Agregar elementos al AnchorPane
        itemBox.getChildren().addAll(imageView, nombreLabel, precioLabel);

        // Configurar el ajuste de los elementos dentro del AnchorPane
        AnchorPane.setTopAnchor(imageView, 0.0);  // Alinear al tope
        AnchorPane.setLeftAnchor(imageView, 0.0); // Alinear a la izquierda
        AnchorPane.setRightAnchor(imageView, 0.0); // Ocupar el espacio horizontal
        AnchorPane.setBottomAnchor(nombreLabel, 30.0); // Espacio desde el fondo para el nombre
        AnchorPane.setLeftAnchor(nombreLabel, 0.0); // Alinear a la izquierda

        AnchorPane.setBottomAnchor(precioLabel, 10.0); // Espacio desde el fondo para el precio
        AnchorPane.setLeftAnchor(precioLabel, 0.0); // Alinear a la izquierda

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
                System.out.println("Consulta exitosa. Respuesta: " + responseBody);

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
}
