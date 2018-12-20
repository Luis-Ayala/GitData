package com.layala.gitdata.servicios;

import com.layala.gitdata.configuraciones.Configuracion;
import com.layala.gitdata.entidades.Repositorio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

/**
 *
 * @author Luis
 */
public class RepositorioSrv {
    public List<Repositorio> getRepositorios() throws IOException {
        RepositoryService repoSrv = new RepositoryService();
        repoSrv.getClient().setCredentials(Configuracion.getCredenciales().get(Configuracion.USUARIO_K),
                                           Configuracion.getCredenciales().get(Configuracion.PWD_K));
        
        Repositorio repositorio = null;
        List<Repositorio> lista = new ArrayList<>();
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
