package com.example.desktopapprestaurant;

import com.example.desktopapprestaurant.MainController;
import com.example.desktopapprestaurant.Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private User userLogeado;


    public void onLoginButtonClick() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();


        if (login(username, password)) {
            // Cambiar a la nueva ventana
            try {
                // Cargar la nueva ventana FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/desktopapprestaurant/MainWindow.fxml"));
                Parent root = loader.load();

                // Obtener el controlador de la nueva ventana
                MainController mainWindowController = loader.getController();
                // Pasar el nombre de usuario al nuevo controlador si es necesario

                // Obtener la instancia del Stage actual y cambiar a la nueva escena
                Stage stage = (Stage) txtUsername.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setResizable(true);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Error al cargar la ventana principal.");
                alert.show();
            }
        }
    }

    public boolean login(String username, String password) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/v1/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                System.out.println("Login exitoso. Token: " + responseBody);
                userLogeado = new User(); // Crea tu objeto User aquí según sea necesario
                return true; // Devuelve true si el login es exitoso
            } else {
                // Si el inicio de sesión falla, puedes mostrar un mensaje de error aquí
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error no login");
                alert.setContentText("Usuario o contraseña incorrectos");
                alert.show();
                return false; // Devuelve false si el login falla
            }
        } catch (Exception e) {
            // Si el inicio de sesión falla, puedes mostrar un mensaje de error aquí
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error en el servidor");
            alert.setContentText("Ha ocurrido un error en el servidor. Por favor, inténtelo de nuevo más tarde.");
            alert.show();
            return false; // También devuelve false en caso de excepción
        }
    }




}
