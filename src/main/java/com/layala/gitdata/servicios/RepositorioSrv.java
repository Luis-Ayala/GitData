package com.layala.gitdata.servicios;

import com.google.gson.Gson;
import com.layala.gitdata.configuraciones.Configuracion;
import com.layala.gitdata.entidades.Repositorio;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
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
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

/**
 * Clase de servicio para administrar los repositorios
 *
 * @author Luis
 */
public class RepositorioSrv {

    private static final Logger LOGGER = LogManager.getLogger(RepositorioSrv.class);

    /**
     * Actualiza una lista de repositorios
     * @param repositorios Lista de repositorios a actualizar
     * @return Número de repositorios actualizados
     */
    public long actualizarRepositorio(final List<Repositorio> repositorios) {
        if (repositorios == null || repositorios.isEmpty()) {
            throw new RuntimeException("La lista no puede ser nula o vacia");
        }

        final List<Long> resultados;
        try (MongoClient cliente = Configuracion.crearConexion()) {
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
        if(modificados != 0)
            LOGGER.info("Se actualizaron los repositorios, total: " + modificados);
        
        return modificados;
    }

    /**
     * Actualiza un repositorio
     * @param repositorio Repositorio a actualizar
     * @return 1 si se actualizó el repositorio 0 en caso contrario
     */
    public long actualizarRepositorio(final Repositorio repositorio) {
        long actualizado = 0;
        try (MongoClient cliente = Configuracion.crearConexion()) {
            final MongoDatabase mongo = cliente.getDatabase(Configuracion.getProperty("database"));
            final MongoCollection<Document> coleccion = mongo.getCollection(Configuracion.getProperty("col_repositorios"));
            final Gson gson = new Gson();

            final UpdateResult resultado = coleccion.replaceOne(eq("repositorioId", String.valueOf(repositorio.getRepositorioId())),
                    Document.parse(gson.toJson(repositorio)));
            actualizado = resultado != null ? resultado.getModifiedCount() : 0L;
        }
        if(actualizado != 0) 
            LOGGER.info("Se actualizó el repositorio: " + repositorio.getNombre());
        
        return actualizado;
    }

    public long insertarRepositorio(List<Repositorio> repositorios) {
        if (repositorios == null || repositorios.isEmpty()) {
            throw new RuntimeException("La lista no puede ser nula o vacia");
        }

        final Gson gson = new Gson();
        final List<WriteModel<Document>> documentos = new ArrayList<>(repositorios.size());
        final BulkWriteResult resultado;
        repositorios.stream().map((repositorio) -> gson.toJson(repositorio)).forEachOrdered((json) -> {
            final Document documento = Document.parse(json);
            documentos.add(new InsertOneModel<>(documento));
        });

        try (MongoClient cliente = MongoClients.create("mongodb://127.0.0.1:27017")) {
            MongoDatabase mongo = cliente.getDatabase("GitData");
            MongoCollection<Document> coleccion = mongo.getCollection("repositorios");
            resultado = coleccion.bulkWrite(documentos);
            LOGGER.info("Se insertaron los repositorios, total: " + documentos.size());
        }

        return Long.valueOf(resultado.getInsertedCount());
    }

    public long insertarRepositorio(final Repositorio repositorio) {
        final Gson gson = new Gson();
        final String json = gson.toJson(repositorio);
        final int resultado = 1;
        try (MongoClient cliente = MongoClients.create("mongodb://127.0.0.1:27017")) {
            MongoDatabase mongo = cliente.getDatabase("GitData");
            MongoCollection<Document> coleccion = mongo.getCollection("repositorios");
            Document documento = Document.parse(json);
            coleccion.insertOne(documento);

            LOGGER.info("Se insertó el repositorio: " + repositorio.getNombre());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Long.valueOf(resultado);
    }

    public List<Repositorio> getRepositorios() throws IOException {
        final RepositoryService repoSrv = new RepositoryService();
        repoSrv.getClient().setCredentials(Configuracion.getCredenciales().get(Configuracion.USUARIO_K),
                Configuracion.getCredenciales().get(Configuracion.PWD_K));

        Repositorio repositorio = null;
        final List<Repositorio> lista = new ArrayList<>();
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
        return lista;
    }
}
