package com.example.desktopapprestaurant.Model;
public class Reserva {
    private int idReserva;
    private String nombreReserva;
    private String correoReserva;
    private String fechaReserva;
    private String horaReserva;
    private int personasReserva;
    private String fechaCreacionReserva;
    private String detallesReserva;
    private Mesa mesa;

    public Reserva() {
    }
    public Reserva(int idReserva, String nombreReserva, String correoReserva, String fechaReserva, String horaReserva,
                   int personasReserva, String fechaCreacionReserva, String detallesReserva, Mesa mesa) {
        this.idReserva = idReserva;
        this.nombreReserva = nombreReserva;
        this.correoReserva = correoReserva;
        this.fechaReserva = fechaReserva;
        this.horaReserva = horaReserva;
        this.personasReserva = personasReserva;
        this.fechaCreacionReserva = fechaCreacionReserva;
        this.detallesReserva = detallesReserva;
        this.mesa = mesa;
    }

    public Reserva( String nombreReserva, String correoReserva, String fechaReserva, String horaReserva,
                   int personasReserva, String fechaCreacionReserva, String detallesReserva, Mesa mesa) {
        this.nombreReserva = nombreReserva;
        this.correoReserva = correoReserva;
        this.fechaReserva = fechaReserva;
        this.horaReserva = horaReserva;
        this.personasReserva = personasReserva;
        this.fechaCreacionReserva = fechaCreacionReserva;
        this.detallesReserva = detallesReserva;
        this.mesa = mesa;
    }

    // Getters y setters para los campos si son necesarios


    public String getNombreReserva() {
        return nombreReserva;
    }

    public void setNombreReserva(String nombreReserva) {
        this.nombreReserva = nombreReserva;
    }

    public String getCorreoReserva() {
        return correoReserva;
    }

    public void setCorreoReserva(String correoReserva) {
        this.correoReserva = correoReserva;
    }

    public String getFechaReserva() {
        String[] fecha = fechaReserva.split("-");
        return fecha[2] + "/" + fecha[1] + "/" + fecha[0];
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getHoraReserva() {
        return horaReserva.substring(0,5);
    }

    public void setHoraReserva(String horaReserva) {
        this.horaReserva = horaReserva;
    }

    public int getPersonasReserva() {
        return personasReserva;
    }

    public void setPersonasReserva(int personasReserva) {
        this.personasReserva = personasReserva;
    }

    public String getFechaCreacionReserva() {
        return fechaCreacionReserva;
    }

    public void setFechaCreacionReserva(String fechaCreacionReserva) {
        this.fechaCreacionReserva = fechaCreacionReserva;
    }

    public String getDetallesReserva() {
        return detallesReserva;
    }

    public void setDetallesReserva(String detallesReserva) {
        this.detallesReserva = detallesReserva;
    }

    public String getMesa() {
        return "Mesa: " + mesa.getIdMesa()+ " Capacidad: " + mesa.getCapacidad();
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", nombreReserva='" + nombreReserva + '\'' +
                ", correoReserva='" + correoReserva + '\'' +
                ", fechaReserva='" + fechaReserva + '\'' +
                ", horaReserva='" + horaReserva + '\'' +
                ", personasReserva=" + personasReserva +
                ", fechaCreacionReserva='" + fechaCreacionReserva + '\'' +
                ", detallesReserva='" + detallesReserva + '\'' +
                ", mesa=" + mesa +
                '}';
    }
}
