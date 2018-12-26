package com.layala.gitdata.servicios;

import com.google.gson.Gson;
import com.layala.gitdata.configuraciones.Configuracion;
import com.layala.gitdata.entidades.Comentario;
import com.layala.gitdata.entidades.Etiqueta;
import com.layala.gitdata.entidades.Hito;
import com.layala.gitdata.entidades.Incidencia;
import com.layala.gitdata.entidades.PullRequest;
import com.layala.gitdata.entidades.Repositorio;
import com.layala.gitdata.entidades.Usuario;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.UpdateResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.IssueService;

/**
 * Clase de servicio para tratar con las incidencias
 *
 * @author Luis
 */
public class IncidenciaSrv {

    private static final Logger LOGGER = LogManager.getLogger(IncidenciaSrv.class);
    
    /**
     * Actualiza una lista de incidencias
     * @param incidencias Lista de incidencias
     * @return Número de incidencias actualizadas
     */
    public long actualizarIncidencia(final List<Incidencia> incidencias) {
        if (incidencias == null || incidencias.isEmpty()) {
            throw new IllegalArgumentException("La lista no puede ser nula o vacia");
        }

        final List<Long> resultados;
        try (final MongoClient cliente = Configuracion.crearConexion()) {
            final MongoDatabase mongo = cliente.getDatabase(Configuracion.getProperty("database"));
            final MongoCollection<Document> coleccion = mongo.getCollection(Configuracion.getProperty("col_repositorios"));
            final Gson gson = new Gson();
            resultados = new ArrayList<>(incidencias.size());
            incidencias.forEach((incidencia) -> {
                final FindIterable<Document> buscado = coleccion.find(eq("incidenciaId", incidencia.getIncidenciaId()));
                if (buscado != null && buscado.first() != null) {
                    final UpdateResult resultado = coleccion.replaceOne(buscado.first(),
                            Document.parse(gson.toJson(incidencia)),
                            new ReplaceOptions().upsert(true));
                    resultados.add(resultado.getModifiedCount());
                }
            });
        }

        long modificados = resultados.stream().mapToLong(Long::longValue).sum();
        if(modificados != 0)
            LOGGER.info("Se actualizaron las incidencias, total: " + modificados);
        
        return modificados;
    }
    
    /**
     * Actualiza una incidencia
     * @param incidencia Incidencia a actualizar
     * @return Número de incidencias actualizadas
     */
    public long actualizarIncidencia(final Incidencia incidencia) {
        long actualizado = 0;
        try (final MongoClient cliente = Configuracion.crearConexion()) {
            final MongoDatabase mongo = cliente.getDatabase(Configuracion.getProperty("database"));
            final MongoCollection<Document> coleccion = mongo.getCollection(Configuracion.getProperty("col_incidencias"));
            final Gson gson = new Gson();

            final UpdateResult resultado = coleccion.replaceOne(eq("incidenciaId", String.valueOf(incidencia.getIncidenciaId())),
                    Document.parse(gson.toJson(incidencia)));
            actualizado = resultado != null ? resultado.getModifiedCount() : 0L;
        }
        if(actualizado != 0) 
            LOGGER.info("Se actualizó la incidencia: " + incidencia.getIncidenciaId());
        
        return actualizado;
    }
    
    /**
     * Inserta una lista de incidencias en mongo
     * @param incidencias Lista de incidencias a insertar en mongo
     * @return Número de incidencias insertadas
     */
    public long insertarIncidencia(final List<Incidencia> incidencias) {
        if (incidencias == null || incidencias.isEmpty()) {
            throw new IllegalArgumentException("La lista no puede ser nula o vacia");
        }

        final Gson gson = new Gson();
        final List<WriteModel<Document>> documentos = new ArrayList<>(incidencias.size());
        final BulkWriteResult resultado;
        incidencias.stream().map((incidencia) -> gson.toJson(incidencia)).forEachOrdered((json) -> {
            documentos.add(new InsertOneModel<>(Document.parse(json)));
        });

        try (final MongoClient cliente = Configuracion.crearConexion()) {
            final MongoDatabase mongo = cliente.getDatabase(Configuracion.getProperty("database"));
            final MongoCollection<Document> coleccion = mongo.getCollection(Configuracion.getProperty("col_incidencias"));
            resultado = coleccion.bulkWrite(documentos);
            LOGGER.info("Se insertaron las incidencias, total: " + resultado.getInsertedCount());
        }

        return Long.valueOf(resultado.getInsertedCount());
    }
    
    /**
     * Inserta una incidencia en mongo
     * @param incidencia incidencia a insertar
     * @return regresa el número de incidencias insertadas
     */
    public long insertarIncidencia(final Incidencia incidencia) {
        final Gson gson = new Gson();
        final String json = gson.toJson(incidencia);
        int resultado = 0;
        try (final MongoClient cliente = Configuracion.crearConexion()) {
            final MongoDatabase mongo = cliente.getDatabase(Configuracion.getProperty("database"));
            final MongoCollection<Document> coleccion = mongo.getCollection(Configuracion.getProperty("col_incidencias"));
            Document documento = Document.parse(json);
            coleccion.insertOne(documento);
            resultado += 1;
            LOGGER.info("Se insertó la incidencia: " + incidencia.getIncidenciaId());
        }

        return Long.valueOf(resultado);
    }

    /**
     * Regresa las incidencias del repositorio que se le pasa como parámetro
     *
     * @param repositorio Repositorio para buscar las incidencias
     * @return Lista de incidencias por repositorio
     * @throws IOException
     */
    public List<Incidencia> getIncidenciasPorRepositorio(final Repositorio repositorio) throws IOException {
        final IssueService issueSrv = new IssueService();
        issueSrv.getClient().setCredentials(Configuracion.getProperty("usuario"), Configuracion.getProperty("password"));

        final List<Incidencia> lista = new ArrayList<>();
        for (Issue issue : issueSrv.getIssues(Configuracion.getProperty("usuario"), repositorio.getNombre(), null)) {
            final Incidencia incidencia = new Incidencia();
            incidencia.setIncidenciaId(issue.getId());
            incidencia.setCerradaEn(issue.getClosedAt());
            incidencia.setCreadaEn(issue.getCreatedAt());
            incidencia.setModificadaEn(issue.getUpdatedAt());
            incidencia.setNumComentarios(issue.getComments());
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
            incidencia.setComentarios(getComentarios(issueSrv, repositorio, issue));
            lista.add(incidencia); 
        }
        return lista;
    }

    /**
     * Mapea un objeto milestone a un hito
     *
     * @param milestone
     * @return Regresa el hito del issue
     */
    private Hito getHito(final Milestone milestone) {
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
     * Mapea un objeto Label a una Etiqueta
     *
     * @param labels Lista de Labels
     * @return Lista de etiquetas
     */
    private List<Etiqueta> getEtiqueteas(final List<Label> labels) {
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
     * Mapea un objeto PullRequest de GitHub a un objeto PullRequest
     *
     * @param pullRequest
     * @return PullRequest del sistema
     */
    private PullRequest getPullRequest(final org.eclipse.egit.github.core.PullRequest pullRequest) {
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
     * Mapea un objeto User a un objeto Usuario
     *
     * @param user User de GitHub
     * @return Un objeto Usuario
     */
    private Usuario getUsuario(final User user) {
        Usuario usuario = new Usuario();
        if (user != null) {
            usuario.setCreadoEn(user.getCreatedAt());
            usuario.setEmail(user.getEmail());
            usuario.setHtmlUrl(user.getHtmlUrl());
            usuario.setNombre(user.getName());
            usuario.setUrl(user.getUrl());
            usuario.setUsuarioId(user.getId());
        }
        return usuario;
    }
    
    /**
     * Regresa la lista de comentarios de la incidencias
     * @param repositorio Repositorio a la cual pertenece la incidencia
     * @param issue Incidencia de la cual se quiere saber los comentarios
     * @return Lista de comentarios de la incidencia
     * @throws IOException 
     */
    private List<Comentario> getComentarios(final IssueService issueSrv, final Repositorio repositorio, final Issue issue) throws IOException {
        final List<Comment> comentarios = issueSrv.getComments(Configuracion.getProperty("usuario"), repositorio.getNombre(), issue.getNumber());
        final List<Comentario> lista = (comentarios != null && !comentarios.isEmpty()) ? new ArrayList<>(comentarios.size()) : new ArrayList<>(0);
        if(comentarios != null && !comentarios.isEmpty()) {
            comentarios.stream().forEach(comment -> {
                final Comentario comentario = new Comentario();
                comentario.setCreadoEn(comment.getCreatedAt());
                comentario.setActualizadoEn(comment.getUpdatedAt());
                comentario.setCuerpo(comment.getBody());
                comentario.setCuerpoHtml(comment.getBodyHtml());
                comentario.setCuerpoTexto(comment.getBodyText());
                comentario.setComentarioId(comment.getId());
                comentario.setUrl(comment.getUrl());
                comentario.setUsuario(getUsuario(comment.getUser()));
                lista.add(comentario);
            });
        }
        return lista;
    }
}
