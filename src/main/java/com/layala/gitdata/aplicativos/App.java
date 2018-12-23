package com.layala.gitdata.aplicativos;

import com.google.gson.Gson;
import com.layala.gitdata.entidades.Incidencia;
import com.layala.gitdata.entidades.Repositorio;
import com.layala.gitdata.servicios.IncidenciaSrv;
import com.layala.gitdata.servicios.RepositorioSrv;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import org.apache.logging.log4j.*;

/**
 *
 * @author Luis
 */
public class App {
    private static final Logger logger = LogManager.getLogger(App.class);
    
    public static void main(String... args) throws IOException {
        logger.trace("Entering application.");
        
        RepositorioSrv repoSrv = new RepositorioSrv();
        List<Repositorio> repositorios = repoSrv.getRepositorios();
        repoSrv.actualizarRepositorio(repositorios);

        /*IncidenciaSrv incidenciaSrv = new IncidenciaSrv();
        List<Incidencia> incidencias = new ArrayList<>();
        List<Incidencia> lista = null;
        for (Repositorio repositorio : repositorios) {
            lista = incidenciaSrv.getIncidenciasPorRepositorio(repositorio);

            incidencias.addAll(lista);
        }*/
    }
}
