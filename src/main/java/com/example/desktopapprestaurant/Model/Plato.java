package com.example.desktopapprestaurant.Model;

public class Plato {
    private Long idPlato;
    private String nombrePlato;
    private String detallesPlato;
    private Double precioPlato;

    private String imagenPlato;

    private boolean masVendido;

    private CategoriaPlato idPlatoCategoria;

    public Plato() {
    }

    public Plato(Long idPlato, String nombrePlato, String detallesPlato, Double precioPlato, String imagenPlato, boolean masVendido, CategoriaPlato platoCategoria) {
        this.idPlato = idPlato;
        this.nombrePlato = nombrePlato;
        this.detallesPlato = detallesPlato;
        this.precioPlato = precioPlato;
        this.imagenPlato = imagenPlato;
        this.masVendido = masVendido;
        this.idPlatoCategoria = platoCategoria;
    }

    public Long getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(Long idPlato) {
        this.idPlato = idPlato;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getDetallesPlato() {
        return detallesPlato;
    }

    public void setDetallesPlato(String detallesPlato) {
        this.detallesPlato = detallesPlato;
    }

    public Double getPrecioPlato() {
        return precioPlato;
    }

    public void setPrecioPlato(Double precioPlato) {
        this.precioPlato = precioPlato;
    }

    public String getImagenPlato() {
        return imagenPlato;
    }

    public void setImagenPlato(String imagenPlato) {
        this.imagenPlato = imagenPlato;
    }

    public boolean isMasVendido() {
        return masVendido;
    }

    public void setMasVendido(boolean masVendido) {
        this.masVendido = masVendido;
    }

    public CategoriaPlato getPlatoCategoria() {
        return idPlatoCategoria;
    }

    public void setPlatoCategoria(CategoriaPlato platoCategoria) {
        this.idPlatoCategoria = platoCategoria;
    }

    @Override
    public String toString() {
        return "Plato{" +
                "idPlato=" + idPlato +
                ", nombrePlato='" + nombrePlato + '\'' +
                ", detallesPlato='" + detallesPlato + '\'' +
                ", precioPlato=" + precioPlato +
                ", imagenPlato='" + imagenPlato + '\'' +
                ", masVendido=" + masVendido +
                ", idPlatoCategoria =" + idPlatoCategoria +
                '}';
    }
}
