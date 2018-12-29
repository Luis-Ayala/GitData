package com.layala.gitdata.entidades;

import java.util.Date;

/**
 * Clase que representa un objeto PullRequest de la API v3 de GitHub
 * 
 * @author Luis Ayala
 * @version 1.0
 * @since 1.0
 */
public class PullRequest {
    private Date cerradoEn;
    private Date modificadoEn;
    private Date creadoEn;
    private long pullRequestId;
    private int archivosModificados;
    private int comentarios;
    private int commits;
    private int eliminados;
    private Hito hito;
    private String cuerpo;
    private String estado;
    private String titulo;
    private String htmlUrl;
    private String incidenciaUrl;
    private String url;

    public Date getCerradoEn() {
        return cerradoEn;
    }

    public void setCerradoEn(Date cerradoEn) {
        this.cerradoEn = cerradoEn;
    }

    public Date getModificadoEn() {
        return modificadoEn;
    }

    public void setModificadoEn(Date modificadoEn) {
        this.modificadoEn = modificadoEn;
    }

    public Date getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(Date creadoEn) {
        this.creadoEn = creadoEn;
    }

    public long getPullRequestId() {
        return pullRequestId;
    }

    public void setPullRequestId(long pullRequestId) {
        this.pullRequestId = pullRequestId;
    }

    public int getArchivosModificados() {
        return archivosModificados;
    }

    public void setArchivosModificados(int archivosModificados) {
        this.archivosModificados = archivosModificados;
    }

    public int getComentarios() {
        return comentarios;
    }

    public void setComentarios(int comentarios) {
        this.comentarios = comentarios;
    }

    public int getCommits() {
        return commits;
    }

    public void setCommits(int commits) {
        this.commits = commits;
    }

    public int getEliminados() {
        return eliminados;
    }

    public void setEliminados(int eliminados) {
        this.eliminados = eliminados;
    }

    public Hito getHito() {
        return hito;
    }

    public void setHito(Hito hito) {
        this.hito = hito;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
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

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getIncidenciaUrl() {
        return incidenciaUrl;
    }

    public void setIncidenciaUrl(String incidenciaUrl) {
        this.incidenciaUrl = incidenciaUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
