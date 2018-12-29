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
import com.layala.gitdata.excepciones.GitDataConfigExcepcion;
import com.layala.gitdata.excepciones.GitDataIncidenciaExcepcion;
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
 * Clase de servicio para tratar con las incidencias.
 * Esta clase es la encargada de procesar las incidencias,
 * este procesamiento consiste en recuperar las incidencias de
 * un repositorio, actualizar e insertarlas en la base de datos.
 * 
 * @author Luis Ayala
 * @version 1.0
 * @since 1.0
 */
public class IncidenciaSrv {
    
    private static final Logger LOGGER = LogManager.getLogger(IncidenciaSrv.class);

    /**
     * Actualiza las incidencias que se le pasan al método en el parámetro <code>incidencias</code> 
     * Este método reemplaza la incidencia por completo por una nueva, si no se
     * localiza la incidencia esta es creada.
     * <p>
     * La búsqueda de la incidencia en la base de datos se hace por medio del campo
     * <code>incidenciaId</code>
     * 
     * @param incidencias Lista de incidencias a actualizar
     * @return Número de incidencias actualizadas
     * @throws com.layala.gitdata.excepciones.GitDataIncidenciaExcepcion
     */
    public long actualizarIncidencia(final List<Incidencia> incidencias) throws GitDataIncidenciaExcepcion {
        LOGGER.info("Entrando al método actualizarIncidencia(final List<Incidencia> incidencias).");
        
        if (incidencias == null) {
            throw new GitDataIncidenciaExcepcion("La lista no puede ser nula", new IllegalArgumentException());
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
        } catch(Exception e) {
            throw new GitDataIncidenciaExcepcion(e); 
        }

        long modificados = resultados.stream().mapToLong(Long::longValue).sum();
        if (modificados != 0) {
            LOGGER.info("Se actualizaron las incidencias, total: " + modificados);
        }
        LOGGER.info("Método actualizarIncidencia(final List<Incidencia> incidencias) finalizado.");
        return modificados;
    }

    /**
     * Actualiza la incidencia que se le pasa al método en el parámetro <code>incidencia</code> 
     * Este método reemplaza la incidencia por completo por una nueva, si no se
     * localiza la incidencia esta es creada.
     * <p>
     * La búsqueda de la incidencia en la base de datos se hace por medio del campo
     * <code>incidenciaId</code>
     *
     * @param incidencia Incidencia a actualizar
     * @return Número de incidencias actualizadas
     * @throws com.layala.gitdata.excepciones.GitDataIncidenciaExcepcion
     */
    public long actualizarIncidencia(final Incidencia incidencia) throws GitDataIncidenciaExcepcion {
        LOGGER.info("Entrando al método actualizarIncidencia(final Incidencia incidencia).");
        
        long actualizado = 0;
        try (final MongoClient cliente = Configuracion.crearConexion()) {
            final MongoDatabase mongo = cliente.getDatabase(Configuracion.getProperty("database"));
            final MongoCollection<Document> coleccion = mongo.getCollection(Configuracion.getProperty("col_incidencias"));
            final Gson gson = new Gson();

            final UpdateResult resultado = coleccion.replaceOne(eq("incidenciaId", String.valueOf(incidencia.getIncidenciaId())),
                    Document.parse(gson.toJson(incidencia)));
            actualizado = resultado != null ? resultado.getModifiedCount() : 0L;
        }catch(Exception e) {
            throw new GitDataIncidenciaExcepcion(e);
        }
        
        if (actualizado != 0) {
            LOGGER.info("Se actualizó la incidencia: " + incidencia.getIncidenciaId());
        }
        LOGGER.info("Método actualizarIncidencia(final Incidencia incidencia) finalizado.");
        return actualizado;
    }

    /**
     * Inserta las incidencias que se le pasan al método en el parámetro <code>incidencias</code> 
     * 
     * @param incidencias Lista de incidencias a insertar en mongo
     * @return Número de incidencias insertadas
     * @throws com.layala.gitdata.excepciones.GitDataIncidenciaExcepcion
     */
    public long insertarIncidencia(final List<Incidencia> incidencias) throws GitDataIncidenciaExcepcion {
        LOGGER.info("Entrando al método insertarIncidencia(final List<Incidencia> incidencias).");
        
        if (incidencias == null) {
            throw new GitDataIncidenciaExcepcion("La lista no puede ser nula", new IllegalArgumentException());
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
        } catch(Exception e) {
            throw new GitDataIncidenciaExcepcion(e);
        }
        LOGGER.info("Método actualizarIncidencia(final List<Incidencia> incidencias) finalizado.");
        return Long.valueOf(resultado.getInsertedCount());
    }

    /**
     * Inserta la incidencia que se le pasa al método en el parámetro <code>incidencia</code> 
     *
     * @param incidencia incidencia a insertar
     * @return regresa el número de incidencias insertadas
     * @throws com.layala.gitdata.excepciones.GitDataIncidenciaExcepcion
     */
    public long insertarIncidencia(final Incidencia incidencia) throws GitDataIncidenciaExcepcion {
        LOGGER.info("Entrando al método insertarIncidencia(final Incidencia incidencia).");
        
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
        } catch(Exception e) {
            throw new GitDataIncidenciaExcepcion(e);
        }
        
        LOGGER.info("Método insertarIncidencia(final Incidencia incidencia) finalizado.");
        return Long.valueOf(resultado);
    }

    /**
     * Regresa las incidencias del repositorio que se le pasa como parámetro <code>repositorio</code>
     *
     * @param repositorio Repositorio para buscar las incidencias
     * @return Lista de incidencias por repositorio
     * @throws com.layala.gitdata.excepciones.GitDataIncidenciaExcepcion
     */
    public List<Incidencia> getIncidenciasPorRepositorio(final Repositorio repositorio) throws GitDataIncidenciaExcepcion {
        LOGGER.info("Entrando al método getIncidenciasPorRepositorio(final Repositorio repositorio).");
        
        final List<Incidencia> lista = new ArrayList<>();
        try {
            final IssueService issueSrv = new IssueService();
            issueSrv.getClient().setCredentials(Configuracion.getProperty("usuario"), Configuracion.getProperty("password"));
            
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
        } catch(Exception e) {
            throw new GitDataIncidenciaExcepcion(e);
        } 
        LOGGER.info("Método getIncidenciasPorRepositorio(final Repositorio repositorio) finalizado.");
        return lista;
    }

    /**
     * Mapea un objeto milestone a un hito
     *
     * @param milestone
     * @return Regresa el hito de la incidencia
     */
    private Hito getHito(final Milestone milestone) {
        LOGGER.info("Entrando al método getHito.");
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
        LOGGER.info("Método getHito finalizado.");
        return hito;
    }

    /**
     * Mapea un objeto Label a una Etiqueta
     *
     * @param labels Lista de Labels
     * @return Lista de etiquetas
     */
    private List<Etiqueta> getEtiqueteas(final List<Label> labels) {
        LOGGER.info("Entrando al método getEtiqueteas.");
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
        LOGGER.info("Método getEtiqueteas finalizado.");
        return etiquetas;
    }

    /**
     * Mapea un objeto PullRequest de GitHub a un objeto PullRequest
     *
     * @param pullRequest
     * @return PullRequest del sistema
     */
    private PullRequest getPullRequest(final org.eclipse.egit.github.core.PullRequest pullRequest) {
        LOGGER.info("Entrando al método getPullRequest.");
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
        LOGGER.info("Método getPullRequest finalizado.");
        return pull;
    }

    /**
     * Mapea un objeto User a un objeto Usuario
     *
     * @param user User de GitHub
     * @return Un objeto Usuario
     */
    private Usuario getUsuario(final User user) {
        LOGGER.info("Entrando al método getUsuario.");
        Usuario usuario = new Usuario();
        if (user != null) {
            usuario.setCreadoEn(user.getCreatedAt());
            usuario.setEmail(user.getEmail());
            usuario.setHtmlUrl(user.getHtmlUrl());
            usuario.setNombre(user.getName());
            usuario.setUrl(user.getUrl());
            usuario.setUsuarioId(user.getId());
        }
        LOGGER.info("Método getUsuario finalizado.");
        return usuario;
    }

    /**
     * Regresa la lista de comentarios de la incidencias
     *
     * @param repositorio Repositorio a la cual pertenece la incidencia
     * @param issue Incidencia de la cual se quiere saber los comentarios
     * @return Lista de comentarios de la incidencia
     * @throws IOException
     * @throws com.layala.gitdata.excepciones.GitDataConfigExcepcion
     */
    private List<Comentario> getComentarios(final IssueService issueSrv, final Repositorio repositorio, final Issue issue) throws IOException, GitDataConfigExcepcion {
        LOGGER.info("Entrando al método getComentarios.");
        final List<Comment> comentarios = issueSrv.getComments(Configuracion.getProperty("usuario"), repositorio.getNombre(), issue.getNumber());
        final List<Comentario> lista = (comentarios != null && !comentarios.isEmpty()) ? new ArrayList<>(comentarios.size()) : new ArrayList<>(0);
        if (comentarios != null && !comentarios.isEmpty()) {
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
        LOGGER.info("Método getComentarios finalizado.");
        return lista;
    }
}
