package com.example.desktopapprestaurant.Model;

public class CategoriaPlato {
    private Long idPlatoCategoria;
    private String categoria;

    public CategoriaPlato() {
    }

    public CategoriaPlato(Long idPlatoCategoria, String categoria) {
        this.idPlatoCategoria = idPlatoCategoria;
        this.categoria = categoria;
    }

    public Long getIdPlatoCategoria() {
        return idPlatoCategoria;
    }

    public void setIdPlatoCategoria(Long idPlatoCategoria) {
        this.idPlatoCategoria = idPlatoCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "CategoriaPlato{" +
                "idPlatoCategoria=" + idPlatoCategoria +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
