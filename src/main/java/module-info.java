module com.example.desktopapprestaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;  // Aquí agregamos la dependencia de Gson
    requires java.sql;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires jakarta.mail;

    opens com.example.desktopapprestaurant to javafx.fxml;
    exports com.example.desktopapprestaurant;



    opens com.example.desktopapprestaurant.Model to com.google.gson;






}