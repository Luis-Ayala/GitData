package com.layala.gitdata.entidades;

/**
 * Clase que representa un objeto Label de la API v3 de GitHub
 * 
 * @author Luis Ayala
 * @version 1.0
 * @since 1.0
 */
public class Etiqueta {
    private String nombre;
    private String url;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
