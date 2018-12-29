package com.layala.gitdata.entidades;

import java.util.Date;

/**
 * Clase que representa un objeto Milestone de la API v3 de GitHub
 * 
 * @author Luis Ayala
 * @version 1.0
 * @since 1.0
 */
public class Hito {
    private Date creadoEn;
    private int incidenciasCerradas;
    private int incidenciasAbiertas;
    private String descripcion;
    private String estado;
    private String titulo;
    private String url;

    public Date getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(Date creadoEn) {
        this.creadoEn = creadoEn;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIncidenciasCerradas() {
        return incidenciasCerradas;
    }

    public void setIncidenciasCerradas(int incidenciasCerradas) {
        this.incidenciasCerradas = incidenciasCerradas;
    }

    public int getIncidenciasAbiertas() {
        return incidenciasAbiertas;
    }

    public void setIncidenciasAbiertas(int incidenciasAbiertas) {
        this.incidenciasAbiertas = incidenciasAbiertas;
    }
}
