package com.layala.gitdata.servicios;

import com.layala.gitdata.configuraciones.Configuracion;
import com.layala.gitdata.entidades.Etiqueta;
import com.layala.gitdata.entidades.Hito;
import com.layala.gitdata.entidades.Incidencia;
import com.layala.gitdata.entidades.PullRequest;
import com.layala.gitdata.entidades.Repositorio;
import com.layala.gitdata.entidades.Usuario;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.IssueService;

/**
 *
 * @author Luis
 */
public class IncidenciaSrv {
    
    /**
     * 
     * @param repositorio
     * @return
     * @throws IOException 
     */
    public List<Incidencia> getIncidenciasPorRepositorio(Repositorio repositorio) throws IOException {
        IssueService issueSrv = new IssueService();
        issueSrv.getClient().setCredentials(Configuracion.getProperty("usuario"),
                Configuracion.getProperty("password"));
        
        Incidencia incidencia = null;
        List<Incidencia> lista = new ArrayList<>();
        for (Issue issue : issueSrv.getIssues(Configuracion.getProperty("usuario"), 
                repositorio.getNombre(), null)) {
            incidencia = new Incidencia();
            incidencia.setIncidenciaId(issue.getId());
            incidencia.setCerradaEn(issue.getClosedAt());
            incidencia.setCreadaEn(issue.getCreatedAt());
            incidencia.setModificadaEn(issue.getUpdatedAt());
            incidencia.setComentarios(issue.getComments());
            incidencia.setCuerpo(issue.getBody());
            incidencia.setHtmlUrl(issue.getHtmlUrl());
            incidencia.setUrl(issue.getUrl());
            incidencia.setEstado(issue.getState());
            incidencia.setTitulo(issue.getTitle());
            incidencia.setHito(getHito(issue.getMilestone()));
            incidencia.setEtiquetas(getEtiqueteas(issue.getLabels()));
            incidencia.setPullRequest(getPullRequest(issue.getPullRequest()));
            incidencia.setUsuario(getUsuario(issue.getUser()));
            incidencia.setAsignadoA(getUsuario(issue.getAssignee()));
            incidencia.setRepositorio(repositorio);
            lista.add(incidencia);
        }
        return lista;
    }
    
    /**
     * 
     * @param milestone
     * @return 
     */
    private Hito getHito(Milestone milestone) {
        Hito hito = new Hito();
        if (milestone != null) {
            hito.setCreadoEn(milestone.getCreatedAt());
            hito.setDescripcion(milestone.getDescription());
            hito.setEstado(milestone.getState());
            hito.setTitulo(milestone.getTitle());
            hito.setUrl(milestone.getUrl());
            hito.setIncidenciasAbiertas(milestone.getOpenIssues());
            hito.setIncidenciasCerradas(milestone.getClosedIssues());
        }
        return hito;
    }
    
    /**
     * 
     * @param labels
     * @return
     * @throws NullPointerException 
     */
    private List<Etiqueta> getEtiqueteas(List<Label> labels) throws NullPointerException {
        List<Etiqueta> etiquetas = new ArrayList<>();
        if (labels != null) {
            Etiqueta etiqueta = null;
            for (Label label : labels) {
                etiqueta = new Etiqueta();
                etiqueta.setNombre(label.getName());
                etiqueta.setUrl(label.getUrl());
                etiquetas.add(etiqueta);
            }
        }
        return etiquetas;
    }
    
    /**
     * 
     * @param pullRequest
     * @return 
     */
    private PullRequest getPullRequest(org.eclipse.egit.github.core.PullRequest pullRequest) {
        PullRequest pull = new PullRequest();
        if (pullRequest != null) {
            pull.setCerradoEn(pullRequest.getClosedAt());
            pull.setModificadoEn(pullRequest.getUpdatedAt());
            pull.setCreadoEn(pullRequest.getCreatedAt());
            pull.setPullRequestId(pullRequest.getId());
            pull.setArchivosModificados(pullRequest.getChangedFiles());
            pull.setComentarios(pullRequest.getComments());
            pull.setCommits(pullRequest.getCommits());
            pull.setEliminados(pullRequest.getDeletions());
            pull.setHito(getHito(pullRequest.getMilestone()));
            pull.setCuerpo(pullRequest.getBody());
            pull.setEstado(pullRequest.getState());
            pull.setTitulo(pullRequest.getTitle());
            pull.setHtmlUrl(pullRequest.getHtmlUrl());
            pull.setIncidenciaUrl(pullRequest.getIssueUrl());
            pull.setUrl(pullRequest.getUrl());
        }
        return pull;
    }
    
    /**
     * 
     * @param user
     * @return 
     */
    private Usuario getUsuario(User user) {
        Usuario usuario = new Usuario();
        if(user != null) {
            usuario.setCreadoEn(user.getCreatedAt());
            usuario.setEmail(user.getEmail());
            usuario.setHtmlUrl(user.getHtmlUrl());
            usuario.setNombre(user.getName());
            usuario.setUrl(user.getUrl());
            usuario.setUsuarioId(user.getId());
        }
        return usuario;
    }
}
