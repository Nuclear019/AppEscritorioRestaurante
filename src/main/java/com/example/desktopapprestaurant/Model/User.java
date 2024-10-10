package com.example.desktopapprestaurant.Model;

public class User {
    private Long idUsuario;
    private String username;
    private String password;
    private String rolUsuario;
    private String correoUsuario;
    private String telefonoUsuario;

    public User() {
    }

    public User(Long idUsuario, String user) {
        this.idUsuario = idUsuario;
        this.username = user;
    }

    public User(Long idUsuario, String user, String password, String rolUsuario, String correoUsuario, String telefonoUsuario) {
        this.idUsuario = idUsuario;
        this.username = user;
        this.password = password;
        this.rolUsuario = rolUsuario;
        this.correoUsuario = correoUsuario;
        this.telefonoUsuario = telefonoUsuario;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    public void setTelefonoUsuario(String telefonoUsuario) {
        this.telefonoUsuario = telefonoUsuario;
    }
}
