package com.layala.gitdata.configuraciones;

import com.layala.gitdata.excepciones.GitDataConfigExcepcion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase de configuraciones
 * 
 * @author Luis Ayala
 * @version 1.0
 * @since 1.0
 */
public final class Configuracion {
    
    private static final Logger LOGGER = LogManager.getLogger(Configuracion.class);  
    
    /**
     * Obtiene el valor de una propiedad dada del archivo config.properties
     * @param llave Llave de la propiedad a buscar
     * @return El valor de la propiedad
     * @throws com.layala.gitdata.excepciones.GitDataConfigExcepcion
     */
    public final static String getProperty(String llave) throws GitDataConfigExcepcion {
        LOGGER.info("Entrando al método getProperty.");
        if(llave == null || llave.isEmpty()) {
            throw new GitDataConfigExcepcion("La llave no puede ser nula o vacía.", new IllegalArgumentException());
        }
        final Properties prop = new Properties();
        try {
            final String archivo = "config.properties";
            final InputStream input = Configuracion.class.getClassLoader().getResourceAsStream(archivo);
            if (input == null) {
                throw new GitDataConfigExcepcion("No se pudo cargar el archivo de propiedades");
            }
            
            prop.load(input);
        } catch (IOException ex) {
            throw new GitDataConfigExcepcion(ex);
        }
        LOGGER.info("Método getProperty finalizado.");
        return prop.getProperty(llave);
    }
    
    /**
     * Crea la conexion a la base de datos
     * 
     * @return MongoClient
     * @throws com.layala.gitdata.excepciones.GitDataConfigExcepcion
     */
    public final static MongoClient crearConexion() throws GitDataConfigExcepcion {
        final String SERVIDOR = Configuracion.getProperty("servidor");
        final String PUERTO = Configuracion.getProperty("puerto");
        final String CONEXION = new StringBuilder().append("mongodb://").append(SERVIDOR).append(":").append(PUERTO).toString();
        final MongoClient cliente = MongoClients.create(CONEXION);
        return cliente;
    }
}
