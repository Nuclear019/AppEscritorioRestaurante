package com.example.desktopapprestaurant.Model;

public class Mesa {
    private long idMesa;
    private int capacidad;

    public Mesa(long idMesa, int capacidad) {
        this.idMesa = idMesa;
        this.capacidad = capacidad;
    }

    public long getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(long idMesa) {
        this.idMesa = idMesa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return "Mesa{" +
                "idMesa=" + idMesa +
                ", capacidad=" + capacidad +
                '}';
    }
}
