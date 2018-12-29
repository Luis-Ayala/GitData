package com.layala.gitdata.servicios;

import com.google.gson.Gson;
import com.layala.gitdata.configuraciones.Configuracion;
import com.layala.gitdata.entidades.Repositorio;
import com.layala.gitdata.excepciones.GitDataConfigExcepcion;
import com.layala.gitdata.excepciones.GitDataRepositorioExcepcion;
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
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

/**
 * Clase de servicio para tratar con los repositorios.
 * Esta clase es la encargada de procesar los repositorios,
 * este procesamiento consiste en recuperar los repositorios, 
 * actualizar e insertarlos en la base de datos.
 * 
 * @author Luis Ayala
 * @version 1.0
 * @since 1.0
 */
public class RepositorioSrv {

    private static final Logger LOGGER = LogManager.getLogger(RepositorioSrv.class);

    /**
     * Actualiza los repositorios que se pasan al método en el parámetro <code>repositorios</code> 
     * Este método reemplaza el repositorio por completo por uno nuevo, si no se
     * localiza el repositorio este es creado.
     * <p>
     * La búsqueda del repositorio en la base de datos se hace por medio del campo
     * <code>repositorioId</code>
     * 
     * @param repositorios Lista de repositorios a actualizar
     * @return Número de repositorios actualizados
     * @throws com.layala.gitdata.excepciones.GitDataRepositorioExcepcion
     * @throws com.layala.gitdata.excepciones.GitDataConfigExcepcion
     */
    public long actualizarRepositorio(final List<Repositorio> repositorios) throws GitDataRepositorioExcepcion, GitDataConfigExcepcion {
        LOGGER.info("Entrando al método actualizarRepositorio(final List<Repositorio> repositorios).");
        if (repositorios == null) {
            throw new GitDataRepositorioExcepcion("La lista no puede ser nula", new IllegalArgumentException());
        }

        final List<Long> resultados;
        try (final MongoClient cliente = Configuracion.crearConexion()) {
            final MongoDatabase mongo = cliente.getDatabase(Configuracion.getProperty("database"));
            final MongoCollection<Document> coleccion = mongo.getCollection(Configuracion.getProperty("col_repositorios"));
            final Gson gson = new Gson();
            resultados = new ArrayList<>(repositorios.size());
            repositorios.forEach((repositorio) -> {
                final FindIterable<Document> buscado = coleccion.find(eq("repositorioId", repositorio.getRepositorioId()));
                if (buscado != null && buscado.first() != null) {
                    final UpdateResult resultado = coleccion.replaceOne(buscado.first(),
                            Document.parse(gson.toJson(repositorio)),
                            new ReplaceOptions().upsert(true));
                    resultados.add(resultado.getModifiedCount());
                }
            });
        }

        long modificados = resultados.stream().mapToLong(Long::longValue).sum();
        if(modificados != 0) {
            LOGGER.info("Se actualizaron los repositorios, total: " + modificados);
        }
        LOGGER.info("Método actualizarRepositorio(final List<Repositorio> repositorios) finalizado.");
        return modificados;
    }

    /**
     * Actualiza el repositorio que se pasa al método en el parámetro <code>repositorio</code> 
     * Este método reemplaza el repositorio por completo por uno nuevo, si no se
     * localiza el repositorio este es creado.
     * <p>
     * La búsqueda del repositorio en la base de datos se hace por medio del campo
     * <code>repositorioId</code>
     * 
     * @param repositorio Repositorio a actualizar
     * @return Número de repositorios actualizados
     * @throws com.layala.gitdata.excepciones.GitDataRepositorioExcepcion
     */
    public long actualizarRepositorio(final Repositorio repositorio) throws GitDataRepositorioExcepcion {
        LOGGER.info("Entrando al método actualizarRepositorio(final Repositorio repositorio).");
        long actualizado = 0;
        try (final MongoClient cliente = Configuracion.crearConexion()) {
            final MongoDatabase mongo = cliente.getDatabase(Configuracion.getProperty("database"));
            final MongoCollection<Document> coleccion = mongo.getCollection(Configuracion.getProperty("col_repositorios"));
            final Gson gson = new Gson();

            final UpdateResult resultado = coleccion.replaceOne(eq("repositorioId", String.valueOf(repositorio.getRepositorioId())),
                    Document.parse(gson.toJson(repositorio)));
            actualizado = resultado != null ? resultado.getModifiedCount() : 0L;
        }catch(Exception e) {
            throw new GitDataRepositorioExcepcion(e);
        }
        if(actualizado != 0) {
            LOGGER.info("Se actualizó el repositorio: " + repositorio.getNombre());
        }
        LOGGER.info("Método actualizarRepositorio(final Repositorio repositorio) finalizado.");
        return actualizado;
    }
    
    /**
     * Inserta los repositorios que se le pasan al método en el parámetro <code>repositorios</code> 
     * 
     * @param repositorios Lista de repositorios a insertar
     * @return Número de repositorios insertados
     * @throws com.layala.gitdata.excepciones.GitDataRepositorioExcepcion
     */
    public long insertarRepositorio(final List<Repositorio> repositorios) throws GitDataRepositorioExcepcion {
        LOGGER.info("Entrando al método insertarRepositorio(final List<Repositorio> repositorios).");
        if (repositorios == null) {
            throw new GitDataRepositorioExcepcion("La lista no puede ser nula", new IllegalArgumentException());
        }

        final Gson gson = new Gson();
        final List<WriteModel<Document>> documentos = new ArrayList<>(repositorios.size());
        final BulkWriteResult resultado;
        repositorios.stream().map((repositorio) -> gson.toJson(repositorio)).forEachOrdered((json) -> {
            documentos.add(new InsertOneModel<>(Document.parse(json)));
        });

        try (final MongoClient cliente = Configuracion.crearConexion()) {
            final MongoDatabase mongo = cliente.getDatabase(Configuracion.getProperty("database"));
            final MongoCollection<Document> coleccion = mongo.getCollection(Configuracion.getProperty("col_repositorios"));
            resultado = coleccion.bulkWrite(documentos);
            LOGGER.info("Se insertaron los repositorios, total: " + resultado.getInsertedCount());
        }catch(Exception e) {
            throw new GitDataRepositorioExcepcion(e);
        }
        LOGGER.info("Método insertarRepositorio(final List<Repositorio> repositorios) finalizado.");
        return Long.valueOf(resultado.getInsertedCount());
    }
    
    /**
     * Inserta el repositorio que se le pasa al método en el parámetro <code>repositorio</code> 
     * 
     * @param repositorio Repositorio a insertar
     * @return 1 Si se insertó el repositorio
     * @throws com.layala.gitdata.excepciones.GitDataRepositorioExcepcion
     */
    public long insertarRepositorio(final Repositorio repositorio) throws GitDataRepositorioExcepcion {
        LOGGER.info("Entrando al método insertarRepositorio(final Repositorio repositorio).");
        final Gson gson = new Gson();
        final String json = gson.toJson(repositorio);
        final int resultado = 1;
        try (final MongoClient cliente = Configuracion.crearConexion()) {
            final MongoDatabase mongo = cliente.getDatabase(Configuracion.getProperty("database"));
            final MongoCollection<Document> coleccion = mongo.getCollection(Configuracion.getProperty("col_repositorios"));
            Document documento = Document.parse(json);
            coleccion.insertOne(documento);

            LOGGER.info("Se insertó el repositorio: " + repositorio.getNombre());
        }catch(Exception e) {
            throw new GitDataRepositorioExcepcion(e);
        }
        LOGGER.info("Método insertarRepositorio(final Repositorio repositorio) finalizado.");
        return Long.valueOf(resultado);
    }
    
    /**
     * Regresa todos los repositorios
     * 
     * @return Lista con los repositorios
     * @throws com.layala.gitdata.excepciones.GitDataRepositorioExcepcion 
     */
    public List<Repositorio> getRepositorios() throws GitDataRepositorioExcepcion {
        LOGGER.info("Entrando al método getRepositorios.");
        final List<Repositorio> lista = new ArrayList<>();
        try {
            final RepositoryService repoSrv = new RepositoryService();
            repoSrv.getClient().setCredentials(Configuracion.getProperty("usuario"),
                Configuracion.getProperty("password"));

            Repositorio repositorio = null;
            for (Repository repo : repoSrv.getRepositories()) {
                repositorio = new Repositorio();
                repositorio.setTieneIncidencias(repo.isHasIssues());
                repositorio.setCreadoEn(repo.getCreatedAt());
                repositorio.setModificadoEn(repo.getUpdatedAt());
                repositorio.setRepositorioId(repo.getId());
                repositorio.setIncidenciasAbiertas(repo.getOpenIssues());
                repositorio.setDescripcion(repo.getDescription());
                repositorio.setHomepage(repo.getHomepage());
                repositorio.setLenguaje(repo.getLanguage());
                repositorio.setNombre(repo.getName());
                repositorio.setUrl(repo.getUrl());
                repositorio.setGitUrl(repo.getGitUrl());
                repositorio.setHtmlUrl(repo.getHtmlUrl());
                lista.add(repositorio);
            }
        }catch(Exception e) {
            throw new GitDataRepositorioExcepcion(e);
        }
        LOGGER.info("Método getHito getRepositorios.");
        return lista;
    }
}
