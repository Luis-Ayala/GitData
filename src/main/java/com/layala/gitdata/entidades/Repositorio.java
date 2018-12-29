package com.layala.gitdata.entidades;

import java.util.Date;

/**
 * Clase que representa un objeto Repository de la API v3 de GitHub
 * 
 * @author Luis Ayala
 * @version 1.0
 * @since 1.0
 */
public class Repositorio {
    private boolean tieneIncidencias;
    private Date creadoEn;
    private Date modificadoEn;
    private long repositorioId;
    private int incidenciasAbiertas;
    private String descripcion;
    private String homepage;
    private String lenguaje;
    private String nombre;
    private String url;
    private String gitUrl;
    private String htmlUrl;

    public boolean isTieneIncidencias() {
        return tieneIncidencias;
    }

    public void setTieneIncidencias(boolean tieneIncidencias) {
        this.tieneIncidencias = tieneIncidencias;
    }

    public Date getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(Date creadoEn) {
        this.creadoEn = creadoEn;
    }

    public Date getModificadoEn() {
        return modificadoEn;
    }

    public void setModificadoEn(Date modificadoEn) {
        this.modificadoEn = modificadoEn;
    }

    public long getRepositorioId() {
        return repositorioId;
    }

    public void setRepositorioId(long repositorioId) {
        this.repositorioId = repositorioId;
    }

    public int getIncidenciasAbiertas() {
        return incidenciasAbiertas;
    }

    public void setIncidenciasAbiertas(int incidenciasAbiertas) {
        this.incidenciasAbiertas = incidenciasAbiertas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

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

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }
    
}
