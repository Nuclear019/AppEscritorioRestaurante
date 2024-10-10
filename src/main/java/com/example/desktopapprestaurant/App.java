package com.example.desktopapprestaurant;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showLogin();
    }



    // Mostrar la ventana de login
    private void showLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        Stage loginStage = new Stage();
        loginStage.setTitle("Login");
        loginStage.setScene(new Scene(root));
        loginStage.setResizable(false);  // Desactivar redimensionamiento para login
        loginStage.show();

        // Aquí puedes añadir el comportamiento para cuando el login sea exitoso
        // Por ejemplo, cerrar el login y abrir la ventana principal
    }

    // Mostrar la ventana principal


    public static void main(String[] args) {
        launch(args);
    }
}
